//package com.busap.vcs.chat.service;
//
//import java.util.Map;
//import java.util.Random;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.busap.vcs.chat.util.ChatUtil;
//import com.busap.vcs.chat.util.MessUtil;
//import com.busap.vcs.chat.util.SpringUtils;
//import com.busap.vcs.constants.BicycleConstants;
//
//public class MajiaCommenService implements Runnable {
//	private static final Logger logger = LoggerFactory.getLogger(MajiaCommenService.class);
//	private String roomId;
//	private Integer step;
//	private Integer period;
//	private Integer maxPeriod;
//	private boolean plus;
//	private JedisService jedisService;
//	private Random r = new Random();
//
//	public MajiaCommenService(String roomId,Integer step,Integer period,Integer maxPeriod,Boolean plus){
//		this.roomId = roomId;
//		this.step = step;
//		this.period = period;
//		this.maxPeriod = maxPeriod;
//		this.plus = plus;
//		jedisService = (JedisService)SpringUtils.getBean("jedisService");
//	}
//
//	@Override
//	public void run() {
//		try{
//			logger.info("commen majia setting in room {},step {},period {},plus {}",roomId,step,period,plus);
//			for(int i = 0;i<step;i++){
//				int p = this.period + r.nextInt(this.maxPeriod-this.period);
//				Thread.sleep(p*1000);
//				if(this.plus){
//					addMajiaToRoom(this.roomId);
//				} else {
//					removeMajiaFromRoom(this.roomId);					
//				}
//				
//			}
//		
//		
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		
//	}
//	
//	private void removeMajiaFromRoom(String roomId) {
//		String majiaId = jedisService.getSetRanomMembers(BicycleConstants.ROOM_MAJIAS+roomId);
//		if(majiaId != null){
//			Map<String,String> majiaInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+majiaId);
//			if(majiaInfo!=null && !majiaInfo.isEmpty()){
//				MessUtil.sendUserLeave(majiaId, roomId, majiaInfo);
//				jedisService.deleteSetItemFromShard(BicycleConstants.ROOM_MAJIAS+roomId, majiaId);
//				ChatUtil.decUserCount(roomId);
//			}
//		}
//	}
//	
//	private void addMajiaToRoom(String roomId) {
//		String majiaId = jedisService.getSetRanomMembers(BicycleConstants.MAJIA_UID);
//		
//		logger.info("commen majia setting add majia {}",majiaId);
//		
//		Map<String,String> majiaInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+majiaId);
//		if(majiaInfo!=null && !majiaInfo.isEmpty()){
//			majiaInfo.put("isMajia", "1");
//			MessUtil.sendUserAdd(majiaId, roomId, majiaInfo);
//			jedisService.setValueToSetInShard(BicycleConstants.ROOM_MAJIAS+roomId, majiaId);
//			ChatUtil.incUserCount(roomId);
//		}
//	}
//
//}
