package org.wallet.service.application;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.common.entity.application.AppTypeEntity;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.service.application.service.AppTypeService;

public class AppTypeTest extends SpringBootJUnitTest{

    @Reference(group = DubboServiceGroup.SERVICE_APP_TYPE)
    private IService appTypeDubboService;

    @Test
    public void getAppTypeById() {
        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TYPE,
                "getAppTypeById", null, 6564346355765215232L);

        ServiceResponse response = appTypeDubboService.invoke(request);
        logger.info(JSON.toJSONString(response));
    }

    @Test
    public void findAllAppType() {
        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TYPE, "findAllAppType");
        ServiceResponse response = appTypeDubboService.invoke(request);
        logger.info(JSON.toJSONString(response));
    }

    @Test
    public void findAppTypeList() {
        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TYPE, "findAppType");
        ServiceResponse response = appTypeDubboService.invoke(request);
        logger.info(JSON.toJSONString(response));
    }

    @Test
    public void updateAppType() {
        AppTypeEntity entity = new AppTypeEntity();
        entity.setId(6564057116594929664L);
        entity.setName("EOS1");

        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TYPE,
                "updateAppType", 1067246875800000002L, entity);

        ServiceResponse response = appTypeDubboService.invoke(request);
        logger.info(JSON.toJSONString(response));
    }

    @Test
    public void addAppType(){
        AppTypeEntity entity = new AppTypeEntity();

        entity.setName("交易");
        entity.setIntro("交易");
        entity.setIcon("/app/icon/type/transfer.png");
        entity.setSort(10);

        entity.setChainId(6564057116594929664L);
        entity.setEnable(true);

        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TYPE,
                "addAppType", 1067246875800000002L, entity);

        appTypeDubboService.invoke(request);

        entity.setId(null);
        entity.setName("游戏");
        entity.setIntro("游戏");
        entity.setIcon("/app/icon/type/game.png");
        entity.setSort(20);

        request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TYPE,
                "addAppType", 1067246875800000002L, entity);

        appTypeDubboService.invoke(request);

        entity.setId(null);
        entity.setName("社交");
        entity.setIntro("社交");
        entity.setIcon("/app/icon/type/social.png");
        entity.setSort(30);

        request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TYPE,
                "addAppType", 1067246875800000002L, entity);

        appTypeDubboService.invoke(request);

        entity.setId(null);
        entity.setName("工具");
        entity.setIntro("工具");
        entity.setIcon("/app/icon/type/tool.png");
        entity.setSort(40);

        request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TYPE,
                "addAppType", 1067246875800000002L, entity);

        appTypeDubboService.invoke(request);

        entity.setId(null);
        entity.setName("资源");
        entity.setIntro("资源");
        entity.setIcon("/app/icon/type/resource.png");
        entity.setSort(50);

        request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TYPE,
                "addAppType", 1067246875800000002L, entity);

        appTypeDubboService.invoke(request);
    }
}
