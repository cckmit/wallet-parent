package org.wallet.service.batch.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wallet.common.constants.MQConsumerGroup;
import org.wallet.common.constants.MQTopic;
import org.wallet.common.dto.wallet.WalletBlockTransDTO;
import org.wallet.dap.mq.RocketMQListener;
import org.wallet.dap.mq.annotation.MessageListener;
import org.wallet.service.batch.service.WalletCoinOrderService;

/**
 * @author zengfucheng
 **/
@Component
@MessageListener(consumerGroup = MQConsumerGroup.WALLET_ACCOUNT_ORDER_PAID, topic = MQTopic.WALLET_ACCOUNT_ORDER_PAID)
public class WalletAccountOrderPaidListener implements RocketMQListener<WalletBlockTransDTO> {

    @Autowired
    private WalletCoinOrderService walletCoinOrderService;

    @Override
    public void onMessage(WalletBlockTransDTO dto) {
        walletCoinOrderService.paid(dto);
    }
}
