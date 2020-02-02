package org.wallet.common.constants;

public interface MQConsumerGroup {
    String ADMIN_LOGIN_LOG = "AdminLoginLogConsumer";
    String ADMIN_ERROR_LOG = "AdminErrorLogConsumer";
    String ADMIN_OPERATION_LOG = "AdminOperationLogConsumer";

    String WALLET_ACCOUNT_ORDER_PAID = "WalletAccountOrderPaidConsumer";
}
