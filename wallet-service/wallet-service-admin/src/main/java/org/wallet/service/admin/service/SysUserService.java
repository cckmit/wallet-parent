package org.wallet.service.admin.service;

import org.wallet.common.dto.admin.SysUserDTO;
import org.wallet.common.entity.admin.SysUserEntity;
import org.wallet.service.common.service.CrudService;

/**
 * @author zengfucheng
 **/
public interface SysUserService extends CrudService<SysUserEntity> {

    /**
     * 根据ID 查询用户
     * @param id ID
     * @return 用户
     */
    SysUserDTO findDTOById(Long id);

    /**
     * 保存系统用户
     * @param dto 用户
     * @param userId 当前登录用户
     * @return 用户
     */
    SysUserEntity save(SysUserDTO dto, Long userId);
}
