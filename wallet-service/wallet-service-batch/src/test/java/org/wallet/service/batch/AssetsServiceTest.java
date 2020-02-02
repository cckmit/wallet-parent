package org.wallet.service.batch;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.service.batch.service.AssetsService;

/**
 * @author zengfucheng
 **/
public class AssetsServiceTest extends SpringBootJUnitTest {

    @Autowired
    private AssetsService assetsService;

    @Test
    public void updateAssetsStatisticsData(){
        assetsService.updateAssetsStatisticsData();
    }
}
