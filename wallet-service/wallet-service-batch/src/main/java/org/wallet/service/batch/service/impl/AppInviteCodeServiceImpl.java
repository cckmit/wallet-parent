package org.wallet.service.batch.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.wallet.common.constants.field.AppInviteCodeField;
import org.wallet.common.entity.application.AppInviteCodeEntity;
import org.wallet.common.enums.application.AppInviteCodeStatusEnum;
import org.wallet.common.enums.application.AppInviteCodeTypeEnum;
import org.wallet.dap.common.dubbo.Responses;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.dap.common.utils.StringGenerator;
import org.wallet.service.batch.dao.AppInviteCodeJpaDao;
import org.wallet.service.batch.service.AppInviteCodeService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class AppInviteCodeServiceImpl extends AbstractCrudService<AppInviteCodeJpaDao, AppInviteCodeEntity> implements AppInviteCodeService {

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ServiceResponse createNewCode(AppInviteCodeTypeEnum type) {
        if(null == type){ return Responses.missingParam(AppInviteCodeField.TYPE); }

        AppInviteCodeEntity entity = new AppInviteCodeEntity();

        entity.setId(null);
        entity.setCreator(AppInviteCodeField.DEFAULT_ADMIN_USER_ID);

        String code = StringGenerator.newInstance().generate(8);
        entity.setCode(code);
        entity.setStatus(AppInviteCodeStatusEnum.UNUSED);
        entity.setType(type);
        entity.setRefId(0L);

        return Responses.success(save(entity));
    }
}
