package org.wallet.service.batch;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wallet.dap.mq.RocketMQListener;
import org.wallet.dap.mq.annotation.MessageListener;

/**
 * @author zengfucheng
 **/
@Service
@MessageListener(consumerGroup = RocketMQTest.TEST_CONSUMER_GROUP, topic = RocketMQTest.TEST_TOPIC, 
	consumeThreadMax = 1, consumeMessageBatchMaxSize=10)
public class MyListener implements RocketMQListener<Car> {
    private Logger logger = LoggerFactory.getLogger(MyListener.class);

    @Override
    public void onMessage(Car message) {
        logger.info("MyListener 消费消息：{}", JSON.toJSONString(message));
    }
}