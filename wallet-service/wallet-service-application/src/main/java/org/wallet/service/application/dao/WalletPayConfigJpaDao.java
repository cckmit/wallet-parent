package org.wallet.service.application.dao;

import org.wallet.common.entity.wallet.WalletPayConfigEntity;
import org.wallet.common.enums.wallet.PaymentTypeEnum;
import org.wallet.service.common.dao.BaseRepository;

/**
 * @author zengfucheng
 **/
public interface WalletPayConfigJpaDao extends BaseRepository<WalletPayConfigEntity> {
    /**
     * 根据支付类型查询支付配置
     * @param type 类型
     * @return 配置
     */
    WalletPayConfigEntity getFirstByType(PaymentTypeEnum type);
}
