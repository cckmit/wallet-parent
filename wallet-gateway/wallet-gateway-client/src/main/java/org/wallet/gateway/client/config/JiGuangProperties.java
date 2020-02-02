package org.wallet.gateway.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zengfucheng
 **/
@Data
@ConfigurationProperties(prefix = "jiguang")
public class JiGuangProperties {
    private String appKey;
    private String masterSecret;
    /**
     * 失败重试次数
     */
    private Integer maxRetryTimes = 3;
}
