package com.busap.vcs.chat.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.chat.srv.JedisService;
import com.busap.vcs.chat.srv.LiveService;
import com.busap.vcs.constants.BicycleConstants;

public class LiveCheckThread implements Runnable {
	private final Logger logger = LoggerFactory.getLogger(LiveCheckThread.class);
	private String uid;
	private String roomId;
	private JedisService jedisService;
	private LiveService liveService;
	public LiveCheckThread(String uid,String roomId,JedisService jedisService,LiveService liveService){
		this.roomId = roomId;
		this.uid = uid;
		this.jedisService = jedisService;
		this.liveService = liveService;
	}
	
	@Override
	public void run() {
		try{
			logger.info("anchor {} down in room {},waiting for reconnection.",uid,roomId);
			CountDownLatch countDown = new CountDownLatch(1);
			countDown.await(5, TimeUnit.MINUTES);
			if(jedisService.zrank(BicycleConstants.ROOM_ORDER, roomId)!=null && jedisService.keyExists(BicycleConstants.ROOM_+roomId)){
				if(!"1".equals(jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "status"))){
					logger.info("anchor {} disconnect timeout in room {}.force live end.",uid,roomId);
					String points = jedisService.get(BicycleConstants.POINT_NUMBER_BY_ROOM_+roomId);
					if(StringUtils.isBlank(points)){
						points = "0";
					}
					String giftNum = jedisService.get(BicycleConstants.GIFT_NUMBER_BY_ROOM_+roomId);
					if(StringUtils.isBlank(giftNum)){
						giftNum = "0";
					}
					RoomUtil.sendLiveEnd(roomId, uid, points, giftNum);
//					liveService.doLiveEnd(uid, roomId);
				}
			}
			countDown.countDown();
		}catch(Exception ex){
			logger.warn("check anchor live status caught an exception.uid:"+ uid +" roomId:"+ roomId ,ex);
		}
		
	}

}
