package org.wallet.service.admin.service;

import org.wallet.common.dto.admin.SysMenuDTO;
import org.wallet.common.entity.admin.SysMenuEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;
import java.util.Set;

/**
 * @author zengfucheng
 **/
public interface SysMenuService extends CrudService<SysMenuEntity> {
    /**
     * 查询用户权限列表
     * @param userId 用户ID
     * @param superAdmin 是否超级管理员
     * @return 权限列表
     */
    Set<String> getUserPermissions(Long userId, Boolean superAdmin);

    /**
     * 获取用户菜单列表
     * @param userId 用户ID
     * @param superAdmin 是否超级管理员
     * @return
     */
    List<SysMenuDTO> getUserMenus(Long userId, Boolean superAdmin);

    /**
     * 获取菜单树
     * @param id 菜单ID
     * @return 菜单树
     */
    SysMenuDTO findSysMenuTreeById(Long id);

    /**
     * 根据父级ID 查询菜单集合
     * @param parentId 父级ID
     * @param depthSearch 是否深度查询
     * @return 菜单集合
     */
    List<SysMenuDTO> findSysMenusByParentId(Long parentId, Boolean depthSearch);

    /**
     * 获取完整菜单树
     * @return 菜单树
     */
    List<SysMenuDTO> getSysMenuTree();
}
