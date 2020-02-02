package org.wallet.dap.mq.core;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author zengfucheng
 * @date 2018年7月18日
 */
@Configuration
@Order
public class MessageSendingTemplateConfiguration {

	@Bean
    public RMQSendingTemplate RMQSendingTemplate(DefaultMQProducer mqProducer) {
        RMQSendingTemplate rocketMQTemplate = new RMQSendingTemplate();
        rocketMQTemplate.setProducer(mqProducer);
        return rocketMQTemplate;
    }
}
