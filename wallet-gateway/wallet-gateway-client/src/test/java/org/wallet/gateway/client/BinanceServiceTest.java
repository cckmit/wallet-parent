package org.wallet.gateway.client;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerPrice;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zengfucheng
 **/
public class BinanceServiceTest extends SpringBootJUnitTest {

    @Autowired
    private BinanceApiRestClient client;

    @Test
    public void test() {
        List<TickerPrice> priceList = client.getAllPrices();
        for (TickerPrice price : priceList) {
            log.info("[{}:{}]", price.getSymbol(), price.getPrice());
        }
    }

    @Test
    public void testSingle() {
        TickerPrice price = client.getPrice("EOSUSDT");
        log.info("[{}:{}]", price.getSymbol(), price.getPrice());
    }
}
