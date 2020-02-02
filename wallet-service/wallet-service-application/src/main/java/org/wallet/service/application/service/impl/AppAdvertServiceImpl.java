package org.wallet.service.application.service.impl;

import org.springframework.stereotype.Service;
import org.wallet.common.entity.application.AppAdvertEntity;
import org.wallet.service.application.dao.AppAdvertJpaDao;
import org.wallet.service.application.service.AppAdvertService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class AppAdvertServiceImpl extends AbstractCrudService<AppAdvertJpaDao, AppAdvertEntity> implements AppAdvertService {
}
