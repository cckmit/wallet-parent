package org.wallet.dap.mq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author zengfucheng
 * @date 2018年7月19日
 */
public abstract class AbstractRMQSendingTemplate {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private DefaultMQProducer producer;
	
	private MessageQueueSelector messageQueueSelector = new SelectMessageQueueByHash();
	
	public SendResult syncSend(MessageWrapper message) {
		return syncSend(message, producer.getSendMsgTimeout());
	}
	
	public SendResult syncSend(MessageWrapper message, long timeout) {
		if(Objects.isNull(message.getTopic())) { throw new IllegalArgumentException("`topic` cannot be null"); }
		if (Objects.isNull(message) || Objects.isNull(message.getBody()) || message.getBody().length==0) {
	         throw new IllegalArgumentException("`message` and `message.body` cannot be null");
	    }
        try {
            long now = System.currentTimeMillis();
            SendResult sendResult = producer.send(message, timeout);
            long costTime = System.currentTimeMillis() - now;
            log.debug("send message cost: {} ms, msgId:{}", costTime, sendResult.getMsgId());
            return sendResult;
        } catch (Exception e) {
            throw new MQException(e);
        }
    }
		
	public SendResult syncSendOrderly(MessageWrapper message, Object hashKey) {
		return syncSendOrderly(message, hashKey, producer.getSendMsgTimeout());
	}
	
	public SendResult syncSendOrderly(MessageWrapper message, Object hashKey, long timeout) {
		if(Objects.isNull(message.getTopic())) { throw new IllegalArgumentException("`topic` cannot be null"); }
		if(Objects.isNull(hashKey)) { throw new IllegalArgumentException("`hashKey` cannot be null"); }
		if (Objects.isNull(message) || Objects.isNull(message.getBodyObject())) {
	         throw new IllegalArgumentException("`message` and `message.body` cannot be null");
	    }
	    try {
	        long now = System.currentTimeMillis();
	        SendResult sendResult = producer.send(message, messageQueueSelector, hashKey, timeout);
	        long costTime = System.currentTimeMillis() - now;
	        log.debug("send message cost: {} ms, msgId:{}", costTime, sendResult.getMsgId());
	        return sendResult;
	    } catch (Exception e) {
	        throw new MQException(e);
	    }
	}

	public DefaultMQProducer getProducer() {
		return producer;
	}

	public void setProducer(DefaultMQProducer producer) {
		this.producer = producer;
	}

	public MessageQueueSelector getMessageQueueSelector() {
		return messageQueueSelector;
	}

	public void setMessageQueueSelector(MessageQueueSelector messageQueueSelector) {
		this.messageQueueSelector = messageQueueSelector;
	}

}
