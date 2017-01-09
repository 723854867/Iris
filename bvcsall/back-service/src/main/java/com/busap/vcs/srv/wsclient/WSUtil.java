package com.busap.vcs.srv.wsclient;

import org.jboss.logging.Logger;


public class WSUtil {

	private static Logger logger = Logger.getLogger(WSUtil.class);
	private static WebSocketClient wsClient = WebSocketClient.getInstance();
		
	private static String CHECKER_CODE = "000";
	
	public static void initUrl(String url){
		wsClient.setUrl(url);
		logger.info("wsclient init url:"+url);
	}
	
	public static void sendMessage(String vid){
		logger.info("new video need to check:"+vid);
		wsClient.sendMessage(CHECKER_CODE);
	}

}
