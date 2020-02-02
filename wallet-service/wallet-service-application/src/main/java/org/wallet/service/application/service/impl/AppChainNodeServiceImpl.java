package org.wallet.service.application.service.impl;

import org.springframework.stereotype.Service;
import org.wallet.common.entity.application.AppChainNodeEntity;
import org.wallet.service.application.dao.AppChainNodeJpaDao;
import org.wallet.service.application.service.AppChainNodeService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class AppChainNodeServiceImpl extends AbstractCrudService<AppChainNodeJpaDao, AppChainNodeEntity> implements AppChainNodeService {
    @Override
    public void removeDefault(Long chainId) {
        getRepository().removeDefault(chainId);
    }
}
