package org.wallet.service.batch.dao;

import org.wallet.common.entity.wallet.WalletBlockTransEntity;
import org.wallet.service.common.dao.BaseRepository;

/**
 * @author zengfucheng
 **/
public interface WalletBlockTransJpaDao extends BaseRepository<WalletBlockTransEntity> {
    /**
     * 根据交易ID查询交易
     * @param trxId 交易ID
     * @return 交易
     */
    WalletBlockTransEntity findFirstByTrxId(String trxId);

    /**
     * 根据交易备注查询交易
     * @param memo 交易备注
     * @return 交易
     */
    WalletBlockTransEntity findFirstByMemo(String memo);
}
