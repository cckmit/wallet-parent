package org.wallet.dap.common.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wallet.dap.common.config.properties.DubboProperties;

/**
 * Dubbo 自动装配
 * @author zengfucheng
 **/
@Configuration
@EnableDubbo(multipleConfig = true)
@DubboComponentScan("org.wallet")
@EnableConfigurationProperties(DubboProperties.class)
public class DubboConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private DubboProperties dubboProperties;

    @Bean
    @ConditionalOnProperty("dubbo.port")
    public ProtocolConfig protocolConfig(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(dubboProperties.getPort());
        protocolConfig.setThreads(20);
        protocolConfig.setIothreads(3);
        return protocolConfig;
    }

    @Bean
    @ConditionalOnProperty("dubbo.port")
    public ProviderConfig providerConfig(){
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setGroup(dubboProperties.getGroup());
        providerConfig.setDelay(-1);
        providerConfig.setRetries(0);
        providerConfig.setTimeout(3000);
        providerConfig.setThreads(20);
        providerConfig.setConnections(10);
        providerConfig.setFilter("providerLogFilter");
        return providerConfig;
    }

    @Bean
    @ConditionalOnBean(ApplicationConfig.class)
    public ConsumerConfig consumerConfig(){
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setTimeout(3000);
        consumerConfig.setRetries(0);
        consumerConfig.setConnections(10);
        consumerConfig.setFilter("consumerLogFilter");
        return consumerConfig;
    }
}
