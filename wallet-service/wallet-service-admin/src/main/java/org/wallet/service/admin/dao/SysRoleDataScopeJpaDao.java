package org.wallet.service.admin.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wallet.common.entity.admin.SysRoleDataScopeEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface SysRoleDataScopeJpaDao extends BaseRepository<SysRoleDataScopeEntity> {

    /**
     * 根据用户ID查询数据权限
     * @param userId 用户ID
     * @return DeptID
     */
    @Query("select scope.deptId from SysRoleDataScopeEntity scope,SysRoleUserEntity roleUser " +
            "where scope.roleId = roleUser.roleId and roleUser.userId = ?1")
    List<Long> findDataScopeList(Long userId);

    /**
     * 根据角色ID 删除菜单
     * @param roleId 角色ID
     */
    @Modifying
    @Query("delete from SysRoleDataScopeEntity scope where scope.roleId = :#{#roleId}")
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色ID 查询菜单
     * @param roleId 角色ID
     * @return 菜单
     */
    @Query("select deptId from SysRoleDataScopeEntity scope where scope.roleId = :#{#roleId}")
    List<Long> findDeptIdByRoleId(@Param("roleId") Long roleId);
}
