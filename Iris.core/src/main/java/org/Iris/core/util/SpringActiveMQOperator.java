package org.Iris.core.util;

import java.io.Serializable;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class SpringActiveMQOperator {

	protected Map<String, String> queueNames;
	private JmsTemplate jmsTemplate;
	
	protected MessageCreator generateObjectCreator(final Serializable obj) {
		return new MessageCreator() {
            @Override
            public Message createMessage(Session session)
                    throws JMSException {
                ObjectMessage objMsg = session.createObjectMessage();
                objMsg.setObject(obj);
                return objMsg;
            }
        };
	}
	
	protected MessageCreator generateTextCreator(final String str) {
		return new MessageCreator() {
            @Override
            public Message createMessage(Session session)
                    throws JMSException {
            	return session.createTextMessage(str);
            }
        };
	}
	
	protected void sendMessage(String queueName, MessageCreator messageCreator) {
		Queue queue = new ActiveMQQueue(queueName);
        jmsTemplate.setDefaultDestination(queue);
        jmsTemplate.send(messageCreator);
	}
	
	protected String getQueueName(String key) {
		return this.queueNames.get(key);
	}
	
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public void setQueueNames(Map<String, String> queueNames) {
		this.queueNames = queueNames;
	}
}
