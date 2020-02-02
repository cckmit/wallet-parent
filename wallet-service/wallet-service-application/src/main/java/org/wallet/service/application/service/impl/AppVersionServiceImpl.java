package org.wallet.service.application.service.impl;

import org.springframework.stereotype.Service;
import org.wallet.common.entity.application.AppVersionEntity;
import org.wallet.service.application.dao.AppVersionJpaDao;
import org.wallet.service.application.service.AppVersionService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class AppVersionServiceImpl extends AbstractCrudService<AppVersionJpaDao, AppVersionEntity> implements AppVersionService {
}
