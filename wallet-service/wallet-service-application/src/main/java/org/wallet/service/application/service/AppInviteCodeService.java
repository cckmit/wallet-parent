package org.wallet.service.application.service;

import org.wallet.common.entity.application.AppInviteCodeEntity;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.service.common.service.CrudService;

/**
 * @author zengfucheng
 **/
public interface AppInviteCodeService extends CrudService<AppInviteCodeEntity> {

    /**
     * 使用邀请码
     * @param code 邀请码
     * @param refId 关联业务ID
     * @return 邀请码
     */
    ServiceResponse useCode(String code, Long refId);

    /**
     * 回滚邀请码状态到未使用
     * @param code 邀请码
     */
    void rollbackCodeStatus(String code);
}
