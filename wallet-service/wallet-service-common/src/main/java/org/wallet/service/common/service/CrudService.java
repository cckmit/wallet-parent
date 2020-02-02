package org.wallet.service.common.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.wallet.common.dto.PageDTO;
import org.wallet.common.entity.IBaseEntity;
import org.wallet.dap.common.bind.search.Search;
import org.wallet.service.common.listener.ContainerLoadedProcessor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * service 接口 提供 简单的 增删改查
 * @author zengfucheng
 */
public interface CrudService<T extends IBaseEntity> extends ContainerLoadedProcessor {
    /**
     * 初始化
     */
    void init();

    /**
     * 获取实体类型
     * @return 实体名称
     */
    Class<T> getEntityClass();

    /**
     * 保存
     * @param t 实体
     * @return 实体
     */
	T save(T t);

    /**
     * 保存立马刷新内存到数据库
     * @param t 实体
     * @return 实体
     */
	T saveAndFlush(T t);

    /**
     * 批量保存
     * @param t 实体
     * @return 实体
     */
	List<T> saveBatch(List<T> t);

    /**
     * 单个删除实体 必须存在ID
     * @param t 实体
     */
	void delete(T t);

    /**
     * 根据主键删除
     * @param id 主键
     */
	void delete(Long id);

    /**
     * 删除所有的
     */
	void deleteAll();

    /**
     * 批量删除
     * @param ids 主键
     */
	void deleteByIds(List<String> ids);

    /**
     * 批量删除实体
     * @param ids 主键
     */
	void deleteEntities(List<T> ids);

    /**
     * 修改
     * @param t 实体
     * @return 实体
     */
	T update(T t);

    /**
     * 批量修改
     * @param t 实体
     * @return 实体
     */
	List<T> updateBatch(List<T> t);

    /**
     * 根据主键获取单个实体
     * @param id 主键
     * @return 实体
     */
	T findOne(Long id);

    /**
     * 获取单个实体
     * @param spec 搜索条件
     * @return 实体
     */
	T findOne(Specification<T> spec);

    /**
     * 获取单个实体
     * @param search 搜索条件
     * @return 实体
     */
	T findOne(Search search);

    /**
     * 获取单个实体
     * @param hql hql
     * @param param 参数
     * @return 实体
     */
	Map<String, Object> queryOneMapByJpql(String hql, Map<String, Object> param);

    /**
     * 获取单个实体
     * @param sql sql
     * @param clazz 类模版
     * @param param 参数
     * @return 实体
     */
	T queryOneBySql(String sql, Class<T> clazz, Map<String, Object> param);

    /**
     * 获取单个实体
     * @param sql sql
     * @param param 参数
     * @return 实体
     */
	T queryOneBySql(String sql, Map<String, Object> param);

    /**
     * 获取单个实体
     * @param sql sql
     * @param param 参数
     * @return 实体
     */
	Map<String, Object> queryOneMapBySql(String sql, Map<String, Object> param);

    /**
     * 获取所有实体
     * @return 实体
     */
	List<T> findAll();

    /**
     * 获取所有实体
     * @param ids 主键
     * @return 实体
     */
	List<T> findAll(List<Serializable> ids);

    /**
     * 获取所有实体
     * @param sort 排序
     * @return 实体
     */
	List<T> findAll(Sort sort);

    /**
     * 获取所有实体
     * @param spec 搜索条件
     * @param sort 排序
     * @return 实体
     */
	List<T> findAll(Specification<T> spec, Sort sort);

    /**
     * 获取所有实体
     * @param spec 搜索条件
     * @return 实体
     */
	List<T> findAll(Specification<T> spec);

    /**
     * 获取所有实体
     * @param search 搜索条件
     * @return 实体
     */
	List<T> findAll(Search search);

    /**
     * 分页查询实体
     * @param search 搜索条件
     * @return 实体
     */
	PageDTO<T> findPage(Search search);

    /**
     * 获取实体数据量
     * @return 数据量
     */
	Long getCount();

    /**
     * 获取实体数据量
     * @param search 搜索条件
     * @return 数据量
     */
	Long getCount(Search search);

    /**
     * 获取实体数据量
     * @param specification 搜索条件
     * @return 数据量
     */
	Long getCount(Specification<T> specification);

    /**
     * 对象是否存在
     * @param id 主键
     * @return 是否存在
     */
	Boolean exists(Long id);

    /**
     * 对象是否存在
     * @param search 搜索条件
     * @return 是否存在
     */
	Boolean exists(Search search);

    /**
     * 根据查询条件删除数据
     * @param search 查询条件
     * @return 删除数据数量
     */
    Integer deleteBy(Search search);
}
