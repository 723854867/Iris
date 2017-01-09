package com.busap.vcs.chat.srv.wsclient;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleEchoClient implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(SimpleEchoClient.class);
	private String url;
	
	private WebSocketClient client;
	private SimpleEchoSocket socket;
	
	public SimpleEchoClient(String url,String client){
		this.url = "ws://"+url+"/ws/consume?client="+client+"_"+System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		logger.info("start connection to {}",url);
		client = new WebSocketClient();
		socket = new SimpleEchoSocket();
		
		try {
			client.setMaxIdleTimeout(12*3600000);//12小时
			client.start();
			URI uri = new URI(url);
			ClientUpgradeRequest request = new ClientUpgradeRequest();
			client.connect(socket, uri, request);
			
			socket.awaitClose(12, TimeUnit.HOURS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("connection closed to url:{}",url);
	}
	
	public void sendBinary(byte[] bytes){
		socket.sendBytes(bytes);
	}
	
	public void sendMessage(String textMessage){
		socket.sendMessage(textMessage);
	}
	
	public boolean isConnected(){		
		if(this.socket==null){
			return false;
		}
		if(this.socket.isConnected()){
			return true;
		}
		
		return false;
	}
	
	public void close(){
		try {
			client.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
