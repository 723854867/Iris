package org.Iris.app.jilu.consumer.listener;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JiLuMessageListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(JiLuMessageListener.class);
	
	protected String destinationName;
	protected int concurrentConsumers = 1;			// 默认为1
	
	public abstract void handleMessage(Message message) throws Throwable;
	
	public JiLuMessageListener(String destinationName) {
		this.destinationName = destinationName;
	}
	
	@Override
	public void onMessage(Message message) {
		try {
			logger.info("ActiveMQ message received, id={}", message.getJMSMessageID());
			handleMessage(message);
		} catch (Throwable e) {
			logger.error("ActiveMQ message process error!", e);
		}
	}
	
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	
	public String getDestinationName() {
		return destinationName;
	}
	
	public void setConcurrentConsumers(int concurrentConsumers) {
		this.concurrentConsumers = concurrentConsumers;
	}
	
	public int getConcurrentConsumers() {
		return concurrentConsumers;
	}
}
