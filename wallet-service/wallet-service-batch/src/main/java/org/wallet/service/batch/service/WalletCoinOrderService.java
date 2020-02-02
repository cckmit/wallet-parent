package org.wallet.service.batch.service;

import org.wallet.common.dto.wallet.WalletBlockTransDTO;
import org.wallet.common.entity.wallet.WalletCoinOrderEntity;
import org.wallet.service.common.service.CrudService;

import java.util.List;

/**
 * @author zengfucheng
 **/
public interface WalletCoinOrderService extends CrudService<WalletCoinOrderEntity> {
    /**
     * 支付订单
     * @param dto 支付交易数据
     */
    void paid(WalletBlockTransDTO dto);

    /**
     * 获取并发送邀请码
     * @param memo 交易备注
     * @param order 订单
     */
    void getInviteCodeAndSendCode(String memo, WalletCoinOrderEntity order);

    /**
     * 发送邀请码
     * @param email 手机号
     * @param inviteCode 邀请码
     * @return 发送结果
     */
    boolean sendInviteCode(String email, String inviteCode);

    /**
     * 查询状态异常的订单
     * @param receiveAccount 接收账户
     * @return 订单
     */
    List<WalletCoinOrderEntity> findAbnormalOrder(String receiveAccount);
}
