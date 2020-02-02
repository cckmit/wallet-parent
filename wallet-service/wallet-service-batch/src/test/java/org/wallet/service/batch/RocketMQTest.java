package org.wallet.service.batch;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet.dap.mq.core.RMQSendingTemplate;

/**
 * @author zengfucheng
 **/
public class RocketMQTest extends SpringBootJUnitTest {

    public static final String TEST_CONSUMER_GROUP = "DemoTestConsumer";

    public static final String TEST_TOPIC = "TOPIC_DEMO_TEST";

    @Autowired
    RMQSendingTemplate rocketMQTemplate;

    @Test
    public void testMQ() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
//    	for(int i=0;i<100000;i++) {
    		rocketMQTemplate.syncSend(TEST_TOPIC, "TagA", new Car("奥迪", 25));
//    	}
//        SendResult sendResult = rocketMQTemplate.syncSend(TEST_TOPIC, "TagA", new Car("奥迪", 25));

//        Assert.assertEquals(sendResult.getSendStatus(), SendStatus.SEND_OK);
        
        Thread.sleep(10000);
    }
}
