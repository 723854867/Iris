package org.Iris.app.jilu.consumer.listener;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.Iris.app.jilu.consumer.QueueNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JiLuMessageListener<T> implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(JiLuMessageListener.class);
	
	protected int concurrentConsumers = 1;			// 默认为1
	
	private String queueName;
	@Resource
	private QueueNames queueNames;
	
	public JiLuMessageListener(String queueName) {
		this.queueName = queueName;
	}
	
	@Override
	public void onMessage(Message message) {
		try {
			logger.info("ActiveMQ message received, id = {}", message.getJMSMessageID());
			handleMessage(message);
		} catch (Throwable e) {
			logger.error("ActiveMQ message process error!", e);
		}
	}
	
	public abstract void handleMessage(Message message) throws Throwable;
	
	@SuppressWarnings("unchecked")
	protected T getObject(Message message) throws JMSException { 
		ObjectMessage om = (ObjectMessage) message;
		return (T) om.getObject();
	}
	
	protected String getText(Message message) throws JMSException { 
		TextMessage tm = (TextMessage) message;
		return tm.getText();
	}
	
	public String getDestination() {
		return queueNames.getDestination(queueName);
	}
	
	public void setConcurrentConsumers(int concurrentConsumers) {
		this.concurrentConsumers = concurrentConsumers;
	}
	
	public int getConcurrentConsumers() {
		return concurrentConsumers;
	}
}
