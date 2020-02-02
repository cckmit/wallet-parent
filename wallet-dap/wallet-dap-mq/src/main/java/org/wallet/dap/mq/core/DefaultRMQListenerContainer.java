package org.wallet.dap.mq.core;

import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wallet.dap.mq.AbstractRMQListenerContainer;

import java.util.List;

/**
 * @author zengfucheng
 * @date 2018年7月18日
 */
public class DefaultRMQListenerContainer extends AbstractRMQListenerContainer {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	protected MessageListenerOrderly genMessageListenerOrderly() {
		return new MessageListenerOrderly(){
			@Override
			public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs,
					ConsumeOrderlyContext context) {
				for (MessageExt messageExt : msgs) {
	                log.debug("received msg: {}", messageExt);
	                try {
	                    long now = System.currentTimeMillis();
	                    getRocketMQListener().onMessage(doConvertMessage(messageExt));
	                    long costTime = System.currentTimeMillis() - now;
	                    log.info("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
	                } catch (Exception e) {
	                    log.warn("consume message failed. messageExt:{}", messageExt, e);
	                    context.setSuspendCurrentQueueTimeMillis(getSuspendCurrentQueueTimeMillis());
	                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
	                }
	            }
	            return ConsumeOrderlyStatus.SUCCESS;
			}
			
		};
	}

	@Override
	protected MessageListenerConcurrently genMessageListenerConcurrently() {
		return new MessageListenerConcurrently(){
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(
					List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				for (MessageExt messageExt : msgs) {
	                log.debug("received msg: {}", messageExt);
	                try {
	                    long now = System.currentTimeMillis();
	                    getRocketMQListener().onMessage(doConvertMessage(messageExt));
	                    long costTime = System.currentTimeMillis() - now;
	                    log.debug("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
	                } catch (Exception e) {
	                    log.warn("consume message failed. messageExt:{}", messageExt, e);
	                    context.setDelayLevelWhenNextConsume(getDelayLevelWhenNextConsume());
	                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
	                }
	            }
	            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		};
	}

}
