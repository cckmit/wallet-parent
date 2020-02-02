package org.wallet.service.application.service.impl;

import org.springframework.stereotype.Service;
import org.wallet.common.constants.cache.WalletInfoCache;
import org.wallet.common.entity.wallet.WalletInfoEntity;
import org.wallet.service.application.dao.WalletInfoJpaDao;
import org.wallet.service.application.service.WalletInfoService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class WalletInfoServiceImpl extends AbstractCrudService<WalletInfoJpaDao, WalletInfoEntity> implements WalletInfoService {

    @Override
    public void deleteCustomCache() {
        cache.evict(WalletInfoCache.CACHE_PREFIX, WalletInfoCache.ALL);
        cache.evict(WalletInfoCache.CACHE_PREFIX, WalletInfoCache.CURRENT);
    }
}
