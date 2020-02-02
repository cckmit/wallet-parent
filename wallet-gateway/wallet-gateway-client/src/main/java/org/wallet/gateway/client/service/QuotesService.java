package org.wallet.gateway.client.service;

import org.springframework.util.CollectionUtils;
import org.wallet.common.dto.block.req.QuotesReqDTO;
import org.wallet.common.enums.wallet.QuotesSourceEnum;
import org.wallet.dap.common.dubbo.ServiceResponse;
import org.wallet.gateway.client.config.QuotesProperties;

/**
 * @author zengfucheng
 **/
public interface QuotesService {
    /**
     * 查询行情
     * @param req 行情请求
     * @return 服务响应
     */
    ServiceResponse findQuotes(QuotesReqDTO req);

    /**
     * 根据配置获取域名
     * @param properties 行情配置
     * @param source 行情来源
     * @return
     */
    default String getHost(QuotesProperties properties, QuotesSourceEnum source){
        if(null == properties){ return null; }
        if(null == source){ return null; }
        if(CollectionUtils.isEmpty(properties.getHosts())){ return null; }
        return properties.getHosts().get(source);
    }
}
