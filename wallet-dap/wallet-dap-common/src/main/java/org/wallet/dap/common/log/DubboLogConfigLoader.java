package org.wallet.dap.common.log;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 加载Dubbo服务调用日志格式化配置信息
 * @author zengfucheng
 **/
@Component
public class DubboLogConfigLoader implements InitializingBean {
    @Autowired
    private DubboLogProperties dubboLogProperties;

    @Override
    public void afterPropertiesSet() {
        if(null != dubboLogProperties && !CollectionUtils.isEmpty(dubboLogProperties.getLogConfigList())){
            ServiceLogFormatHandler.getInstance().addLogConfig(dubboLogProperties.getLogConfigList());
        }
    }
}
