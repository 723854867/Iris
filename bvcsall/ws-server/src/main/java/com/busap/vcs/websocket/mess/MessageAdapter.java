package com.busap.vcs.websocket.mess;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.busap.vcs.websocket.bean.Base;

public class MessageAdapter {
	public static Map<String, Channel> uidChannel = new ConcurrentHashMap<String, Channel>();
	private static Logger logger = Logger.getLogger(MessageAdapter.class);
	
	public static boolean addChannel(Base base,Channel chn){
		boolean b = false;
		String uid = base.getParams().get("uid");
		if(StringUtils.isBlank(uid) || chn == null){
			logger.info("invalid channel connect,closed");
			if(chn!=null){
				chn.close();
			}
			return b;
		}
		//绑定用户连接
		uidChannel.put(uid, chn);
		b = true;
		logger.info("channel connected ok.uid:"+uid);
		return b;
	}
	
	public static void sendMessage(String mess,Channel chn){
		if (StringUtils.isNotBlank(mess) && chn != null && chn.isOpen()) {
			logger.info("push message to channel.mess:"+mess);
			chn.write(new TextWebSocketFrame(mess));
			chn.flush();
		}
	}

	public static void sendToChecker(String message) {
		if(uidChannel.isEmpty()){
			logger.info("on checker online.");
			return;
		}
		logger.info("send message to video checker");
		Set<String> uids = uidChannel.keySet();
		for(String uid:uids){
			Channel chn = uidChannel.get(uid);
			sendMessage(message,chn);
		}		
	}

}
