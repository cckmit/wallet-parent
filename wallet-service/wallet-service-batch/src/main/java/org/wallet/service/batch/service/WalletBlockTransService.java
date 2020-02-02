package org.wallet.service.batch.service;

import org.wallet.common.dto.wallet.WalletBlockTransDTO;
import org.wallet.common.entity.wallet.WalletBlockTransEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface WalletBlockTransService extends CrudService<WalletBlockTransEntity> {
    /**
     * 同步区块交易
     */
    void syncTrans(List<WalletBlockTransDTO> dtoList);

    /**
     * 修复同步失败，或发送邀请码失败的订单
     */
    void fixAbnormalTrans();

    /**
     * 获取EOS收款账号
     * @return 收款账号
     */
    String getEosReceiverAccount();
}
