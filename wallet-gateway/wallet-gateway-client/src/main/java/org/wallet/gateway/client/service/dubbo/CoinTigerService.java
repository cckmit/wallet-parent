package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.platform.CoinTigerConstants;
import org.wallet.common.entity.platform.cointiger.KLineObject;
import org.wallet.common.entity.platform.cointiger.MarketDetail;
import org.wallet.common.entity.platform.cointiger.RestResult;
import org.wallet.dap.common.dubbo.*;
import org.wallet.gateway.client.utils.httpclient.Browsers;
import org.wallet.gateway.client.utils.httpclient.HttpConnectionPool;

import java.util.Map;

/**
 * @author zengfucheng
 **/
@Service(group = DubboServiceGroup.CLIENT_COIN_TIGER)
@org.springframework.stereotype.Service
public class CoinTigerService extends BaseDubboService implements IService {
    private Logger logger = LoggerFactory.getLogger(CoinTigerService.class);

    public ServiceResponse getCoinTigerQuotes(ServiceRequest request, ServiceResponse response){
        try {
            String result = HttpConnectionPool.get("https://www.cointiger.com/exchange/api/public/market/detail", Browsers.SAFARI);
            Map<String, MarketDetail> marketDetailMap = JSON.parseObject(result, new TypeReference<Map<String, MarketDetail>>(){});
            response.setRespCode(ResponseCode.SUCCESS);
            response.setResultValue("result", marketDetailMap);
        } catch (Exception e) {
            String errorMsg = "获取CoinTiger 24小时内市场行情失败：" + e.getMessage();
            response.setRespCode(ResponseCode.FAIL);
            response.setRespMsg(e.getMessage());
            logger.error(errorMsg, e);
        }
        return response;
    }

    public ServiceResponse getKLineData(ServiceRequest request, ServiceResponse response){
        String productCoinName = request.getParamValue("productCoinName");
        String priceCoinName = request.getParamValue("priceCoinName");
        String period = request.getParamValue("period");
        Integer size = request.getParamValue("size");
        if(StringUtils.isEmpty(productCoinName)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[productCoinName]不存在！");
            return response;
        }
        if(StringUtils.isEmpty(priceCoinName)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[priceCoinName]不存在！");
            return response;
        }
        if(StringUtils.isEmpty(period)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[period]不存在！");
        }

        String symbol = productCoinName.toLowerCase() + priceCoinName.toLowerCase();

        try {
            String url = "https://api.cointiger.com/exchange/trading/api/market/history/kline";
            url += ("?symbol=" + symbol);
            url += ("&period=" + period);
            size = null == size ? 2000 : size;
            size = size < 1 ? 1 : size;
            url += ("&size=" + size);

            String resultString = HttpConnectionPool.get(url, Browsers.SAFARI);
            RestResult<KLineObject> restResult = JSON.parseObject(resultString, new TypeReference<RestResult<KLineObject>>(){});
            if(CoinTigerConstants.CODE_SUCCESS.equals(restResult.getCode())){
                response.setRespCode(ResponseCode.SUCCESS);
                response.setResultValue("result", restResult.getData().getkLineDetails());
            }else{
                response.setRespCode(ResponseCode.FAIL);
                response.setRespMsg(restResult.getMsg());
            }
        } catch (Exception e) {
            String errorMsg = "获取CoinTiger K线数据失败：" + e.getMessage();
            response.setRespCode(ResponseCode.FAIL);
            response.setRespMsg(e.getMessage());
            logger.error(errorMsg, e);
        }
        return response;
    }
}
