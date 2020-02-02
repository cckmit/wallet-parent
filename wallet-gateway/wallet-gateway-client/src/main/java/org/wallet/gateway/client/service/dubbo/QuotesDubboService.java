package org.wallet.gateway.client.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeansException;
import org.wallet.common.dto.block.req.QuotesReqDTO;
import org.wallet.common.dto.wallet.QuotesDTO;
import org.wallet.common.enums.wallet.QuotesSourceEnum;
import org.wallet.dap.common.dubbo.*;
import org.wallet.dap.common.utils.SpringContextUtils;
import org.wallet.gateway.client.service.QuotesService;

/**
 * @author zengfucheng
 **/
@Service(group = DubboServiceGroup.CLIENT_QUOTES, timeout = 30000)
@org.springframework.stereotype.Service
public class QuotesDubboService extends BaseDubboService implements IService {

    public ServiceResponse findQuotes(ServiceRequest request, ServiceResponse response) {
        QuotesReqDTO quotesReq = request.getParam();

        if(null == quotesReq){return Responses.missingParam("quotesReq"); }

        QuotesSourceEnum source = quotesReq.getSource();

        if(null == source){return Responses.missingParam("source"); }

        try{
            QuotesService quotesService = SpringContextUtils.getBean(source.getServiceName(), QuotesService.class);
            if(null != quotesService){
                response = quotesService.findQuotes(quotesReq);

                if(response.success()){
                    QuotesDTO quotes = response.getResult();
                    if(null == quotes){
                        return Responses.fail(ResponseCode.NOT_FOUND_DATA, String.format("未查询到交易所[%s:%s]交易对[%s]行情",
                                source.name(), source.getLabel(), JSON.toJSONString(quotesReq)));
                    }
                }

                return response;
            }
        } catch (BeansException e){
            String msg = String.format("不支持查询该交易所[%s]行情", source.getLabel());
            log.warn(msg);
            return Responses.fail(ResponseCode.NOT_FOUND_DATA, msg);
        }

        return response;
    }
}
