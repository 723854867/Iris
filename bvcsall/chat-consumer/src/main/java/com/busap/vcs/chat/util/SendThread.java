//package com.busap.vcs.chat.util;
//
//import java.util.Map;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.busap.vcs.chat.srv.wsclient.MyWebSocketClient;
//import com.busap.vcs.chat.srv.wsclient.SimpleEchoSocket;
//
//public class SendThread implements Runnable {
//	private static Logger logger = LoggerFactory.getLogger(SendThread.class);
//	
//	private static MyWebSocketClient wsClient = MyWebSocketClient.getInstance();
//	
//	@Override
//	public void run() {
//		while(true){
//			try{
//				while(!RoomUtil.messageQueue.isEmpty()){
//					Map<String,Object> msg = RoomUtil.messageQueue.poll();
//					if(!msg.isEmpty()){
//						Set<String> keys = msg.keySet();
//						for(String url:keys){
//							SimpleEchoSocket socket = wsClient.getWebSocketConnection(url, null);
//							if(socket !=null && socket.isConnected()){
//								Object message = msg.get(url);
//								if(message instanceof String){
//									socket.sendMessage((String)message);
//								} else if(message instanceof byte[]){
//									socket.sendBytes((byte[])message);
//								} else {
//									logger.warn("invalid message.{}",message);
//								}
//							}
//						}
//					}
//					RoomUtil.messageQueue.remove(msg);
//				}
//			}catch(Exception ex){
//				ex.printStackTrace();
//			}
//			try {
//				Thread.sleep(5);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//	}
//}
