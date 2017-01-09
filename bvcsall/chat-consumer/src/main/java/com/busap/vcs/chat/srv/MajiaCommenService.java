package com.busap.vcs.chat.srv;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.chat.util.RoomUtil;
import com.busap.vcs.constants.BicycleConstants;

public class MajiaCommenService implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(MajiaCommenService.class);
	private String roomId;
	private Integer step;
	private Integer period;
	private Integer maxPeriod;
	private boolean plus;
	private JedisService jedisService;
	private Random r = new Random();
	
	private CountDownLatch countDown;

	public MajiaCommenService(String roomId,Integer step,Integer period,Integer maxPeriod,Boolean plus,JedisService jedisServic){
		this.roomId = roomId;
		this.step = step;
		this.period = period;
		this.maxPeriod = maxPeriod;
		this.plus = plus;
		this.jedisService = jedisServic;
		this.countDown  = new CountDownLatch(step);
	}

	@Override
	public void run() {
		try{
			logger.info("commen majia setting in room {},step {},period {},plus {}",roomId,step,period,plus);
			for(int i = 0;i<step;i++){
				int p = this.period + r.nextInt(this.maxPeriod-this.period);
				countDown.await(p, TimeUnit.SECONDS);
				if(this.plus){
					addMajiaToRoom(this.roomId);
				} else {
					removeMajiaFromRoom(this.roomId);
				}
				
			}
		
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	private void removeMajiaFromRoom(String roomId) {
		String majiaId = jedisService.getSetRanomMembers(BicycleConstants.ROOM_MAJIAS+roomId);
		if(majiaId != null){
			Map<String,String> majiaInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+majiaId);
			if(majiaInfo!=null && !majiaInfo.isEmpty()){
				RoomUtil.sendUserLeave(majiaId, roomId);
				jedisService.deleteSetItemFromShard(BicycleConstants.ROOM_MAJIAS+roomId, majiaId);
				RoomUtil.decUserCount(roomId);
			}
		}
	}
	
	private void addMajiaToRoom(String roomId) {
		String majiaId = jedisService.getSetRanomMembers(BicycleConstants.MAJIA_UID);
		
		logger.info("commen majia setting add majia {}",majiaId);
		
		Map<String,String> majiaInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+majiaId);
		if(majiaInfo!=null && !majiaInfo.isEmpty()){
			RoomUtil.sendUserAdd(majiaId, roomId);
			jedisService.setValueToSetInShard(BicycleConstants.ROOM_MAJIAS+roomId, majiaId);
			RoomUtil.incUserCount(roomId);
		}
	}

}
