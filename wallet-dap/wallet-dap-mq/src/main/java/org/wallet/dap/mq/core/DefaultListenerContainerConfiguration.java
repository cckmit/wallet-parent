package org.wallet.dap.mq.core;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.wallet.dap.mq.AbstractListenerContainerConfiguration;
import org.wallet.dap.mq.RMQListenerContainer;
import org.wallet.dap.mq.annotation.MessageListener;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zengfucheng
 * @date 2018年7月18日
 */
@Component
public class DefaultListenerContainerConfiguration extends AbstractListenerContainerConfiguration {
	
	private AtomicInteger counter = new AtomicInteger(0);

	@Override
	protected RMQListenerContainer registerMessageListenerContainer(String beanName, Object bean, Class<?> clazz) {
        MessageListener annotation = clazz.getAnnotation(MessageListener.class);
        BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder.rootBeanDefinition(DefaultRMQListenerContainer.class);
        beanBuilder.addPropertyValue(DefaultRMQListenerContainerConstants.PROP_NAMESERVER, getMqProperties().getNameServer());
        beanBuilder.addPropertyValue(DefaultRMQListenerContainerConstants.PROP_TOPIC, getEnvironment().resolvePlaceholders(annotation.topic()));
        beanBuilder.addPropertyValue(DefaultRMQListenerContainerConstants.PROP_CONSUMER_GROUP, getEnvironment().resolvePlaceholders(annotation.consumerGroup()));
        beanBuilder.addPropertyValue(DefaultRMQListenerContainerConstants.PROP_CONSUME_MODE, annotation.consumeMode());
        beanBuilder.addPropertyValue(DefaultRMQListenerContainerConstants.PROP_CONSUME_THREAD_MAX, annotation.consumeThreadMax());
        beanBuilder.addPropertyValue(DefaultRMQListenerContainerConstants.PROP_MESSAGE_MODEL, annotation.messageModel());
        beanBuilder.addPropertyValue(DefaultRMQListenerContainerConstants.PROP_SELECTOR_EXPRESS, getEnvironment().resolvePlaceholders(annotation.selectorExpress()));
        beanBuilder.addPropertyValue(DefaultRMQListenerContainerConstants.PROP_CONSUME_MESSAGE_BATCH_MAXSIZE, annotation.consumeMessageBatchMaxSize());
        beanBuilder.addPropertyValue(DefaultRMQListenerContainerConstants.PROP_MAX_RE_CONSUME_TIMES, annotation.maxReconsumeTimes());
        beanBuilder.addPropertyValue(DefaultRMQListenerContainerConstants.PROP_SELECTOR_TYPE, annotation.selectorType());
        beanBuilder.addPropertyValue(DefaultRMQListenerContainerConstants.PROP_ROCKET_MQ_LISTENER,  bean);
        beanBuilder.setDestroyMethodName(DefaultRMQListenerContainerConstants.METHOD_DESTROY);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) getApplicationContext().getBeanFactory();
		String containerBeanName = String.format("%s_%s",DefaultRMQListenerContainer.class.getName(), counter.getAndIncrement());
		beanFactory.registerBeanDefinition(containerBeanName, beanBuilder.getBeanDefinition());
        return beanFactory.getBean(containerBeanName, DefaultRMQListenerContainer.class);
	}
	
	@Override
    public void afterPropertiesSet() {
        Map<String, Object> beans = getApplicationContext().getBeansWithAnnotation(MessageListener.class);
        beans.forEach(this::registerContainer);
    }
}
