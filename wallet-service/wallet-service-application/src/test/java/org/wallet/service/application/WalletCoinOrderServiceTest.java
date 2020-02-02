package org.wallet.service.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.common.constants.CoinConstants;
import org.wallet.common.enums.wallet.PaymentTypeEnum;
import org.wallet.service.application.service.WalletCoinOrderService;

/**
 * @author zengfucheng
 **/
@Slf4j
public class WalletCoinOrderServiceTest extends SpringBootJUnitTest {

    @Autowired
    WalletCoinOrderService walletCoinOrderService;

    @Test
    public void createOrder() {
        walletCoinOrderService.createOrder(CoinConstants.EOS, PaymentTypeEnum.PayPal, "snzke@live.cn");
    }
}
