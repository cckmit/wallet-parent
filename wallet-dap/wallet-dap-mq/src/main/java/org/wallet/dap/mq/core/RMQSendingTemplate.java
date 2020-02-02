package org.wallet.dap.mq.core;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.wallet.dap.mq.AbstractRMQSendingTemplate;
import org.wallet.dap.mq.MQException;
import org.wallet.dap.mq.MessageWrapper;

import java.util.Objects;

/**
 * @author zengfucheng
 * @date 2018年7月18日
 */
public class RMQSendingTemplate extends AbstractRMQSendingTemplate {

	public SendResult syncSend(String topic, Object message) {
		return syncSend(topic, "", message);
    }
	
	public SendResult syncSend(String topic, String tags, Object message) {
		return syncSend(topic, tags, "", message);
    }
	
	public SendResult syncSend(String topic, String tags, String keys, Object message)  {
		return syncSend(topic, tags, keys, message, getProducer().getSendMsgTimeout());
	}
	
	public SendResult syncSend(String topic, Object message,long timeout) {
		return syncSend(topic, "", message, timeout);
    }
	
	public SendResult syncSend(String topic, String tags, Object message, long timeout) {
		return syncSend(topic, tags, "", message, timeout);
    }
	
	public SendResult syncSend(String topic, String tags, String keys, Object message, long timeout) {
		if (Objects.isNull(tags)) throw new IllegalArgumentException("`tags` cannot be null");
	    if (Objects.isNull(keys)) throw new IllegalArgumentException("`keys` cannot be null");
        return super.syncSend(new MessageWrapper(topic, tags, keys, message), timeout);
    }
	
	public SendResult syncSendOrderly(String topic, Object message, Object hashKey) {
	    return syncSendOrderly(topic, "", message, hashKey);
	}
	
	public SendResult syncSendOrderly(String topic, String tags, Object message,Object hashKey) {
	    return syncSendOrderly(topic, tags, "", message, hashKey);
	}
	
	public SendResult syncSendOrderly(String topic, String tags, String keys, Object message, Object hashKey) {
	    return syncSendOrderly(topic, tags, keys, message, hashKey, getProducer().getSendMsgTimeout());
	}
	
	public SendResult syncSendOrderly(String topic, Object message, Object hashKey, long timeout) {
	    return syncSendOrderly(topic, "", message, hashKey, timeout);
	}
	
	public SendResult syncSendOrderly(String topic, String tags, Object message, Object hashKey, long timeout) {
	    return syncSendOrderly(topic, tags, "", message,hashKey, timeout);
	}
	
	public SendResult syncSendOrderly(String topic, String tags, String keys, Object message, Object hashKey, long timeout) {
		if (Objects.isNull(tags)) throw new IllegalArgumentException("`tags` cannot be null");
	    if (Objects.isNull(keys)) throw new IllegalArgumentException("`keys` cannot be null");
	    return super.syncSendOrderly(new MessageWrapper(topic, tags, keys, message), hashKey, timeout);
	}
		
	public void asyncSend(String topic, Object message, SendCallback sendCallback) {
		asyncSend(topic, "", message, sendCallback);
	}
	
	public void asyncSend(String topic, String tags, Object message, SendCallback sendCallback) {
		asyncSend(topic, tags, "", message, sendCallback);
	}
	
	public void asyncSend(String topic, String tags, String keys, Object message, SendCallback sendCallback) {
		asyncSend(topic, tags, keys, message, sendCallback, getProducer().getSendMsgTimeout());
	}
	
	public void asyncSend(String topic, Object message, SendCallback sendCallback, long timeout) {
		asyncSend(topic, "", message, sendCallback, timeout);
	}
	
	public void asyncSend(String topic, String tags, Object message, SendCallback sendCallback, long timeout) {
		asyncSend(topic, tags, "", message, sendCallback, timeout);
	}
	
	public void asyncSend(String topic, String tags, String keys, Object message, SendCallback sendCallback, long timeout) {
		if (Objects.isNull(tags)) throw new IllegalArgumentException("`tags` cannot be null");
	    if (Objects.isNull(keys)) throw new IllegalArgumentException("`keys` cannot be null");
        asyncSend(new MessageWrapper(topic, tags, keys, message), sendCallback, timeout);
    }
	
	public void asyncSend(MessageWrapper message, SendCallback sendCallback, long timeout) {
		if(Objects.isNull(message.getTopic())) throw new IllegalArgumentException("`topic` cannot be null");
		if (Objects.isNull(message) || Objects.isNull(message.getBodyObject())) {
	         throw new IllegalArgumentException("`message` and `message.body` cannot be null");
	    }
        try {
            getProducer().send(message, sendCallback, timeout);
        } catch (Exception e) {
            throw new MQException(e);
        }
	}
	 
	public void asyncSendOrderly(String topic, Object message, Object hashKey, SendCallback sendCallback) {
		asyncSendOrderly(topic, "", message, hashKey, sendCallback);
	}
	
	public void asyncSendOrderly(String topic, String tags, Object message, Object hashKey, SendCallback sendCallback) {
		asyncSendOrderly(topic, tags, "", message,hashKey, sendCallback);
	}
	
	public void asyncSendOrderly(String topic, String tags, String keys, Object message, Object hashKey, SendCallback sendCallback) {
		asyncSendOrderly(topic, tags, keys , message, hashKey, sendCallback, getProducer().getSendMsgTimeout());
	}
	
	public void asyncSendOrderly(String topic, Object message, Object hashKey, SendCallback sendCallback, long timeout) {
		asyncSendOrderly(topic, "", message,hashKey, sendCallback, timeout);
	}
	
	public void asyncSendOrderly(String topic, String tags, Object message, Object hashKey, SendCallback sendCallback, long timeout) {
		asyncSendOrderly(topic, tags, "", message, hashKey, sendCallback, timeout);
	}
	
	public void asyncSendOrderly(String topic, String tags, String keys, Object message, Object hashKey, SendCallback sendCallback, long timeout) {
		if (Objects.isNull(tags)) throw new IllegalArgumentException("`tags` cannot be null");
	    if (Objects.isNull(keys)) throw new IllegalArgumentException("`keys` cannot be null");
		asyncSendOrderly(new MessageWrapper(topic,tags,keys, message), hashKey, sendCallback, timeout);
	}
	
	public void asyncSendOrderly(MessageWrapper message, Object hashKey, SendCallback sendCallback, long timeout) {
		if(Objects.isNull(message.getTopic())) throw new IllegalArgumentException("`topic` cannot be null");
		if(Objects.isNull(hashKey)) throw new IllegalArgumentException("`hashKey` cannot be null");
		if(Objects.isNull(message) || Objects.isNull(message.getBodyObject())) {
	         throw new IllegalArgumentException("`message` and `message.body` cannot be null");
	    }
        try {
            getProducer().send(message, getMessageQueueSelector(), hashKey, sendCallback, timeout);
        } catch (Exception e) {
            throw new MQException(e);
        }
	}
	
	public void sendOneWay(String topic, Object mmessage) {
		sendOneWay(topic, "", mmessage);
	}
	
	public void sendOneWay(String topic, String tags, Object mmessage) {
		sendOneWay(topic, tags, "", mmessage);
	}
	
	public void sendOneWay(String topic, String tags, String keys, Object mmessage) {
		if (Objects.isNull(tags)) throw new IllegalArgumentException("`tags` cannot be null");
	    if (Objects.isNull(keys)) throw new IllegalArgumentException("`keys` cannot be null");
		sendOneWay(new MessageWrapper(topic, tags, keys, mmessage));
	}
	
	public void sendOneWay(MessageWrapper message) {
		if(Objects.isNull(message.getTopic())) throw new IllegalArgumentException("`topic` cannot be null");
		if(Objects.isNull(message) || Objects.isNull(message.getBodyObject())) {
	         throw new IllegalArgumentException("`message` and `message.body` cannot be null");
	    }
        try {
            getProducer().sendOneway(message);
        } catch (Exception e) {
            throw new MQException(e);
        }
    }

	public void sendOneWayOrderly(String topic, Object message, Object hashKey) {
		sendOneWayOrderly(topic, "", message, hashKey);
	}
	
	public void sendOneWayOrderly(String topic, String tags, Object message,Object hashKey) {
		sendOneWayOrderly(topic, tags, "", message, hashKey);
	}
	
	public void sendOneWayOrderly(String topic, String tags, String keys, Object message,Object hashKey) {
		if (Objects.isNull(tags)) throw new IllegalArgumentException("`tags` cannot be null");
	    if (Objects.isNull(keys)) throw new IllegalArgumentException("`keys` cannot be null");
		sendOneWayOrderly(new MessageWrapper(topic, tags, keys, message), hashKey);
	}
	
	public void sendOneWayOrderly(MessageWrapper message, Object hashKey)  {
		if(Objects.isNull(message.getTopic())) throw new IllegalArgumentException("`topic` cannot be null");
		if(Objects.isNull(hashKey)) throw new IllegalArgumentException("`hashKey` cannot be null");
		if(Objects.isNull(message) || Objects.isNull(message.getBodyObject())) {
	         throw new IllegalArgumentException("`message` and `message.body` cannot be null");
	    }
        try {
            getProducer().sendOneway(message, getMessageQueueSelector(), hashKey);
        } catch (Exception e) {
            throw new MQException(e);
        }
	}    
}
