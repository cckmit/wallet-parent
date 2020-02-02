package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.wallet.common.entity.platform.zgtop.ZGTopQuotes;
import org.wallet.common.entity.platform.zgtop.ZGTopResult;
import org.wallet.dap.common.dubbo.*;
import org.wallet.gateway.client.utils.httpclient.HttpConnectionPool;

/**
 * @author zengfucheng
 **/
@Service(group = DubboServiceGroup.CLIENT_ZG_TOP)
@org.springframework.stereotype.Service
public class ZGTopService extends BaseDubboService implements IService {
    private Logger logger = LoggerFactory.getLogger(ZGTopService.class);

    /**
     * 获取币种行情
     * @return
     */
    public ServiceResponse getZGTopQuotes(ServiceRequest request, ServiceResponse response){
        Object symbolId = request.getParamValue("symbolId");

        if(StringUtils.isEmpty(symbolId)){
            response.setRespCode(ResponseCode.ILLEGAL_PARAM);
            response.setRespMsg("参数[symbolId]不存在！");
            return response;
        }
        try {
            String body = HttpConnectionPool.get("https://www.zgtop.io/API/api/v1/ticker?symbol=" + symbolId);
            ZGTopResult<ZGTopQuotes> result = JSON.parseObject(body, new TypeReference<ZGTopResult<ZGTopQuotes>>(){});
            response.setRespCode(ResponseCode.SUCCESS);
            response.setResultValue("result", result);
        } catch (Exception e) {
            String errorMsg = "获取ZG.TOP 24H行情失败：" + e.getMessage();
            response.setRespCode(ResponseCode.FAIL);
            response.setRespMsg(e.getMessage());
            logger.error(errorMsg, e);
        }
        return response;
    }

}
