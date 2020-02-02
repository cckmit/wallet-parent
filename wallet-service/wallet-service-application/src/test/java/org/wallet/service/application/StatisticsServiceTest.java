package org.wallet.service.application;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.service.application.service.StatisticsService;

public class StatisticsServiceTest extends SpringBootJUnitTest{

    @Autowired
    private StatisticsService statisticsService;

    @Test
    public void getAppTypeById() {
        statisticsService.assetsStatistics(6564057116594929664L);
    }
}
