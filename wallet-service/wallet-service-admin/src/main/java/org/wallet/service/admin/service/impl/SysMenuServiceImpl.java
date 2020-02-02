package org.wallet.service.admin.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.constants.cache.CacheConstants;
import org.wallet.common.constants.cache.SysMenuCache;
import org.wallet.common.constants.field.SysMenuField;
import org.wallet.common.dto.SortDTO;
import org.wallet.common.dto.admin.SysMenuDTO;
import org.wallet.common.entity.admin.SysMenuEntity;
import org.wallet.dap.common.bind.filter.SearchFilters;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.utils.TreeUtil;
import org.wallet.service.admin.dao.SysMenuJpaDao;
import org.wallet.service.admin.service.SysMenuService;
import org.wallet.service.common.service.AbstractCrudService;

import java.util.*;

/**
 * @author zengfucheng
 **/
@Service
public class SysMenuServiceImpl extends AbstractCrudService<SysMenuJpaDao, SysMenuEntity> implements SysMenuService {
    @Autowired
    private SysMenuJpaDao menuJpaDao;

    @Override
    public Set<String> getUserPermissions(Long userId, Boolean superAdmin) {
        //系统管理员，拥有最高权限
        List<String> permissionsList;
        if(superAdmin) {
            permissionsList = menuJpaDao.getAllPermission();
        }else{
            permissionsList = menuJpaDao.getUserPermissions(userId);
        }

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String permissions : permissionsList){
            if(StringUtils.isBlank(permissions)){
                continue;
            }
            permsSet.addAll(Arrays.asList(permissions.trim().split(",")));
        }

        return permsSet;
    }

    @Override
    public List<SysMenuDTO> getUserMenus(Long userId, Boolean superAdmin) {
        List<SysMenuDTO> menuList = new ArrayList<>();
        if(superAdmin) {
            return getSysMenuTree();
        }else{
            List<SysMenuEntity> entityList = menuJpaDao.getUserMenus(userId);
            if(!CollectionUtils.isEmpty(entityList)){
                List<SysMenuDTO> finalMenuList = menuList;
                entityList.forEach(entity -> {
                    SysMenuDTO dto = new SysMenuDTO();
                    BeanUtils.copyProperties(entity, dto);
                    finalMenuList.add(dto);
                });
                menuList = finalMenuList;
            }
        }

        if(!CollectionUtils.isEmpty(menuList)){
            menuList = TreeUtil.build(menuList);
        }

        return menuList;
    }

    @Override
    public SysMenuDTO findSysMenuTreeById(Long id) {
        SysMenuDTO dto = cache.get(SysMenuCache.TREE, id.toString(), SysMenuDTO.class);

        if(null != dto){
            return dto;
        }

        SysMenuEntity entity = findOne(id);

        if(null == entity){ return null; }

        dto = new SysMenuDTO();

        BeanUtils.copyProperties(entity, dto);

        List<SysMenuDTO> childList = findSysMenusByParentId(entity.getId(), true);

        if(!CollectionUtils.isEmpty(childList)){
            dto.setChildList(childList);
        }

        cache.put(SysMenuCache.TREE, id.toString(), dto, SysMenuCache.EXPIRE);

        return dto;
    }

    @Override
    public List<SysMenuDTO> findSysMenusByParentId(Long parentId, Boolean depthSearch){
        List<SysMenuEntity> entities = findAll(Searchs.of(
                SortDTO.asc(SysMenuField.PID, SysMenuField.SORT),
                SearchFilters.eq(SysMenuField.PID, parentId),
                SearchFilters.eq(SysMenuField.ENABLE, Boolean.TRUE)));

        if(!CollectionUtils.isEmpty(entities)){
            List<SysMenuDTO> dtoList = new ArrayList<>();
            entities.forEach(entity -> {
                SysMenuDTO dto = new SysMenuDTO();

                BeanUtils.copyProperties(entity, dto);

                if(depthSearch){
                    dto.setChildList(findSysMenusByParentId(dto.getId(), depthSearch));
                }

                dtoList.add(dto);
            });

            return dtoList;
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SysMenuDTO> getSysMenuTree() {
        List<SysMenuDTO> cacheList = cache.get(SysMenuCache.CACHE_PREFIX, CacheConstants.TREE, List.class);

        if(!CollectionUtils.isEmpty(cacheList)){
            return cacheList;
        }

        List<SysMenuEntity> entities = findAll(Searchs.of(
                SortDTO.asc(SysMenuField.PID, SysMenuField.SORT),
                SearchFilters.eq(SysMenuField.ENABLE, Boolean.TRUE))
        );

        if(!CollectionUtils.isEmpty(entities)){
            List<SysMenuDTO> dtoList = new ArrayList<>();
            entities.forEach(entity -> {
                SysMenuDTO dto = new SysMenuDTO();
                BeanUtils.copyProperties(entity, dto);
                dtoList.add(dto);
            });

            List<SysMenuDTO> result = TreeUtil.build(dtoList);

            cache.put(SysMenuCache.CACHE_PREFIX, CacheConstants.TREE, result, SysMenuCache.EXPIRE);

            return result;

        }
        return null;
    }

    @Override
    public void deleteCustomCache() {
        cache.evict(SysMenuCache.CACHE_PREFIX, CacheConstants.TREE);
        cache.evict(SysMenuCache.CACHE_PREFIX + CacheConstants.SEP + CacheConstants.TREE);
    }
}
