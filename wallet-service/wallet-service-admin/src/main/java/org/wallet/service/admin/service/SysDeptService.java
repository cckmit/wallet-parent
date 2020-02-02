package org.wallet.service.admin.service;

import org.wallet.common.entity.admin.SysDeptEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface SysDeptService extends CrudService<SysDeptEntity> {
    /**
     * 获取用户的部门数据权限列表
     * @param userId 用户
     * @return 部门数据权限列表
     */
    List<Long> findDataScopeList(Long userId);
}
