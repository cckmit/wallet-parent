package org.wallet.service.batch.service;

import org.wallet.common.entity.application.AppInviteCodeEntity;
import org.wallet.common.enums.application.AppInviteCodeTypeEnum;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.service.common.service.CrudService;

/**
 * @author zengfucheng
 **/
public interface AppInviteCodeService extends CrudService<AppInviteCodeEntity> {
    /**
     * 创建新邀请码
     * @param type 邀请码类型
     * @return 邀请码
     */
    ServiceResponse createNewCode(AppInviteCodeTypeEnum type);
}
