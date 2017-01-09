package com.busap.vcs.chat.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.chat.service.JedisService;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.MessageConst;

public class LiveUtil {
	private static Logger logger = LoggerFactory.getLogger(LiveUtil.class);

	//直播房间自动推送、自动点赞服务
//	public static Map<String,LiveThreadService> roomLiveThread = new ConcurrentHashMap<String,LiveThreadService>();
	
	//直播开始时间
//	public static Map<String,Long> roomStartTime = new ConcurrentHashMap<String,Long>();
			
	private static JedisService jedisService = (JedisService)SpringUtils.getBean("jedisService");	
	//阶段点赞数
	public static Long praiseNum=0l;
	
	public static void startLive(WsMessage message){
		String uid = message.getSenderId();
		String roomId = message.getRoomId();
		if(StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(roomId)){
			jedisService.setValueToMap(BicycleConstants.USER_INFO+uid, "liveRoom",roomId);
			
//			Channel chn = ChatUtil.uidChannel.get(uid);
//			ChatUtil.channelRoom.put(chn, roomId);
//			
//			List<Channel> channels = ChatUtil.roomChannels.get(roomId);
//			if(channels == null){
//				channels = new ArrayList<Channel>();
//			}
//			if(channels.contains(chn)){
//				channels.remove(chn);
//			}
//			channels.add(chn);
//			
//			ChatUtil.roomChannels.put(roomId, channels);
			
			MessUtil.sendToMessageQueue(message);
		}
	}
	
	public static void endLive(String uid,String roomId){
		String stime = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "startTime");
		Long startTime = StringUtils.isNumeric(stime)?Long.parseLong(stime):Long.parseLong(jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "createDate"));
		logger.info("endlive room:{},startTime:{}",roomId,startTime);
		if(startTime != null){
			Long endTime = System.currentTimeMillis();
			Long duration = endTime - startTime;
			jedisService.setValueToMap(BicycleConstants.ROOM_+roomId, "finishDate", endTime.toString());
			jedisService.setValueToMap(BicycleConstants.ROOM_+roomId, "duration", duration.toString());
			logger.info("live was end,info:[roomId:{},uid:{},start:{},end:{},duration:{}]",roomId,uid,startTime,endTime,duration);
		}
		Long userCount = jedisService.scard("ROOM_USER_ALL_"+roomId);
		jedisService.setValueToMap(BicycleConstants.ROOM_+roomId, "UV", userCount.toString());//记录UV
		
		//从房间列表移除
		jedisService.deleteSortedSetItemFromShard(BicycleConstants.ROOM_ORDER, roomId);
		
		int t = 1;
		while(jedisService.zrank(BicycleConstants.ROOM_ORDER, roomId)!=null){
			jedisService.deleteSortedSetItemFromShard(BicycleConstants.ROOM_ORDER, roomId);
			t++;
			logger.info("delete room {} from room_order,times: {}",roomId,t);
			if(t>5)
				break;
		}

		String points = jedisService.get(BicycleConstants.POINT_NUMBER_BY_ROOM_+roomId);
		if(StringUtils.isBlank(points)){
			points = "0";
		}
		String giftNum = jedisService.get(BicycleConstants.GIFT_NUMBER_BY_ROOM_+roomId);
		if(StringUtils.isBlank(giftNum)){
			giftNum = "0";
		}
		//发送直播结束消息
		MessUtil.sendLiveEnd(roomId, uid,points,giftNum);
//		//回调直播结束接口
		String result = HttpUtil.doGet(PpsConfig.getString("live_end_callback")+roomId, 5);
		logger.info("room {} live end,callback result:{}",roomId,result);
	}

	/**
	 * 主播断线
	 * @param uid
	 * @param liveRoom
	 */
	public static void anchorDown(String uid, String liveRoom) {
		WsMessage m = new WsMessage();
		m.setCode(MessageConst.LIVE);
		m.setChildCode(MessageConst.LIVE_ANCHORLEAVE);
		m.setRoomId(liveRoom);
		m.setSenderId(uid);
		
		MessUtil.sendToMessageQueue(m);
	}
	
}
