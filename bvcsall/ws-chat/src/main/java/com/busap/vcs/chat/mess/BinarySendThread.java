package com.busap.vcs.chat.mess;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.chat.bean.BinaryMessage;
import com.busap.vcs.chat.util.ChatUtil;
import com.busap.vcs.chat.util.MessUtil;

public class BinarySendThread implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(BinarySendThread.class);
	private Queue<BinaryMessage> messageQueue;
	
	public BinarySendThread(Queue<BinaryMessage> messQueue){
		this.messageQueue = messQueue;
	}
	@Override
	public void run() {
		logger.info("Binary Message send thread started.");
		while(true){
			synchronized(messageQueue){
				while(!messageQueue.isEmpty()){
					try{
						BinaryMessage message = messageQueue.poll();
						int length = message.getContent().length;
						
						if("1".equals(message.getType())){
							String roomId = message.getRoomId();
							if(roomId == null){
								logger.warn("不合法的消息，{}",message);
								continue;
							}
							
							List<Channel> channels = ChatUtil.roomChannels.get(roomId);
							if(channels != null && channels.size()>0){
								for(Channel chn:channels){
									MessUtil.sendBinary(chn,PooledByteBufAllocator.DEFAULT.directBuffer(length).writeBytes(message.getContent()));
								}
							}
						} else {
							String recieverId = message.getRecieverId();
							if(recieverId == null){
								logger.warn("不合法的消息，{}",message);
								continue;
							}
							Channel chn = ChatUtil.uidChannel.get(recieverId);
							MessUtil.sendBinary(chn,PooledByteBufAllocator.DEFAULT.directBuffer(length).writeBytes(message.getContent()));
						}
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

	public Queue<BinaryMessage> getMessageQueue() {
		return messageQueue;
	}

	public void setMessageQueue(Queue<BinaryMessage> messageQueue) {
		this.messageQueue = messageQueue;
	}

}
