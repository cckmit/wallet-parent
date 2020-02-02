package org.wallet.service.batch.task.quotes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.wallet.service.batch.service.PriceService;

/**
 * 币种行情价定时更新任务
 * @author zengfucheng
 **/
@Slf4j
@Component
public class CoinPriceTask {

    @Autowired
    private PriceService priceService;

    @Scheduled(cron = "30 * * * * *")
    public void syncQuotes(){
        priceService.syncAllCoinPrice();
    }
}
