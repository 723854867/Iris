package com.busap.vcs.webcomn.util;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlightweb.IWebSocketConnection;
import org.xlightweb.TextMessage;
import org.xlightweb.WebSocketConnection;

public class WebSocketClient {
	private Logger logger = LoggerFactory.getLogger(WebSocketClient.class);
	private Map<String,IWebSocketConnection> webSocketConnections = new HashMap<String,IWebSocketConnection>();
	
	private WebSocketClient(){
		
	}
	
	private static class SingletonHolder {  
	    private static final WebSocketClient INSTANCE = new WebSocketClient();  
	}  
	public static final WebSocketClient getInstance() {  
	    return SingletonHolder.INSTANCE;  
	}  
	
	public IWebSocketConnection getWebSocketConnection(String url){
		if (!StringUtils.isBlank(url) && !"null".equals(url.trim())){
			try {
				IWebSocketConnection webSocketConnection = new WebSocketConnection(url);
				this.webSocketConnections.put(url, webSocketConnection);
				logger.info("create connection success:" + url);
				return webSocketConnection;
			} catch (IOException e) {
				logger.info("create connection error:" + url,e);
				e.printStackTrace();
			}
		} else {
			logger.info("url not found for :" + url);
		}
		return null;
	}
	
	public boolean sendMessage(String url,String msg){
		boolean successed = false;
		IWebSocketConnection connection = this.webSocketConnections.get(url);
		TextMessage tmsg = new TextMessage(msg);
		if(connection != null && connection.isOpen()){
			try {
				connection.writeMessage(tmsg);
				successed = true;
				logger.info("send message success msg:"+msg);
			} catch (IOException e) {
				logger.info("send message error,the connection lost:" + url,e);
				e.printStackTrace();
			}
		} else {//连接如果关闭自动重连
			connection = this.getWebSocketConnection(url);
			if(connection != null && connection.isOpen()){
				try {
					connection.writeMessage(tmsg);
					successed = true;
					logger.info("send message success msg:"+msg);
				} catch (IOException e) {
					logger.info("send message error,the connection lost:" + url,e);
					e.printStackTrace();
				}
			}
		}
		
		return successed;
	}
	
//	public void sendMsg(String msg,String url){
//		if(StringUtils.isBlank(url) || StringUtils.isBlank(msg)){
//			return ;
//		}
//		TextMessage tmsg = new TextMessage(msg);
//		try {
//			if(webSocketConnection == null ){
//				webSocketConnection = new WebSocketConnection(url);
//			}
//			logger.info("connection is open : "+webSocketConnection.isOpen());
//			
//			webSocketConnection.writeMessage(tmsg);
//			logger.info("send all success - - "+msg);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
//	public void close(){
//		try {
//			if(webSocketConnection!= null){
//
//				logger.info("close connection ..............");
//				webSocketConnection.close();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
