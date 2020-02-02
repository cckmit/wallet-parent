package org.wallet.service.application.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wallet.common.constants.cache.WalletPayConfigCache;
import org.wallet.common.dto.wallet.WalletPayConfigDTO;
import org.wallet.common.entity.wallet.WalletPayConfigEntity;
import org.wallet.common.enums.wallet.PaymentTypeEnum;
import org.wallet.service.application.dao.WalletPayConfigJpaDao;
import org.wallet.service.application.service.WalletPayConfigService;
import org.wallet.service.common.service.AbstractCrudService;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author zengfucheng
 **/
@Service
public class WalletPayConfigServiceImpl extends AbstractCrudService<WalletPayConfigJpaDao, WalletPayConfigEntity> implements WalletPayConfigService {
    @PostConstruct
    public void initTypeCache(){
        List<WalletPayConfigEntity> list = getRepository().findAll();
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(payConfig -> {
                WalletPayConfigDTO dto = new WalletPayConfigDTO();

                BeanUtils.copyProperties(payConfig, dto);

                cache.put(WalletPayConfigCache.TYPE, dto.getType().name(), dto);
            });
        }
    }

    @Override
    public void deleteCustomCache() {
        cache.evict(WalletPayConfigCache.TYPE, PaymentTypeEnum.EOS.name());
        cache.evict(WalletPayConfigCache.TYPE, PaymentTypeEnum.AliPay.name());
        cache.evict(WalletPayConfigCache.TYPE, PaymentTypeEnum.WeChatPay.name());
        cache.evict(WalletPayConfigCache.TYPE, PaymentTypeEnum.PayPal.name());
    }
}
