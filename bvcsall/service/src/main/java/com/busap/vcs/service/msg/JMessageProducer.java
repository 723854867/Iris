package com.busap.vcs.service.msg;
//
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.ObjectMessage;
//import javax.jms.Session;
//
//import org.apache.log4j.Logger;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.jms.core.MessageCreator;
//  
//public class JMessageProducer {  
//      
//    private static final Logger LOG = Logger.getLogger(JMessageProducer.class);  
//    private JmsTemplate jmsTemplate;  
//  
//    public void setJmsTemplate(JmsTemplate jmsTemplate) {  
//        this.jmsTemplate = jmsTemplate;  
//    }  
//      
//    public void sendMessage(final com.busap.vcs.base.Message message) {  
//        LOG.info("Send message: " + message);  
//        jmsTemplate.send(new MessageCreator() {    
//            public Message createMessage(Session session) throws JMSException {  
//                ObjectMessage objectMessage = session.createObjectMessage(message);  
//                return objectMessage;  
//            }  
//              
//        });  
//    }  
//  
//}  