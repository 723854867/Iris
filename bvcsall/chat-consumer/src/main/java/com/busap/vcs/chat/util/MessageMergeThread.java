package com.busap.vcs.chat.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.base.WsMessage;

public class MessageMergeThread implements Runnable{
	private final Logger logger = LoggerFactory.getLogger(MessageMergeThread.class);
	
	public MessageMergeThread (){
		
	}
	@Override
	public void run() {
		while(true){
			CountDownLatch countDown = new CountDownLatch(1);
			try{
				Set<String> keys = MessageMergeUtil.roomMessage.keySet();
				if(keys != null && keys.size()>0){
					for(String roomId:keys){
						List<WsMessage> list = MessageMergeUtil.roomMessage.get(roomId);
						if(list != null && list.size()>0){
							while(list.size()>100){
								List<WsMessage> msList = new ArrayList<WsMessage>();
								msList.addAll(list.subList(0, 100));
								list.removeAll(msList);
								byte[] buf = GZipUtil.buildChat(roomId, msList,1);
								RoomUtil.sendBinaryToRoom(roomId, buf);
								logger.debug("send merged message to room:{} size:{}",roomId,msList.size());
							}
							List<WsMessage> msList = new ArrayList<WsMessage>();
							msList.addAll(list);
							list.clear();
							byte[] buf = GZipUtil.buildChat(roomId, msList,1);
							RoomUtil.sendBinaryToRoom(roomId, buf);
							logger.debug("send merged message to room:{} size:{}",roomId,msList.size());
						} else {
							MessageMergeUtil.roomMessage.remove(roomId);
						}
					}
				}
			} catch(Exception ex){
				logger.error("message merge thread error.", ex);
				ex.printStackTrace();
			} finally{
				try {
					countDown.await(50, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		}
	}
}