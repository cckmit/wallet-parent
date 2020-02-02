package org.wallet.gateway.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.CoinConstants;
import org.wallet.common.constants.QuotesConstants;
import org.wallet.common.dto.block.req.QuotesReqDTO;
import org.wallet.common.dto.block.res.findex.FindexQuotesResDTO;
import org.wallet.common.dto.block.res.findex.FindexResultDTO;
import org.wallet.common.dto.wallet.QuotesDTO;
import org.wallet.dap.common.dubbo.ResponseCode;
import org.wallet.dap.common.dubbo.Responses;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.gateway.client.config.QuotesProperties;
import org.wallet.gateway.client.service.PriceService;
import org.wallet.gateway.client.service.QuotesService;
import org.wallet.gateway.client.utils.httpclient.HttpConnectionPool;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengfucheng
 **/
@Slf4j
@Service(QuotesConstants.FINDEX)
public class FindexService implements QuotesService {
    private Logger logger = LoggerFactory.getLogger(FindexService.class);

    @Autowired
    private QuotesProperties quotesProperties;

    @Autowired
    private PriceService priceService;

    @Override
    public ServiceResponse findQuotes(QuotesReqDTO req) {
        String host = getHost(quotesProperties, req.getSource());
        String contract = req.getContract();
        String symbol = req.getSymbol();
        String anchor = req.getAnchor();

        if(StringUtils.isEmpty(contract)){
            return Responses.missingParam("contract");
        }else if(StringUtils.isEmpty(anchor)){
            return Responses.missingParam("anchor");
        }else if(StringUtils.isEmpty(symbol)){
            return Responses.missingParam("symbol");
        }

        String symbolStr = contract.toLowerCase() + "_" + symbol.toUpperCase() + "-eosio.token_" + anchor.toLowerCase();

        BigDecimal usdRate = priceService.getCoinUSDPrice(anchor);

        if(null == usdRate){
            return Responses.fail(ResponseCode.FAIL, String.format("不支持该主链币种：%s", anchor));
        }

        String url = host + "/v1/ticker?symbol=" + symbolStr;

        try {
            String resultString = HttpConnectionPool.get(url);
            FindexResultDTO<FindexQuotesResDTO> resultDTO = JSON.parseObject(resultString, new TypeReference<FindexResultDTO<FindexQuotesResDTO>>(){});
            if(null == resultDTO){
                return Responses.fail(ResponseCode.FAIL, String.format("获取Findex交易对[%s]行情失败：无返回结果", symbolStr));
            }else if(resultDTO.success()){
                FindexQuotesResDTO quotesResDTO = resultDTO.getData();

                BigDecimal last = quotesResDTO.getPrice();
                BigDecimal change = quotesResDTO.getChange();

                QuotesDTO quotesDTO = new QuotesDTO();
                quotesDTO.setSymbol(symbol);
                quotesDTO.setAnchor(anchor);
                quotesDTO.setLast(last);

                quotesDTO.setChange(change);

                if(null != change && change.compareTo(BigDecimal.ZERO) != 0){
                    BigDecimal percent = change.divide(last, CoinConstants.SCALE_PERCENT, BigDecimal.ROUND_DOWN);
                    quotesDTO.setPercent(percent);
                }

                BigDecimal lastUSD = last.multiply(usdRate);

                quotesDTO.setLastUSD(lastUSD.setScale(CoinConstants.SCALE_PRICE, BigDecimal.ROUND_DOWN));

                quotesDTO.setUpdated(new Date());

                return Responses.success(quotesDTO);
            }else{
                return Responses.fail(ResponseCode.FAIL, String.format("获取Findex交易对[%s]行情失败：[code:%s][msg:%s]",
                        symbolStr, resultDTO.getCode(), resultDTO.getMsg()));
            }
        } catch (IOException e) {
            String errorMsg = String.format("获取Findex交易对[%s]行情失败：%s", symbolStr, e.getMessage());
            log.error(errorMsg, e);
            return Responses.fail(ResponseCode.FAIL, errorMsg);
        }
    }
}
