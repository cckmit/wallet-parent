package org.wallet.service.application;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.common.entity.application.AppTagEntity;
import org.wallet.common.entity.application.AppTypeEntity;
import org.wallet.common.enums.application.AppTagEnum;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.service.application.service.AppTypeService;

public class AppTagTest extends SpringBootJUnitTest{

    @Reference(group = DubboServiceGroup.SERVICE_APP_TAG)
    private IService appTagDubboService;

    @Test
    public void addAppTag(){
        AppTagEntity entity = new AppTagEntity();

        entity.setChainId(6564057116594929664L);
        entity.setAppId(6564352894337024000L);
        entity.setTag(AppTagEnum.HOT);
        entity.setSort(10);

        appTagDubboService.invoke(ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TAG,
                "addAppTag", 1067246875800000001L, entity));

        entity.setChainId(6564057116594929664L);
        entity.setAppId(6564352896056688640L);
        entity.setTag(AppTagEnum.HOT);
        entity.setSort(20);

        appTagDubboService.invoke(ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TAG,
                "addAppTag", 1067246875800000001L, entity));
    }

    @Test
    public void deleteAppTag(){
        appTagDubboService.invoke(ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_TAG,
                "deleteAppTag", 1067246875800000001L, 6564420409478086656L));
    }
}
