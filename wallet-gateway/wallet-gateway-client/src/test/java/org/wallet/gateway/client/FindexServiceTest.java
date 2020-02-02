package org.wallet.gateway.client;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.common.dto.block.req.QuotesReqDTO;
import org.wallet.common.enums.wallet.QuotesSourceEnum;
import org.wallet.gateway.client.service.impl.FindexService;
import org.wallet.gateway.client.service.impl.NewdexService;

/**
 * @author zengfucheng
 **/
public class FindexServiceTest extends SpringBootJUnitTest{

    @Autowired
    FindexService findexService;

    @Test
    public void test(){
        QuotesReqDTO quotesReqDTO = new QuotesReqDTO();
        quotesReqDTO.setSource(QuotesSourceEnum.FINDEX);
        quotesReqDTO.setContract("athenastoken");
        quotesReqDTO.setSymbol("ATHENA");
        quotesReqDTO.setAnchor("EOS");

        log.info(JSON.toJSONString(findexService.findQuotes(quotesReqDTO)));
    }
}
