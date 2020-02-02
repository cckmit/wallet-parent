package org.wallet.service.admin.dao;

import org.springframework.data.jpa.repository.Query;
import org.wallet.common.dto.admin.SysMenuDTO;
import org.wallet.common.entity.admin.SysMenuEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface SysMenuJpaDao extends BaseRepository<SysMenuEntity> {

    @Query("select distinct permissions from SysMenuEntity")
    List<String> getAllPermission();

    @Query("select menu.permissions from SysRoleUserEntity roleUser " +
            "left join SysRoleMenuEntity roleMenu on roleUser.roleId = roleMenu.roleId " +
            "left join SysMenuEntity menu on roleMenu.menuId = menu.id " +
            "where roleUser.userId = ?1 " +
            "order by menu.sort")
    List<String> getUserPermissions(Long userId);

    @Query("select menu from SysRoleUserEntity roleUser " +
            "left join SysRoleMenuEntity roleMenu on roleUser.roleId = roleMenu.roleId " +
            "left join SysMenuEntity menu on roleMenu.menuId = menu.id " +
            "where roleUser.userId = ?1 " +
            "order by menu.sort")
    List<SysMenuEntity> getUserMenus(Long userId);
}
