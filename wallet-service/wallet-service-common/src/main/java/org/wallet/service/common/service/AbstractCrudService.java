package org.wallet.service.common.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.map.MapUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.cache.CacheConstants;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.entity.BaseEntity;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.dap.common.bind.search.SearchFinal;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.ISequence;
import org.wallet.service.common.bind.JPACovert;
import org.wallet.service.common.bind.specification.MySpecification;
import org.wallet.service.common.dao.BaseRepository;
import org.wallet.service.common.exception.ServiceException;
import org.wallet.service.common.utils.MD5Util;
import org.wallet.service.common.utils.ReflectUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 所有业务类的基类，提供了基础的CRUD方法。
 */
@Slf4j
public abstract class AbstractCrudService<M extends BaseRepository<T>,T extends BaseEntity> implements CrudService<T> {
    public static final String CACHE_PREFIX = "CrudService";
    public static final String CACHE_ALL = "all";
    public static final String CACHE_PAGE = "page";
    public static final String CACHE_SEP = CacheConstants.SEP;

    private static final String FIND_JPQL = "select x from %s x where 1=1 ";
    private static final String DELETE_JPQL = "delete from %s x where 1=1 ";
    private static final String COUNT_JPQL = "select count(x) from %s x where 1=1 ";

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final Class<T> entityClass;
    protected String entityName;
    protected String findJPQL;
    protected String countJPQL;
    protected String deleteJPQL;

    @Autowired
    protected Cache cache;

    @Reference(group = DubboServiceGroup.DAP_SEQUENCE)
    protected ISequence sequence;

    /**
     * 不能直接调用这个 使用getEntityManager()获取
     */
    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * baseService 里面不能直接使用 repository，没有验证领域类型是否一致
     * 请使用 getRepository() 方法来获取 repository
     */
    @Autowired
    protected M repository;

    /**
     * 初始化
     */
    public AbstractCrudService() {
        this.entityClass = ReflectUtil.findParameterizedType(getClass(), 1);
        if (null == entityClass) {
            throw new ServiceException("初始化实体类模版失败");
        }
        this.entityName = entityClass.getName();
        this.findJPQL = String.format(FIND_JPQL, entityName);
        this.countJPQL = String.format(COUNT_JPQL, entityName);
        this.deleteJPQL = String.format(DELETE_JPQL, entityName);
    }

    /**
     * 验证是不是我们要的领域类型 不是直接报异常
     */
    protected M getRepository() throws ServiceException {
        boolean b = repository.support(entityName);
        if (!b) {
            throw new ServiceException("没有对应的领域类型");
        }
        return repository;
    }

    /**
     * 获取实体管理者
     */
    protected EntityManager getEntityManager() {
        if (entityManager.isOpen()) {
            return entityManager;
        } else {
            throw new ServiceException("EntityManager 已关闭");
        }
    }

    @Override
    public int level() {
        return 0;
    }

    @Override
    public void initializedHandle(ApplicationReadyEvent event) {
        this.init();
    }

    @Override
    public void init() {
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public T save(T t) {
        deleteEntityCache();
        if(null == t.getId()){
            t.setId(sequence.getSequence());
        }else if(t.isCoverageUpdate()){
            T e = findOne(t.getId());
            if(null == e){
                return null;
            }else{
                deleteEntityCache(t.getId());
                BeanUtil.copyProperties(t, e, CopyOptions.create().setIgnoreNullValue(true));
                t = e;
            }
        }
        try{
            return getRepository().save(t);
        }catch (DataIntegrityViolationException e) {
            Throwable cause = e.getCause();
            if(cause instanceof ConstraintViolationException){
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) cause;
                String constraintName = constraintViolationException.getConstraintName();
                SQLException sqlException = constraintViolationException.getSQLException();
                if(!StringUtils.isEmpty(constraintName)){
                    log.warn("保存[{}][{}]时违反约束[{}]", getEntityClass().getSimpleName(), t, constraintName);
                }else{
                    log.warn("保存[{}][{}]失败[{}]", getEntityClass().getSimpleName(), t, sqlException.getMessage());
                }
            } else {
                throw e;
            }
        }
        return null;
    }

    @Override
    public T saveAndFlush(T t) {
        deleteEntityCache();
        if(null == t.getId()){
            t.setId(sequence.getSequence());
        }
        return getRepository().saveAndFlush(t);
    }

    @Override
    public List<T> saveBatch(List<T> t) {
        deleteEntityCache();
        if(!CollectionUtils.isEmpty(t)){
            t.forEach(e -> {
                if(null == e.getId()){
                    e.setId(sequence.getSequence());
                }
            });
        }
        return getRepository().saveAll(t);
    }

    @Override
    @CacheEvict(value = CACHE_PREFIX, key = "#root.target.getEntityClass().getName() + #root.target.CACHE_SEP + #t.id", beforeInvocation = true)
    public void delete(T t) {
        deleteEntityCache();
        getRepository().delete(t);
    }

    @Override
    @CacheEvict(value = CACHE_PREFIX, key = "#root.target.getEntityClass().getName() + #root.target.CACHE_SEP + #id", beforeInvocation = true)
    public void delete(Long id) {
        deleteEntityCache();
        try {
            getRepository().deleteById(id);
        } catch (EmptyResultDataAccessException ignored){}
    }

    @Override
    public void deleteAll() {
        getRepository().deleteAllInBatch();
    }

    @Override
    public void deleteByIds(List<String> ids) {
        deleteEntityCache();

        String jpql = deleteJPQL + " and x.id in (:ids) ";
        getEntityManager().createQuery(jpql)
                .setParameter("ids", ids)
                .executeUpdate();
    }

    @Override
    public void deleteEntities(List<T> ids) {
        deleteEntityCache();
        getRepository().deleteInBatch(ids);
    }

    @Override
    @CachePut(value = CACHE_PREFIX, key = "#root.target.getEntityClass().getName() + #root.target.CACHE_SEP + #t.id")
    public T update(T t) {
        deleteEntityCache();
        return getEntityManager().merge(t);
    }

    @Override
    public List<T> updateBatch(List<T> ts) {
        List<T> result = new ArrayList<>();
        for (T t : ts) {
            result.add(this.update(t));
        }
        return result;
    }

    @Override
    @Cacheable(value = CACHE_PREFIX, key = "#root.target.getEntityClass().getName() + #root.target.CACHE_SEP + #id")
    public T findOne(Long id) {
        Optional<T> option = getRepository().findById(id);
        return option.orElse(null);
    }

    @Override
    public T findOne(Specification<T> spec) {
        Optional<T> option = getRepository().findOne(spec);
        return option.orElse(null);
    }

    @Override
    public T findOne(Search search) {
        Optional<T> option = getRepository().findOne(new MySpecification<T>(search).toPredicate());
        return option.orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> queryOneMapByJpql(String hql, Map<String, Object> param) {
        return (Map<String, Object>) getEntityManager()
                .createQuery(hql)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T queryOneBySql(String sql, Class<T> clazz, Map<String, Object> param) {
        return (T) getEntityManager()
                .createNativeQuery(sql, clazz)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T queryOneBySql(String sql, Map<String, Object> param) {
        return (T) getEntityManager()
                .createNativeQuery(sql)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> queryOneMapBySql(String sql, Map<String, Object> param) {
        return (Map<String, Object>) getEntityManager()
                .createNativeQuery(sql)
                .getSingleResult();
    }

    @Override
    @Cacheable(value = CACHE_PREFIX, key = "#root.target.getEntityClass().getName() + #root.target.CACHE_SEP + #root.target.CACHE_ALL")
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public List<T> findAll(List<Serializable> ids) {
        return getRepository().findAllById(ids);
    }

    @Override
    public List<T> findAll(Sort sort) {
        return getRepository().findAll(sort);
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort) {
        return getRepository().findAll(spec, sort);
    }

    @Override
    public List<T> findAll(Specification<T> spec) {
        return getRepository().findAll(spec);
    }

    @Override
    @Cacheable(value = CACHE_PREFIX, key = "#root.target.getEntityClass().getName() + #root.target.CACHE_SEP + " +
            "#root.target.CACHE_ALL + #root.target.CACHE_SEP + #root.target.searchCacheName(#search)")
    public List<T> findAll(Search search) {
        MySpecification<T> spec = new MySpecification<>(search);
        if(null != search.getSort()){
            return getRepository().findAll(spec.toPredicate(), covertSort(search.getSort()));
        }else{
            return getRepository().findAll(spec.toPredicate());
        }
    }

    @Override
    @Cacheable(value = CACHE_PREFIX, key = "#root.target.getEntityClass().getName() + #root.target.CACHE_SEP + " +
            "#root.target.CACHE_PAGE + #root.target.CACHE_SEP + #root.target.searchCacheName(#search)")
    public PageDTO<T> findPage(Search search) {
        if (null == search.getPage()) { search.setPage(PageDTO.of(SearchFinal.DEFAULT_PAGE, SearchFinal.DEFAULT_LIMIT)); }
        if (null == search.getSort()) { search.setSort(SortDTO.by(SortDTO.Direction.DESC, SearchFinal.DEFAULT_SORT)); }
        Pageable pageable = covertPage(search.getPage(), search.getSort());
        org.springframework.data.domain.Page<T> page = getRepository().findAll(new MySpecification<T>(search).toPredicate(), pageable);
        return new PageDTO<>(pageable.getPageNumber() + 1, pageable.getPageSize(), page.getTotalPages(), page.getTotalElements(), page.getContent());
    }

    @Override
    public Long getCount() {
        return getRepository().count();
    }

    @Override
    public Long getCount(Specification<T> specification) {
        return getRepository().count(specification);
    }

    @Override
    public Long getCount(Search search) {
        return getRepository().count(new MySpecification<T>(search).toPredicate());
    }

    @Override
    public Boolean exists(Long id) {
        return getRepository().existsById(id);
    }

    @Override
    public Boolean exists(Search search) {
        return !CollectionUtils.isEmpty(getRepository().findAll(new MySpecification<T>(search).toPredicate()));
    }

    @Override
    public Integer deleteBy(Search search) {
        List<T> entityList = findAll(search);

        if(!CollectionUtils.isEmpty(entityList)){
            deleteEntities(entityList);

            return entityList.size();
        }
        return 0;
    }

    public void flush() {
        getRepository().flush();
    }

    public void detach(T t) {
        getEntityManager().detach(t);
    }

    public String searchCacheName(Search search){
        String cacheName = "";
        if(!MapUtil.isEmpty(search.getParamMap())){
            cacheName += MD5Util.MD5(JSON.toJSONString(search.getParamMap()));
        }
        if(null != search.getPage() && null != search.getPage().getPageNo()){
            cacheName += CacheConstants.SEP + search.getPage().getPageNo();
        }
        if(null != search.getPage() && null != search.getPage().getPageSize()){
            cacheName += CacheConstants.SEP + search.getPage().getPageSize();
        }
        if(null != search.getSort() && !CollectionUtils.isEmpty(search.getSort().getOrders())){
            cacheName += CacheConstants.SEP + MD5Util.MD5(JSON.toJSONString(search.getSort().getOrders()));
        }
        return cacheName ;
    }

    private Pageable covertPage(PageDTO page, SortDTO sort){
        return JPACovert.covertPage(page, sort);
    }

    private Sort covertSort(SortDTO sort){
        return JPACovert.covertSort(sort);
    }

    protected void deleteEntityCache(){
        cache.evict(CACHE_PREFIX + CacheConstants.SPRING_SEP + getEntityClass().getName() + CacheConstants.SEP + CACHE_ALL);
        cache.evict(CACHE_PREFIX + CacheConstants.SPRING_SEP + getEntityClass().getName() + CacheConstants.SEP + CACHE_PAGE);

        deleteCustomCache();
    }

    protected void deleteEntityCache(Long id){
        cache.evict(CACHE_PREFIX + CacheConstants.SPRING_SEP + getEntityClass().getName(), id.toString());
    }

    /**
     * 删除自定义缓存
     */
    public void deleteCustomCache(){

    }
}
