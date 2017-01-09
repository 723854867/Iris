package com.busap.vcs.chat.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.base.WsMessage;

public class MessageMergeUtil {
	private final Logger logger = LoggerFactory.getLogger(MessageMergeUtil.class);
	
	public static final Map<String, List<WsMessage>> roomMessage = new ConcurrentHashMap<String, List<WsMessage>>();
	private static MessageMergeThread mergeThread;
	static{
		mergeThread = new MessageMergeThread();
		new Thread(mergeThread).start();
	}
	
	public static void addMessage(String roomId,WsMessage message){
		if(StringUtils.isNotBlank(roomId)) {
			List<WsMessage> list = roomMessage.get(roomId);
			if(list == null){
				list = Collections.synchronizedList(new ArrayList<WsMessage>());
				roomMessage.put(roomId, list);
			}
			list.add(message);
		}
	}
	
	public static void main(String arg[]){
		for(int i=0;i<120;i++){
			WsMessage m = new WsMessage();
			m.setChildCode("1001");
			m.setCode("100");
			m.setSenderId("118991");
			m.setSenderName("这是中文版");
			m.setRoomId("40968");
			m.setContent("来了"+i);
			
			m.getExtra().put("userid", 118991);
			m.getExtra().put("sex", "1");
			m.getExtra().put("name", "这是中文版");
			m.getExtra().put("username", "123456789");
			m.getExtra().put("signature", "sbsbsbsbsbsbsbsbsbsbsbsbsbsb");
			m.getExtra().put("vipStat", "1");
			m.getExtra().put("pic", "tututututututututututututututututuuttutututututututututu");
			
			MessageMergeUtil.addMessage("40968", m);
		}
	}
	
}
