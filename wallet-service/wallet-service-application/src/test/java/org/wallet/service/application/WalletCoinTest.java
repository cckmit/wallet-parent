package org.wallet.service.application;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.common.dto.wallet.req.FindCoinInfoReqDTO;
import org.wallet.service.application.service.WalletCoinService;

import java.util.Arrays;

public class WalletCoinTest extends SpringBootJUnitTest{

    @Autowired
    private WalletCoinService walletCoinService;

    @Test
    public void findCoinBaseInfo() {
        FindCoinInfoReqDTO findCoinInfoReqDTO = new FindCoinInfoReqDTO();
        findCoinInfoReqDTO.setChainId(6564057116594929664L);
        findCoinInfoReqDTO.setCoinNames(Arrays.asList("EOS222", "EOS3"));
        logger.info(JSON.toJSONString(walletCoinService.findCoinBaseInfo(findCoinInfoReqDTO)));
    }
}
