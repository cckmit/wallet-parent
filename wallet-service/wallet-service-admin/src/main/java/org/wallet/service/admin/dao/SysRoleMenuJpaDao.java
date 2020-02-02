package org.wallet.service.admin.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wallet.common.entity.admin.SysRoleMenuEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface SysRoleMenuJpaDao extends BaseRepository<SysRoleMenuEntity> {

    /**
     * 根据角色ID 删除菜单
     * @param roleId 角色ID
     */
    @Modifying
    @Query("delete from SysRoleMenuEntity roleMenu where roleMenu.roleId = :#{#roleId}")
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID 查询所有菜单
     * @param roleId 角色ID
     * @return 菜单
     */
    @Query("select menuId from SysRoleMenuEntity roleMenu where roleMenu.roleId = :#{#roleId}")
    List<Long> findMenuIdByRoleId(@Param("roleId") Long roleId);
}
