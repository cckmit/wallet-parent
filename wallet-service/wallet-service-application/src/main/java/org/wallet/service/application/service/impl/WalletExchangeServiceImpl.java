package org.wallet.service.application.service.impl;

import org.springframework.stereotype.Service;
import org.wallet.common.entity.wallet.WalletExchangeEntity;
import org.wallet.service.application.dao.WalletExchangeJpaDao;
import org.wallet.service.application.service.WalletExchangeService;
import org.wallet.service.common.service.AbstractCrudService;

/**
 * @author zengfucheng
 **/
@Service
public class WalletExchangeServiceImpl extends AbstractCrudService<WalletExchangeJpaDao, WalletExchangeEntity> implements WalletExchangeService {
}
