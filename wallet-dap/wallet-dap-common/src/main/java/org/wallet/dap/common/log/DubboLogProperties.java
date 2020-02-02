package org.wallet.dap.common.log;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = DubboLogProperties.PROPERTIES_PREFIX)
public class DubboLogProperties {
    public static final String PROPERTIES_PREFIX = "dubbo";
    private List<ServiceLogConfig> logConfigList;

    public List<ServiceLogConfig> getLogConfigList() {
        return logConfigList;
    }

    public void setLogConfigList(List<ServiceLogConfig> logConfigList) {
        this.logConfigList = logConfigList;
    }
}