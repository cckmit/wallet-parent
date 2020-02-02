package org.wallet.service.batch.task.quotes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.wallet.service.batch.service.PriceService;

/**
 * 主链币种美元行情价定时更新任务
 * @author zengfucheng
 **/
@Slf4j
@Component
public class ChainPriceTask {

    @Autowired
    private PriceService priceService;

    @Scheduled(cron = "0 * * * * *")
    public void syncQuotes(){
        priceService.syncChainCoinUSDPrice();
    }
}
