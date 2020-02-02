package org.wallet.gateway.client;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.common.dto.block.req.CreateAccountReqDTO;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.gateway.client.service.dubbo.DFuseService;
import org.wallet.gateway.client.service.impl.EOSBlockService;

/**
 * @author zengfucheng
 **/
public class EOSServiceTest extends SpringBootJUnitTest{

    @Autowired
    EOSBlockService eosBlockService;

    @Test
    public void test(){
        CreateAccountReqDTO dto = new CreateAccountReqDTO();

        dto.setName("snzke6666666");
        dto.setPublicKey("sdfsdf");

        eosBlockService.createAccount(dto);
    }
}
