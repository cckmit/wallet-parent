package org.wallet.service.application.service.impl;

import org.springframework.stereotype.Service;
import org.wallet.common.entity.application.AppMessageEntity;
import org.wallet.service.application.dao.AppMessageJpaDao;
import org.wallet.service.application.service.AppMessageService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class AppMessageServiceImpl extends AbstractCrudService<AppMessageJpaDao, AppMessageEntity> implements AppMessageService {
}
