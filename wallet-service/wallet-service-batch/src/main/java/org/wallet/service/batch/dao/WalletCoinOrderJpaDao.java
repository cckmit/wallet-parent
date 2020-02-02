package org.wallet.service.batch.dao;

import org.springframework.data.jpa.repository.Query;
import org.wallet.common.constants.CoinConstants;
import org.wallet.common.entity.wallet.WalletCoinOrderEntity;
import org.wallet.common.entity.wallet.WalletPayConfigEntity;
import org.wallet.service.common.dao.BaseRepository;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface WalletCoinOrderJpaDao extends BaseRepository<WalletCoinOrderEntity> {
    /**
     * 根据备注获取币种订单
     * @param memo 备注
     * @return 订单
     */
    WalletCoinOrderEntity findFirstByMemo(String memo);

    /**
     * 获取EOS支付配置
     * @return
     */
    @Query("select config from WalletPayConfigEntity config where config.type = '" + CoinConstants.EOS + "'")
    WalletPayConfigEntity getEOSPayConfig();

    /**
     * 获取异常状态订单
     * @param receiveAccount 接收账户
     * @return 订单
     */
    @Query("select order from WalletCoinOrderEntity order where order.status in ('INIT', 'GET_CODE_FAIL', 'SEND_FAIL') " +
            "and memo in (select trans.memo from WalletBlockTransEntity trans where trans.receiver = ?1)")
    List<WalletCoinOrderEntity> findAbnormalOrder(String receiveAccount);
}
