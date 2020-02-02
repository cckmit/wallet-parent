package org.wallet.gateway.client.config;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.ApacheHttpClient;
import cn.jiguang.common.connection.IHttpClient;
import cn.jpush.api.JPushClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author zengfucheng
 **/
@Configuration
@EnableConfigurationProperties({JiGuangProperties.class, DFuseProperties.class, BlockProperties.class, QuotesProperties.class})
public class JiGuangConfig {

    @Autowired
    JiGuangProperties jiGuangProperties;

    @Bean
    JPushClient pushClient(){
        if(StringUtils.isEmpty(jiGuangProperties.getAppKey())){
            throw new IllegalArgumentException("请配置jiguang.app-key参数");
        }else if(StringUtils.isEmpty(jiGuangProperties.getMasterSecret())){
            throw new IllegalArgumentException("请配置jiguang.master-secret");
        }
        return new JPushClient(jiGuangProperties.getAppKey(), jiGuangProperties.getMasterSecret());
    }

    @Bean
    IHttpClient pushHttpClient(){
        String appKey = jiGuangProperties.getAppKey();
        String masterSecret = jiGuangProperties.getMasterSecret();
        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);
        ClientConfig clientConfig = ClientConfig.getInstance();
        clientConfig.setMaxRetryTimes(jiGuangProperties.getMaxRetryTimes());
        return new ApacheHttpClient(authCode, null, clientConfig);
    }
}
