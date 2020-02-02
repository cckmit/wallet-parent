package org.wallet.dap.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.StandardEnvironment;

import javax.annotation.Resource;

public abstract class AbstractListenerContainerConfiguration implements ApplicationContextAware, InitializingBean {

	Logger log = LoggerFactory.getLogger(getClass());
	
    private ConfigurableApplicationContext applicationContext;

    @Resource
    private StandardEnvironment environment;

    @Resource
    private MqProperties mqProperties;

    public AbstractListenerContainerConfiguration() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    public final void registerContainer(String beanName, Object bean) {
    	Class<?> clazz = AopUtils.getTargetClass(bean);
        if (!RocketMQListener.class.isAssignableFrom(bean.getClass())) {
            throw new IllegalStateException(clazz + " is not instance of " + RocketMQListener.class.getName());
        }
        RMQListenerContainer container = registerMessageListenerContainer(beanName, bean, clazz);
        String instanceName = mqProperties.getInstanceName();
        container.setInstanceName(instanceName);
        if (!container.isStarted()) {
            try {
                container.start();
            } catch (Exception e) {
                log.error("started container failed. {}", container, e);
                throw new RuntimeException(e);
            }
        }
        log.info("register rocketMQ listener to container, listenerBeanName:{}", beanName);
    }
    
    protected abstract RMQListenerContainer registerMessageListenerContainer(String beanName, Object bean, Class<?> clazz);
    
    public ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public StandardEnvironment getEnvironment() {
		return environment;
	}

	public MqProperties getMqProperties() {
		return mqProperties;
	}

}
