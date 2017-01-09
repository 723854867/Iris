package com.busap.vcs.chat.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.busap.vcs.base.WsMessage;

public class ConsumeUtil {
	private static Logger logger = LoggerFactory.getLogger(ConsumeUtil.class);
	
	public static Set<Channel> consumeChannels = new HashSet<Channel>();
	
	public static void addChannel(Channel chn,String clientId){
		consumeChannels.add(chn);
		logger.info("consume channel connected,clientId:{}",clientId);
	}
	
	public static void consume(String text){
		WsMessage message = (WsMessage)JSONObject.parseObject(text, WsMessage.class);
		MessUtil.recieveMessage(message);
	}
	
	public static void removeChannel(Channel chn){
		if(chn!=null && consumeChannels.contains(chn)){
			consumeChannels.remove(chn);
			logger.info("channel removed.{}",chn.remoteAddress());
			chn.close();
		}
	}

	public static void consumeBinary(ByteBuf buf) {
		MessUtil.recieveBinaryMessage(buf);
	}
}
