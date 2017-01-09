package com.busap.vcs.chat.mess;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.chat.util.ChatUtil;
import com.busap.vcs.chat.util.MessUtil;
import com.busap.vcs.constants.MessageConst;

public class MessageSendThread implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(MessageSendThread.class);
	private Queue<WsMessage> messageQueue;
	
	public MessageSendThread(Queue<WsMessage> messQueue){
		this.messageQueue = messQueue;
	}
	@Override
	public void run() {
		logger.info("Message send thread started.");
		while(true){
			synchronized(messageQueue){
				while(!messageQueue.isEmpty()){
					try{
						WsMessage message = messageQueue.poll();
						if(message.getRoomId()!=null){//指定房间
							if(message.getRecieverId() != null 
								&& (MessageConst.CHAT_PRIVATE.equals(message.getChildCode())//私聊或关注指令发给指定用户
									|| MessageConst.COMMEN_ATTENTION.equals(message.getChildCode()))){//私聊或关注指令发给指定用户
								if(ChatUtil.uidChannel.containsKey(message.getRecieverId())){
									Channel chn = ChatUtil.uidChannel.get(message.getRecieverId());
									MessUtil.send(chn,message);
								}
							}else{
								MessUtil.sendToRoom(message.getRoomId(),message);
							}
						}else {
							Set<String> roomIds = ChatUtil.roomChannels.keySet();
							for(String roomId:roomIds){
								List<Channel> channels = ChatUtil.roomChannels.get(roomId);
								if(channels != null && channels.size()>0){
									for(Channel chn:channels){
										MessUtil.send(chn,message);
									}
								}
							}
						}
						
						messageQueue.remove(message);
					}catch(Exception ex){
						logger.error("message send thread caught an exception.", ex);
					}
				}
			}
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Queue<WsMessage> getMessageQueue() {
		return messageQueue;
	}

	public void setMessageQueue(Queue<WsMessage> messageQueue) {
		this.messageQueue = messageQueue;
	}

}
