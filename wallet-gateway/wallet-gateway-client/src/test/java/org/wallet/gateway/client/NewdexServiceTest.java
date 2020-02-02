package org.wallet.gateway.client;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.common.dto.block.req.QuotesReqDTO;
import org.wallet.dap.common.dubbo.DubboServiceGroup;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.gateway.client.service.impl.NewdexService;

/**
 * @author zengfucheng
 **/
public class NewdexServiceTest extends SpringBootJUnitTest{

    @Autowired
    NewdexService newdexService;

    @Test
    public void test(){
        QuotesReqDTO quotesReqDTO = new QuotesReqDTO();
        quotesReqDTO.setContract("athenastoken");
        quotesReqDTO.setSymbol("ATHENA");
        quotesReqDTO.setAnchor("EOS");

        log.info(JSON.toJSONString(newdexService.findQuotes(quotesReqDTO)));
    }
}
