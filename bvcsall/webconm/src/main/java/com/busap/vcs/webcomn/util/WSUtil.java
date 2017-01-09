package com.busap.vcs.webcomn.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.base.WsMessage;

public class WSUtil {

	private static Logger logger = LoggerFactory.getLogger(WSUtil.class);
	private static WebSocketClient wsClient = WebSocketClient.getInstance();
	
	/**
	 * 后台消息发送
	 * @param url
	 * @param msg
	 */
	public static void sendMessage(String url,String msg){
		wsClient.sendMessage(url,msg);
		logger.info("admin message send over:{}",msg);
	}
	/**
	 * 管理员强制结束直播消息
	 * @param roomId
	 * @param adminId
	 * @param content
	 * @return
	 */
	public static String buildLiveEndMsg(String roomId,String adminId,String content){
		WsMessage m = new WsMessage();
		m.setCode("400");
		m.setChildCode("4003");
		m.setRoomId(roomId);
		m.setSenderId(adminId);
		m.setContent(content);
		m.setTitle("直播终结");
		
		return m.toString();
	}
}
