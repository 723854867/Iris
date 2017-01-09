package com.busap.vcs.service.msg;

//import javax.jms.Message;
//import javax.jms.MessageListener;
//import javax.jms.ObjectMessage;
//
//import org.apache.log4j.Logger;
//  
//public class JMessageConsumer implements MessageListener {  
//  
//    private static final Logger LOG = Logger.getLogger(JMessageConsumer.class);  
//      
//    public void onMessage(Message message) {  
//        if (message instanceof ObjectMessage) {  
//        	try {  
//        		ObjectMessage objMessage = (ObjectMessage)message;
//        		com.busap.vcs.base.Message text = (com.busap.vcs.base.Message) objMessage.getObject();  
//            
//                LOG.info("Received message:" + text.getAction());  
//            } catch (Exception e) {  
//                e.printStackTrace();  
//            }  
//        }  
//    }  
//}