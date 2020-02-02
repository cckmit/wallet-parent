package org.wallet.gateway.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author zengfucheng
 **/
@Data
@ConfigurationProperties(prefix = "block")
public class BlockProperties {
    private Map<String, String> coinHost;

}
