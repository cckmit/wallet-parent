package org.wallet.service.application.service.impl;

import org.springframework.stereotype.Service;
import org.wallet.common.entity.application.AppLinkEntity;
import org.wallet.service.application.dao.AppLinkJpaDao;
import org.wallet.service.application.service.AppLinkService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class AppLinkServiceImpl extends AbstractCrudService<AppLinkJpaDao, AppLinkEntity> implements AppLinkService {
}
