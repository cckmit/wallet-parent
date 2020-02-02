package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.platform.BitZConstants;
import org.wallet.common.entity.platform.bitz.BitZKLine;
import org.wallet.common.entity.platform.bitz.BitZQuotes;
import org.wallet.common.entity.platform.bitz.BitZResult;
import org.wallet.dap.common.dubbo.*;
import org.wallet.gateway.client.utils.httpclient.Browsers;
import org.wallet.gateway.client.utils.httpclient.HttpConnectionPool;

/**
 * @author zengfucheng
 **/
@Service(group = DubboServiceGroup.CLIENT_BIT_Z, timeout = 10000)
@org.springframework.stereotype.Service
public class BitZService extends BaseDubboService implements IService {
    private Logger logger = LoggerFactory.getLogger(BitZService.class);

    public ServiceResponse getBitZQuotes(ServiceRequest request, ServiceResponse response){
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
        String symbol = productCoinName.toLowerCase() + "_" + priceCoinName.toLowerCase();
        try {
            String resultString = HttpConnectionPool.get("https://apiv2.bitz.com/Market/ticker?symbol=" + symbol, Browsers.SAFARI);
            BitZResult<BitZQuotes> result = JSON.parseObject(resultString, new TypeReference<BitZResult<BitZQuotes>>(){});
            response.setRespCode(ResponseCode.SUCCESS);
            response.setResultValue("result", result);
        } catch (Exception e) {
            String errorMsg = "获取Bit-Z 24小时内市场行情失败：" + e.getMessage();
            response.setRespCode(ResponseCode.FAIL);
            response.setRespMsg(e.getMessage());
            logger.error(errorMsg, e);
        }
        return response;
    }

    public ServiceResponse getKLineData(ServiceRequest request, ServiceResponse response){
        String productCoinName = request.getParamValue("productCoinName");
        String priceCoinName = request.getParamValue("priceCoinName");
        Integer size = request.getParamValue("size");
        Long time = request.getParamValue("time");
        String resolution = request.getParamValue("resolution");

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
        if(StringUtils.isEmpty(resolution)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[resolution]不存在！");
            return response;
        }
        String url = "https://apiv2.bitz.com/Market/kline";
        String symbol = productCoinName.toLowerCase() + "_" + priceCoinName.toLowerCase();
        url += "?symbol=" + symbol;
        url += "&resolution=" + resolution;
        if(null != size && size > 0){
            url += "&size=" + size;
        }
        if(null != time && time > 0){
            url += "&to=" + time;
        }
        try {
            String resultString = HttpConnectionPool.get(url, Browsers.SAFARI);
            BitZResult<BitZKLine> result = JSON.parseObject(resultString, new TypeReference<BitZResult<BitZKLine>>(){});
            if(result.getStatus().equals(BitZConstants.STATUS_SUCCESS)){
                response.setRespCode(ResponseCode.SUCCESS);
                response.setResultValue("result", result.getData());
            }else{
                response.setRespCode(ResponseCode.FAIL);
                response.setRespMsg(result.getMsg());
            }
        } catch (Exception e) {
            String errorMsg = "获取Bit-Z K线行情失败：" + e.getMessage();
            response.setRespCode(ResponseCode.FAIL);
            response.setRespMsg(e.getMessage());
            logger.error(errorMsg, e);
        }
        return response;
    }
}
