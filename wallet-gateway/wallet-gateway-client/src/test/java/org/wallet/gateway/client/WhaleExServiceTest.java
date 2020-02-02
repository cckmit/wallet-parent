package org.wallet.gateway.client;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.common.dto.block.req.QuotesReqDTO;
import org.wallet.common.enums.wallet.QuotesSourceEnum;
import org.wallet.gateway.client.service.impl.WhaleExService;

/**
 * @author zengfucheng
 **/
public class WhaleExServiceTest extends SpringBootJUnitTest{

    @Autowired
    WhaleExService whaleExService;

    @Test
    public void test(){
        QuotesReqDTO quotesReqDTO = new QuotesReqDTO();
        quotesReqDTO.setSource(QuotesSourceEnum.WhaleEx);
        quotesReqDTO.setSymbol("ATHENA");
        quotesReqDTO.setAnchor("EOS");

        log.info(JSON.toJSONString(whaleExService.findQuotes(quotesReqDTO)));
    }
}
