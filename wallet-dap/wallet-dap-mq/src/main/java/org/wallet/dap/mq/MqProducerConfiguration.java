/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wallet.dap.mq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@ComponentScan(basePackages = "org.wallet.dap.mq")
@EnableConfigurationProperties(MqProperties.class)
public class MqProducerConfiguration {
    private Logger log = LoggerFactory.getLogger(MqProducerConfiguration.class);

    @Autowired
    MqProperties mqProperties;
   
    @Bean(initMethod="start", destroyMethod="shutdown")
    public DefaultMQProducer initMqProducer() {
        log.info("mq.nameServer: {}", mqProperties.getNameServer());
        log.info("mq.instanceName: {}", mqProperties.getInstanceName());

    	if(!mqProperties.getProducer().isEnable()) {
            return null;
        }

    	String nameServer = mqProperties.getNameServer();
        if(StringUtils.isEmpty(nameServer)){
            throw new IllegalArgumentException("[mq.nameServer] must not be null");
        }
        MqProperties.Producer producerConfig = mqProperties.getProducer();
        String groupName = producerConfig.getGroup();
        if(StringUtils.isEmpty(groupName)){
            throw new IllegalArgumentException("[mq.producer.group] must not be null");
        }

        log.info("mq.producer.group: {}", groupName);

        DefaultMQProducer producer = new DefaultMQProducer(groupName);
        producer.setNamesrvAddr(nameServer);
        producer.setSendMsgTimeout(producerConfig.getSendMsgTimeout());
        producer.setRetryTimesWhenSendFailed(producerConfig.getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(producerConfig.getRetryTimesWhenSendAsyncFailed());
        producer.setMaxMessageSize(producerConfig.getMaxMessageSize());
        producer.setCompressMsgBodyOverHowmuch(producerConfig.getCompressMsgBodyOverHowMuch());
        producer.setRetryAnotherBrokerWhenNotStoreOK(producerConfig.isRetryAnotherBrokerWhenNotStoreOk());
        String instanceName = mqProperties.getInstanceName();
        if(!StringUtils.isEmpty(instanceName)) {
            producer.setClientIP(buildClientIP(instanceName));
        }
        return producer;
    }
    
    private String buildClientIP(String instanceName) {
    	return instanceName+="#"+(int)(Math.random()*100);
    } 
}
