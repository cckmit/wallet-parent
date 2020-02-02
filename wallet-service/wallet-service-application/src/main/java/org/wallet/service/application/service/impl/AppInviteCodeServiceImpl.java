package org.wallet.service.application.service.impl;

import org.springframework.stereotype.Service;
import org.wallet.common.constants.field.AppInviteCodeField;
import org.wallet.common.entity.application.AppInviteCodeEntity;
import org.wallet.common.enums.application.AppInviteCodeStatusEnum;
import org.wallet.dap.common.dubbo.Responses;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.service.application.dao.AppInviteCodeJpaDao;
import org.wallet.service.application.service.AppInviteCodeService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class AppInviteCodeServiceImpl extends AbstractCrudService<AppInviteCodeJpaDao, AppInviteCodeEntity> implements AppInviteCodeService {

    @Override
    public ServiceResponse useCode(String code, Long refId) {

        if(null == refId){ return Responses.missingParam(AppInviteCodeField.REF_ID); }

        AppInviteCodeEntity entity = getRepository().findByCode(code);

        if(null == entity){ return Responses.notFoundData(AppInviteCodeField.CODE, code); }
        if(AppInviteCodeStatusEnum.USED.equals(entity.getStatus())){
            return Responses.illegalParam(String.format("该邀请码[%s]已被使用", code));
        }

        entity.setStatus(AppInviteCodeStatusEnum.USED);
        entity.setRefId(refId);

        return Responses.success(save(entity));
    }

    @Override
    public void rollbackCodeStatus(String code) {
        getRepository().rollbackCodeStatus(code, AppInviteCodeStatusEnum.UNUSED);
    }
}
