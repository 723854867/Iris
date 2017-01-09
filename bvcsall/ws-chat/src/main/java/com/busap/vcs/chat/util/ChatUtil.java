package com.busap.vcs.chat.util;

import io.netty.channel.Channel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.chat.bean.Base;
import com.busap.vcs.chat.bean.BinaryMessage;
import com.busap.vcs.chat.mess.BinarySendThread;
import com.busap.vcs.chat.mess.MessageSendThread;
import com.busap.vcs.chat.service.JedisService;
import com.busap.vcs.constants.BicycleConstants;

public class ChatUtil {
	private static Logger logger = LoggerFactory.getLogger(ChatUtil.class);
	//userid 与chanel映射，用户长连接关系
	public static Map<String, Channel> uidChannel = new ConcurrentHashMap<String, Channel>();
	//roomId 与用户channel列表映射，频道下用户长连接列表
	public static Map<String, List<Channel>> roomChannels = new ConcurrentHashMap<String, List<Channel>>();
	
	//userid 与chanel映射，用户长连接关系
	public static Map<Channel, String> channelUid = new ConcurrentHashMap<Channel, String>();
//	//chanelId 与用户channel列表映射，频道下用户长连接列表
	public static Map<Channel, String> channelRoom = new ConcurrentHashMap<Channel, String>();
	
//	//user 在线时间
//	public static Map<Channel, Long> userOnline = new ConcurrentHashMap<Channel, Long>();
		
	public static MessageSendThread messageSender;
	
	public static BinarySendThread binarySender;
	
	private static JedisService jedisService;
	
	private static final String appSecret = "a2a866274137a1ac454a73886cf77cdb";
	
	private static String localhost = PpsConfig.getString("websocket.host");
	private static String port = PpsConfig.getString("websokcet.port");
	public static String localUri;
	
	static{
		localUri = localhost + ":" +port;
		messageSender = new MessageSendThread(new LinkedBlockingDeque<WsMessage>());
		new Thread(messageSender).start();
		binarySender = new BinarySendThread(new LinkedBlockingDeque<BinaryMessage>());
		new Thread(binarySender).start();
		
		jedisService = (JedisService)SpringUtils.getBean("jedisService");
	}
	
	/**
	 * 用户进入房间处理
	 * @param base
	 * @param chn
	 * @return
	 */
	public static boolean addUser(Base base,Channel chn){
		if(chn == null || !chn.isOpen()){
			logger.info("长连接用户加入时已关闭!");
			return false;
		}
		
		String uid = base.getParams().get("uid");
		String deviceId = base.getParams().get("deviceId");
		String timestamp = base.getParams().get("timestamp");
		String signature = base.getParams().get("signature");
		String roomId = base.getParams().get("roomId");
		
		if(StringUtils.isNotBlank(roomId)){
			return addUserAndInRoom(chn,uid,roomId);
		}
		
		if(StringUtils.isBlank(uid) || StringUtils.isBlank(deviceId) || !StringUtils.isNumeric(timestamp) || StringUtils.isBlank(signature)){
			logger.warn("invalid connect parameter.uid:{} deviceId:{} timestamp:{} signature:{}",uid,deviceId,timestamp,signature);
			return false;
		}
		
		if(!checkConnection(timestamp,signature)){
			logger.warn("The connection not from valid app client.connection refused.");
			return false;
		}
		
		userConnection(chn,uid);
		
		logger.info("connection successful.uid:{} deviceId:{} timestamp:{} signature:{}",uid,deviceId,timestamp,signature);
		return true;
	}
	
	private static void userConnection(Channel chn,String uid){
		if(StringUtils.isNotBlank(uid)){
			Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+uid);
			if(userInfo.get("chaturl") != null){
				if(!localUri.equals(userInfo.get("chaturl"))){
					MessUtil.sendRelogin(userInfo.get("chaturl"),uid);
				}
			}
			
			jedisService.setValueToMap(BicycleConstants.USER_INFO+uid, "chaturl", localUri);
			
			uidChannel.put(uid, chn);
			channelUid.put(chn, uid);
			
	//		Long loginTime = System.currentTimeMillis();
			jedisService.setValueToSortedSetInShard(BicycleConstants.USER_LAST_HARTBEAT, System.currentTimeMillis(), uid);
			
			MessUtil.sendUserConn(uid);
		}
	}
	
	public static void checkinRoom(Channel chn,String uid, String roomId){
		if(StringUtils.isNotBlank(uid)){
			if(!jedisService.ifKeyExists(BicycleConstants.ROOM_ + roomId) || !jedisService.isKeyExistsInMap(BicycleConstants.ROOM_ + roomId, "id")){
				MessUtil.sendConnFail(chn, roomId);
				return;
			}
			String liveRoom = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "liveRoom");
			if(roomId.equals(liveRoom)){//主播重连恢复直播
				if(jedisService.zrank(BicycleConstants.ROOM_ORDER, liveRoom)!=null && jedisService.keyExists(BicycleConstants.ROOM_+liveRoom)){
					long cr = System.currentTimeMillis();
					Double lastHartbeat = jedisService.zscore(BicycleConstants.USER_LAST_HARTBEAT, uid);
					if((cr-lastHartbeat.longValue())<300000){//断开五分钟内重连恢复
						jedisService.setValueToMap(BicycleConstants.ROOM_+liveRoom, "status", "1");
						MessUtil.sendLiveGoon(uid,roomId);
					} else {
						LiveUtil.endLive(uid, roomId);
					}
				} else {
					LiveUtil.endLive(uid, roomId);
					MessUtil.sendConnFail(chn, roomId);
					return;
				}
			} else if(liveRoom != null){//清理上次直播残留
				if(jedisService.zrank(BicycleConstants.ROOM_ORDER, liveRoom)!=null && jedisService.keyExists(BicycleConstants.ROOM_+liveRoom)){
					LiveUtil.endLive(uid, liveRoom);
				}
			}
			
			if(!jedisService.isSetMemberInShard(BicycleConstants.ROOM_SERVERURLS+roomId, localUri)){
				jedisService.setValueToSetInShard(BicycleConstants.ROOM_SERVERURLS+roomId, localUri);
			}
			
			Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+uid);
			MessUtil.sendUserAdd(uid, roomId,userInfo);
			
			jedisService.setValueToMap(BicycleConstants.USER_INFO+uid, "userRoom", roomId);
			
			jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "normalUserCount");
		}
		
		channelRoom.put(chn, roomId);
		
		List<Channel> channels = roomChannels.get(roomId);
		if(channels == null){
			channels = new ArrayList<Channel>();
		}
		if(!channels.contains(chn)){
			channels.add(chn);
		}
		
		roomChannels.put(roomId, channels);
		//"绿色直播，你我共建！我拍TV禁止直播任何违反法律法规和社会公共道德标准的内容。希望大家共同维护平台的纯净，举报请点击直播主角头像弹层上的“举报”按钮或联系官方客服QQ：3064114611"
		String notice = jedisService.get("LIVE_NOTICE");
		if(StringUtils.isNotBlank(notice)){
			MessUtil.sendNotice(roomId,notice,"提示",chn);
		}
		//推送当前房间点赞数
		MessUtil.sendPraisNumber(chn, roomId);
				
		//推送当前房间用户数
		logger.debug("get in room send room[{}] user count",roomId);
		MessUtil.sendRoomUserCount(roomId,chn);
		
		String roomStatus = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "liveStatus");
		if(roomStatus !=null && "2".equals(roomStatus)){//发直播暂停消息
			MessUtil.sendLiveTimeout(roomId,chn);
		}	
	}
	
	private static boolean addUserAndInRoom(Channel chn,String uid, String roomId) {	
		if(StringUtils.isNotBlank(uid)){
			String oldRoomId = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "userRoom");
			if(StringUtils.isNotBlank(oldRoomId) && !roomId.equals(oldRoomId)){
				leaveRoom(uidChannel.get(uid),uid,oldRoomId);
			}
		}
		
		userConnection(chn,uid);
		
		checkinRoom(chn,uid,roomId);
		
		logger.info("old version for user connection and checkin room,uid:{} room:{}",uid,roomId);
	
		return true;
	}

	private static boolean checkConnection(String timestamp, String signature) {
		if (!MD5(appSecret + timestamp).equals(signature)
				&& !MD5(timestamp + appSecret).equals(signature)) {
			logger.info("ValidateUtil,验证客户端签名失败");
			return false;
		}
		Long now = System.currentTimeMillis(); //获得当前时间
		Long time = Long.valueOf(timestamp);
		Long diff = now - time;
		if (Math.abs(diff) > 1000 * 60 * 8) { // 请求时间在当前时间正负8分钟之外，为非法请求
			logger.info("ValidateUtil,过期请求,diff={}",diff);
			return false;
		}
		return true;
	}

	public static void leaveRoom(Channel chn,String uid, String roomId) {
//		Channel chn = uidChannel.get(uid);
		if(chn != null){
			List<Channel> channels = roomChannels.get(roomId);
			if(channels != null && channels.size()>0){
				channels.remove(uid);
			}else{
				if(jedisService.isSetMemberInShard(BicycleConstants.ROOM_SERVERURLS+roomId, localUri)){
					jedisService.deleteSetItemFromShard(BicycleConstants.ROOM_SERVERURLS+roomId, localUri);
				}
			}
			channelRoom.remove(chn);
		}
//		roomChannels.put(roomId, channels);
		if(StringUtils.isNotBlank(uid)){
			jedisService.deleteValueFromMap(BicycleConstants.USER_INFO+uid, "userRoom");
			
			Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+uid);
			
			jedisService.delete(BicycleConstants.LOOP_GIFT_RECORD + roomId +"_"+ uid);//清空用户在房间内连续礼物赠送记录
			
			MessUtil.sendUserLeave(uid, roomId, userInfo);	
		}
	}

	/**
	 * 用户进入房间，加房间在线人数和总人数
	 * @param roomId
	 */
	public static synchronized void incUserCount(String roomId) {
		Double scoreValue = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
		if(scoreValue!=null && scoreValue>=0){//只更新在线房间
			long step = 1;// onlineNum>=100?5:1;
			
			long maxAccess = jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "maxAccessNumber",step);
			
			Map<String,String> userNumNotice = MessUtil.getUserNumNotice();
			if(userNumNotice!=null){//房间人数达到固定阀值通知
				String notice = userNumNotice.get(maxAccess + "");
				if(StringUtils.isNotBlank(notice)){
					MessUtil.sendNotice(roomId,notice,"提示",null);
				}
			}
			
			jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "onlineNumber",step);
			
			jedisService.zincrByScore(BicycleConstants.ROOM_ORDER, roomId, 1d);
		}
	}
	
	/**
	 * 根据用户名移除用户
	 * @param uid
	 * @return
	 */
	public static boolean removeUser(String uid){
		if(uidChannel.containsKey(uid)){
			return removeUser(uidChannel.get(uid));
		}
		return true;
	}
	/**
	 * 用户离开房间处理
	 * @param chn
	 * @return
	 */
	public static boolean removeUser(Channel chn){
		
		if(chn == null){
			logger.info("要移除的连接不存在！");
			return false;
		}
//		if(userOnline.containsKey(chn)){
//			userOnline.remove(chn);
//		}
		
		String uid = channelUid.get(chn);
//		if(uid != null){
//			jedisService.deleteSortedSetItemFromShard(BicycleConstants.USER_LAST_HARTBEAT, uid);
//		}
		if(StringUtils.isNotBlank(uid)){
			String liveRoom = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "liveRoom");
			if(StringUtils.isNotBlank(liveRoom)){//主播用户未按照正常流程断开直播,结束正在直播的房间
				if(jedisService.zrank(BicycleConstants.ROOM_ORDER, liveRoom)!=null && jedisService.keyExists(BicycleConstants.ROOM_+liveRoom)){
//					LiveUtil.endLive(uid,liveRoom);
					jedisService.setValueToMap(BicycleConstants.ROOM_+liveRoom, "finishTime", System.currentTimeMillis()+"");
					jedisService.setValueToMap(BicycleConstants.ROOM_+liveRoom, "status", "3");
					LiveUtil.anchorDown(uid,liveRoom);
				}
				logger.info("主播{}在房间{}未按正常流程断开直播.直播房间强制结束",uid,liveRoom);
			}
			
			String userRoom = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "userRoom");
			if(StringUtils.isNotBlank(userRoom)){//退出正在观看的房间
				leaveRoom(chn,uid,userRoom);
			}
			
			channelUid.remove(chn);
			uidChannel.remove(uid);
//			userOnline.remove(chn);
		}else{
			logger.info("连接没有对应的用户信息，将直接关闭！");
		}
		
		if(chn.isOpen()){
			chn.close();
		}
		
		logger.info("连接已关闭！uid:{}",uid);
		
		return true;
	}

//	/**
//	 * 用户退出房间，减房间在线人数
//	 * @param roomId
//	 */
//	public static synchronized void decUserCount(String roomId) {
//		Double scoreValue = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
//		if(scoreValue!=null && scoreValue>=0){//不在线房间不更新
//			String online = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "onlineNumber");
//			long onlineNum = StringUtils.isNumeric(online)?Long.parseLong(online):0;
//			long step = -1; //onlineNum>100?-3:-1;
//			
//			if(onlineNum<0){
//				jedisService.setValueToMap(BicycleConstants.ROOM_+roomId, "onlineNumber", "0");
//			}
//			
//			jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "onlineNumber", step);
//			
//					
//			jedisService.zincrByScore(BicycleConstants.ROOM_ORDER, roomId, -1d);
//		}
//	}
	
	private static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
}
