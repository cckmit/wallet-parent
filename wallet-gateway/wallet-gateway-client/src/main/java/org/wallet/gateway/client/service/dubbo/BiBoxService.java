package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.wallet.common.entity.platform.bibox.BiBoxQuotes;
import org.wallet.common.entity.platform.bibox.BiBoxResult;
import org.wallet.common.entity.platform.bibox.KLineData;
import org.wallet.dap.common.dubbo.*;
import org.wallet.gateway.client.utils.httpclient.HttpConnectionPool;

import java.util.List;

/**
 * @author zengfucheng
 **/
@Service(group = DubboServiceGroup.CLIENT_BIBOX)
@org.springframework.stereotype.Service
public class BiBoxService extends BaseDubboService implements IService {
    private Logger logger = LoggerFactory.getLogger(BiBoxService.class);

    /**
     * 获取币种行情
     * @return
     */
    public ServiceResponse getBiBoxQuotes(ServiceRequest request, ServiceResponse response){
        String productCoinName = request.getParamValue("productCoinName");
        String priceCoinName = request.getParamValue("priceCoinName");

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
        String symbol = productCoinName.toUpperCase() + "_" + priceCoinName.toUpperCase();
        try {
            String result = HttpConnectionPool.get("https://api.bibox.com/v1/mdata?cmd=market&pair=" + symbol);
            BiBoxResult<BiBoxQuotes> biBoxResult = JSON.parseObject(result, new TypeReference<BiBoxResult<BiBoxQuotes>>(){});
            response.setRespCode(ResponseCode.SUCCESS);
            response.setResultValue("result", biBoxResult);
        } catch (Exception e) {
            String errorMsg = "获取BiBox 24H行情失败：" + e.getMessage();
            response.setRespCode(ResponseCode.FAIL);
            response.setRespMsg(e.getMessage());
            logger.error(errorMsg, e);
        }
        return response;
    }

    /**
     * 获取币种行情
     * @return
     */
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
        if(null == period){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[period]不存在！");
            return response;
        }
        String symbol = productCoinName.toUpperCase() + "_" + priceCoinName.toUpperCase();
        String url = "https://api.bibox.com/v1/mdata?cmd=kline";
        url += "&pair=" + symbol;
        url += "&period=" + period;
        if(null != size && size > 0){
            url += "&size=" + size;
        }
        try {
            String result = HttpConnectionPool.get(url);
            BiBoxResult<List<KLineData>> biBoxResult = JSON.parseObject(result, new TypeReference<BiBoxResult<List<KLineData>>>(){});
            response.setRespCode(ResponseCode.SUCCESS);
            response.setResultValue("result", biBoxResult);
        } catch (Exception e) {
            String errorMsg = "获取BiBox K线行情失败：" + e.getMessage();
            response.setRespCode(ResponseCode.FAIL);
            response.setRespMsg(e.getMessage());
            logger.error(errorMsg, e);
        }
        return response;
    }
}
