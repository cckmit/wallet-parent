package org.wallet.service.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.wallet.dap.common.utils.CommonUtil;

/**
 * @author zengfucheng
 **/
@Component
public class StartListener implements ApplicationRunner{

    @Autowired
    private AssetsService assetsService;

    @Override
    public void run(ApplicationArguments args) {
        if(CommonUtil.allowTest()){
            assetsService.updateAssetsStatisticsData();
        }
    }

}