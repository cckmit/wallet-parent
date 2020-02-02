package org.wallet.service.common.config.properties.druid;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zengfucheng
 **/
@Data
@ConfigurationProperties(prefix = "spring.datasource.stat-view-servlet")
public class StatViewServletProperties {
    private String urlPattern = "/druid/*";
    private String allow = "127.0.0.1";
    private String deny = "";
    private String loginUsername = "druid";
    private String loginPassword = "Druid#18Wallet";
    private String resetEnable = "false";
}
