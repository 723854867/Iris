package org.Iris.app.jilu.consumer;

import org.Iris.app.jilu.consumer.listener.JiLuMessageListener;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class ListenerPostProcessor implements BeanPostProcessor, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof JiLuMessageListener) {
			JiLuMessageListener listener = (JiLuMessageListener)bean;
			DefaultMessageListenerContainer container = applicationContext.getBean(DefaultMessageListenerContainer.class);
			container.setMessageListener(listener);
			container.setDestination(new ActiveMQQueue(listener.getDestinationName()));
			if (listener.getConcurrentConsumers() > 0) 
				container.setConcurrentConsumers(listener.getConcurrentConsumers());
			container.start();
		}
		return bean;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
