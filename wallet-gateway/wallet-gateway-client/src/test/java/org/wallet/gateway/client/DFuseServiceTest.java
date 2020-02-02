package org.wallet.gateway.client;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.gateway.client.service.dubbo.DFuseService;

/**
 * @author zengfucheng
 **/
public class DFuseServiceTest extends SpringBootJUnitTest{

    @Autowired
    DFuseService dFuseService;

    @Test
    public void test(){
        dFuseService.queryTrans(ServiceRequest.newInstance("wallet-gateway-client", DubboServiceGroup.CLIENT_DFUSE,
                "queryTrans", null, "athenastoken"), ServiceResponse.newInstance());
    }
}
