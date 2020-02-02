package org.wallet.dap.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zengfucheng
 **/
@Data
@Component
@ConfigurationProperties(prefix = "dubbo")
public class DubboProperties {
    private String address;
    private String group;
    private Integer port;
}
