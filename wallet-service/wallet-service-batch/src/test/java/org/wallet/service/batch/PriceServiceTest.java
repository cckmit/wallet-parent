package org.wallet.service.batch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.service.batch.service.PriceService;

/**
 * @author zengfucheng
 **/
@Slf4j
public class PriceServiceTest extends SpringBootJUnitTest {

    @Autowired
    PriceService priceService;

    @Test
    public void syncChainCoinUSDPrice(){
        priceService.syncChainCoinUSDPrice();
    }
    @Test
    public void syncAllCoinPrice(){
        priceService.syncAllCoinPrice();
    }
}
