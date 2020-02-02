package org.wallet.web.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author zengfucheng
 **/
@Data
@ConfigurationProperties(prefix = "admin")
public class AdminProperties {
    /** Token Timeout */
    private Duration tokenTimeout;

}
