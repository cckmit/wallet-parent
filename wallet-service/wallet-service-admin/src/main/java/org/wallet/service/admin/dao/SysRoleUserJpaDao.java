package org.wallet.service.admin.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wallet.common.entity.admin.SysRoleUserEntity;
import org.wallet.service.common.dao.BaseRepository;

/**
 * @author zengfucheng
 **/
public interface SysRoleUserJpaDao extends BaseRepository<SysRoleUserEntity> {
    /**
     * 删除用户角色
     * @param userId 用户ID
     * @return 删除数量
     */
    @Modifying
    @Query("delete from SysRoleUserEntity where userId = :#{#userId}")
    Integer deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询角色ID
     * @param userId 用户ID
     * @return 角色ID
     */
    @Query("select roleId from SysRoleUserEntity where userId = ?1")
    Long findRoleIdByUserId(Long userId);
}
