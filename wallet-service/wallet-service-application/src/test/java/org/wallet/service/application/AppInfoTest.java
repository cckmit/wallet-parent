package org.wallet.service.application;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.common.entity.application.AppInfoEntity;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.service.application.service.AppInfoService;

public class AppInfoTest extends SpringBootJUnitTest{

    @Reference(group = DubboServiceGroup.SERVICE_APP_INFO)
    private IService appInfoDubboService;

    @Test
    public void getAppInfoById() {
        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_INFO,
                "getAppInfoById", null, 6564057116594929664L);

        ServiceResponse response = appInfoDubboService.invoke(request);
        logger.info(JSON.toJSONString(response));
    }

    @Test
    public void findAllAppInfo() {
        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_INFO, "findAllAppInfo");
        ServiceResponse response = appInfoDubboService.invoke(request);
        logger.info(JSON.toJSONString(response));
    }

    @Test
    public void findAppInfo() {
        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_INFO, "findAppInfo");
        ServiceResponse response = appInfoDubboService.invoke(request);
        logger.info(JSON.toJSONString(response));
    }

    @Test
    public void updateAppInfo() {
        AppInfoEntity entity = new AppInfoEntity();
        entity.setId(6564352896056688640L);
        entity.setName("EOS Poker");

        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_INFO,
                "updateAppInfo", 1067246875800000002L, entity);

        ServiceResponse response = appInfoDubboService.invoke(request);
        logger.info(JSON.toJSONString(response));
    }

    @Test
    public void addAppInfo(){
        AppInfoEntity entity = new AppInfoEntity();

        entity.setChainId(6564057116594929664L);
        entity.setTypeId(6564346355765215232L);

        entity.setName("WhaleEX");
        entity.setIntro("WhaleEx鲸交所是架构在EOS公链上的去中心化交易所，愿景是成为全球流动性最强的去中心化交易所");
        entity.setIcon("/app/icon/info/whale_ex.png");
        entity.setUrl("https://www.whaleex.com/");
        entity.setSort(10);
        entity.setEnable(true);

        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_INFO,
                "addAppInfo", 1067246875800000002L, entity);

        appInfoDubboService.invoke(request);

        entity.setTypeId(6564346357531017216L);

        entity.setId(null);
        entity.setName("EOS Poker");
        entity.setIntro("EOS Poker");
        entity.setIcon("/app/icon/info/eos_poker.png");
        entity.setUrl("https://eospoker.win");
        entity.setSort(20);
        entity.setEnable(true);

        request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_INFO,
                "addAppInfo", 1067246875800000002L, entity);

        appInfoDubboService.invoke(request);
    }
}
