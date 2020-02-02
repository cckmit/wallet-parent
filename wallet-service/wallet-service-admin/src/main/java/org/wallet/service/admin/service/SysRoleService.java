package org.wallet.service.admin.service;

import org.wallet.common.dto.admin.SysRoleDTO;
import org.wallet.common.entity.admin.SysRoleEntity;
import org.wallet.service.common.service.CrudService;

/**
 * @author zengfucheng
 **/
public interface SysRoleService extends CrudService<SysRoleEntity> {

    /**
     * 根据角色ID查询菜单和数据权限
     * @param id 角色ID
     * @return 角色
     */
    SysRoleDTO findDTOById(Long id);

    /**
     * 保存角色
     * @param dto 角色
     * @param userId
     * @return 角色
     */
    SysRoleEntity save(SysRoleDTO dto, Long userId);

    /**
     * 根据用户ID获取角色ID
     * @param userId 用户ID
     * @return 角色ID
     */
    Long findRoleIdByUserId(Long userId);
}
