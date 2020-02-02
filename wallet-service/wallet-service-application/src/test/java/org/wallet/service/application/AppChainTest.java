package org.wallet.service.application;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.wallet.common.constants.field.AppChainField;
import org.wallet.common.entity.application.AppChainEntity;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.IService;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.dap.common.bind.search.Searchs;
import org.wallet.dap.common.bind.filter.SearchFilters;

public class AppChainTest extends SpringBootJUnitTest{

    @Reference(group = DubboServiceGroup.SERVICE_APP_CHAIN)
    private IService appChainDubboService;

    @Test
    public void getAppChainById() {
        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_CHAIN,
                "getAppChainById", null, 6564057116594929664L);

        ServiceResponse response = appChainDubboService.invoke(request);
        logger.info(JSON.toJSONString(response));
    }

    @Test
    public void findAllAppChain() {
        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_CHAIN, "findAllAppChain");
        ServiceResponse response = appChainDubboService.invoke(request);
        logger.info(JSON.toJSONString(response));
    }

    @Test
    public void findAppChain() {
        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_CHAIN,
                "findAppChain", 1067246875800000002L, Searchs.of(SearchFilters.eq(AppChainField.NAME, "EOS")));
        ServiceResponse response = appChainDubboService.invoke(request);
        logger.info(JSON.toJSONString(response));
    }

    @Test
    public void updateAppChain() {
        AppChainEntity entity = new AppChainEntity();
        entity.setId(6564057116594929664L);
        entity.setName("EOS1");

        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_CHAIN,
                "updateAppChain", 1067246875800000002L, entity);

        appChainDubboService.invoke(request);
    }

    @Test
    public void addAppChain(){
        AppChainEntity entity = new AppChainEntity();

        entity.setName("EOS1");
        entity.setIntro("Enterprise Operation System");
        entity.setIcon("/app/icon/chain/EOS.png");
        entity.setSort(30);
        entity.setEnable(true);

        ServiceRequest request = ServiceRequest.newInstance(appName, DubboServiceGroup.SERVICE_APP_CHAIN,
                "addAppChain", 1067246875800000002L, entity);

        appChainDubboService.invoke(request);
    }
}
