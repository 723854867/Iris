package com.busap.vcs.chat.srv.wsclient;


import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyWebSocketClient {
	private Logger logger = LoggerFactory.getLogger(MyWebSocketClient.class);
	private Map<String,SimpleEchoClient> connections = new HashMap<String,SimpleEchoClient>();
			
	private MyWebSocketClient(){
	}
	
	private static class SingletonHolder {  
	    private static final MyWebSocketClient INSTANCE = new MyWebSocketClient();  
	}  
	public synchronized static final MyWebSocketClient getInstance() {  
	    return SingletonHolder.INSTANCE;  
	}  
	
	public synchronized SimpleEchoClient getWebSocketConnection(String url,String clientId){
		
		try {
			SimpleEchoClient webSocketConnection = connections.get(url);
			if(webSocketConnection != null && webSocketConnection.isConnected()){
				return webSocketConnection;
			}
			logger.info("start connect to url {}",url);
			webSocketConnection = new SimpleEchoClient(url,clientId);
			new Thread(webSocketConnection).start();
			
			long t1 = System.currentTimeMillis();
			boolean connected = false;
			while(!connected){//等待连接成功，
				if(webSocketConnection.isConnected()){
					connections.put(url, webSocketConnection);
					logger.info("create connection successful:{}",url);
					return webSocketConnection;
				}
				long t2 = System.currentTimeMillis();
				if((t2-t1)>5000){
					break;
				}
				Thread.sleep(1000);
			}
			if(webSocketConnection!=null){
				webSocketConnection.close();
			}
			logger.info("create connection fail:{}", url);
			return null;
		} catch (Exception e) {
			logger.info("create connection error:" + url,e);
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean sendMessage(String url,String msg,String clientId){
		boolean successed = false;
		SimpleEchoClient connection = getWebSocketConnection(url,clientId);
		
		if(connection != null && connection.isConnected()){
			try {
				connection.sendMessage(msg);
				successed = true;
				logger.info("send message success msg:"+msg);
			} catch (Exception e) {
				logger.error("send message error,the connection lost:" + url,e);
				e.printStackTrace();
			}
		} else {
			logger.info("message lost:{}",msg);
		}
		
		return successed;
	}
	

	public boolean sendBinary(String url,byte[] bytes,String clientId){
		boolean successed = false;
		SimpleEchoClient connection = getWebSocketConnection(url,clientId);
		
		if(connection != null && connection.isConnected()){
			try {
				connection.sendBinary(bytes);
				logger.info("send binary successful.url:{}",url);
				successed = true;
			} catch (Exception e) {
				logger.error("send message error,the connection lost:" + url,e);
				e.printStackTrace();
			}
		} else {
			logger.info("message lost:{}",url);
		}
		
		return successed;
	}
}
