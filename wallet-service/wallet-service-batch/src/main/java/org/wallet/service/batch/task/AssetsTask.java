package org.wallet.service.batch.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.wallet.common.constants.cache.WalletBlockTransCache;
import org.wallet.dap.cache.lock.DapLock;
import org.wallet.service.batch.service.AssetsService;
import org.wallet.service.batch.service.WalletBlockTransService;

/**
 * 资产统计定时任务
 * @author zengfucheng
 **/
@Slf4j
@Component
public class AssetsTask {

    @Autowired
    private AssetsService assetsService;

    @Scheduled(cron = "50 59 23 * * *")
    public void updateAssetsStatisticsData(){
        assetsService.updateAssetsStatisticsData();
    }
}
