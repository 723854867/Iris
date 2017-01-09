package com.busap.vcs.chat.srv;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Tuple;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.chat.util.MessageMergeUtil;
import com.busap.vcs.chat.util.RoomUtil;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.MessageConst;
import com.busap.vcs.data.mapper.RoomDao;

@Service("liveService")
public class LiveService {

	private static Logger logger = LoggerFactory.getLogger(LiveService.class);

	@Value("#{configProperties['client_id']}")
	private String clientId;
	
	private final Random r = new Random();
	
	@Autowired
	private RoomDao roomDao;
	
	@Resource(name = "jedisService")
	private JedisService jedisService;
	
	public void doLiveStart(String uid,String roomId){
		long startTime = new Date().getTime();
		jedisService.setValueToMap(BicycleConstants.ROOM_+roomId, "startTime", startTime+"");
		double liveWeight = 0;
		String lw = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "liveWeight");
		if(StringUtils.isNumeric(lw)){
			liveWeight = Double.parseDouble(lw);
		}else{
			lw = "0";
		}
		jedisService.zincrByScore(BicycleConstants.ROOM_ORDER, roomId, liveWeight);
		jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "additionalNumber", Long.parseLong(lw));
		
		jedisService.getIncrValueFromMap(BicycleConstants.ROOM_ + roomId, "mjPraiseNumber", 1);
		jedisService.getIncrValueFromMap(BicycleConstants.ROOM_ + roomId, "praiseNumber", 1);
		
		this.doMajiaService(uid, roomId);
		logger.info("user {} live started in room{}.liveWeight {}",uid,roomId,liveWeight);
	}
	
	public void doLiveEnd(String uid,String roomId){
		Double scoreValue = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
		if(scoreValue==null || !jedisService.ifKeyExists(BicycleConstants.ROOM_+roomId)){
			logger.info("room has removed.user:{} room:{}",uid,roomId);
			return;
		}
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
		Date finishTime = new Date();
		long endTime = finishTime.getTime();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roomId", roomId);
		params.put("finishTime", finishTime);
		String startTime = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "startTime");
		
		long duration = endTime - Long.parseLong(startTime);
		params.put("duration", duration);
		
		String chatCount = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "chatCount");
		params.put("chatCount", StringUtils.isNumeric(chatCount)?chatCount:0);
		
		String anchorChatCount = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "anchorChatCount");
		params.put("anchorChatCount", StringUtils.isNumeric(anchorChatCount)?anchorChatCount:0);
		
		String maxAccessNumber = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "maxAccessNumber");
		params.put("maxAccessNumber", StringUtils.isNumeric(maxAccessNumber)?maxAccessNumber:0);
		
		String normalUserCount = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "normalUserCount");
		params.put("normalUserCount", StringUtils.isNumeric(normalUserCount)?normalUserCount:0);
		
		String praiseNumber = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "praiseNumber");
		params.put("praiseNumber", StringUtils.isNumeric(praiseNumber)?praiseNumber:0);
		
		Long userCount = jedisService.scard("ROOM_USER_ALL_"+roomId);
		params.put("uv", userCount);
		
		String points = jedisService.get(BicycleConstants.POINT_NUMBER_BY_ROOM_+roomId);
		if(StringUtils.isBlank(points)){
			points = "0";
		}
		params.put("pointNumber", points);
		
		String giftNum = jedisService.get(BicycleConstants.GIFT_NUMBER_BY_ROOM_+roomId);
		if(StringUtils.isBlank(giftNum)){
			giftNum = "0";
		}
		params.put("giftNumber", giftNum);
		
		roomDao.endLive(params);//结束状态写库
		
		//清理缓存
		jedisService.deleteValueFromMap(BicycleConstants.USER_INFO+uid, "liveRoom");
		
		jedisService.delete("ROOM_USER_ALL_"+roomId);//
		
		jedisService.delete(BicycleConstants.GIFT_NUMBER_BY_ROOM_+roomId);
		jedisService.delete(BicycleConstants.POINT_NUMBER_BY_ROOM_+roomId);
		
		jedisService.delete(BicycleConstants.ROOM_+roomId);
		jedisService.delete(BicycleConstants.ROOM_USERS+roomId);
		
		jedisService.delete(BicycleConstants.ROOM_SERVERURLS+roomId);
	}
	
	/**
	 * 直播开启执行机器人策略
	 * @param uid
	 * @param roomId
	 */
	private void doMajiaService(String uid,String roomId){
		MajiaGetinRoom majiaInroom = new MajiaGetinRoom(uid,roomId);
		new Thread(majiaInroom).start();
		
		AddUserCount addUser = new AddUserCount(uid,roomId);
		new Thread(addUser).start();
		
		ContributionTop3 top3 = new ContributionTop3(uid,roomId);
		new Thread(top3).start();
	}
	
	/**
	 * 房间马甲策略-自动进马甲
	 * @author dmsong
	 *
	 */
	class MajiaGetinRoom implements Runnable{
		private String uid;
		private String roomId;
		MajiaGetinRoom(String uid,String roomId){
			this.uid = uid;
			this.roomId = roomId;
		}
		@Override
		public void run() {
			while(true){
				CountDownLatch countDown = new CountDownLatch(1);
				try{
					Double scoreValue = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
					if(scoreValue==null){
						logger.info("majia getin room stoped.user:{} live end in room:{}",uid,roomId);
						break;
					}
					
					String liveSetting = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "liveSetting");
					if(StringUtils.isNotBlank(liveSetting) && !"-1".equals(liveSetting)){
						Map<String,String> settings = jedisService.getMapByKey(BicycleConstants.LIVE_SETTING+liveSetting);
						logger.info("live setting for user{},setting:{}",uid,settings);
						
						if(settings!=null && !settings.isEmpty() && "0".equals(settings.get("status"))){
							String majiaperiod = settings.get("majiaPeriod");
							int period = Integer.parseInt(majiaperiod);
							String maxMajiaperiod = settings.get("maxMajiaPeriod");
							int maxPeriod = Integer.parseInt(maxMajiaperiod);
							int c = r.nextInt(maxPeriod-period);
							period = period + c;
							logger.info("majia getin room wait {} second,user:{} room:{}",period,uid,roomId);
							countDown.await(period, TimeUnit.SECONDS);
							
							Set<String> majiaIds = jedisService.getSetFromShard(BicycleConstants.ROOM_MAJIAS+roomId);
							String majiaCount = jedisService.getValueFromMap(BicycleConstants.LIVE_SETTING+liveSetting, "majiaCount");
							int max = Integer.parseInt(majiaCount);
							if(max !=-1 && majiaIds.size()>=max){
								logger.info("user {} room majia size {} more or equal the max {}",uid,majiaIds.size(),majiaCount);
								countDown.await(30, TimeUnit.SECONDS);
								continue;
							}
							
							String majiaId = jedisService.getSetRanomMembers(BicycleConstants.MAJIA_UID);
							if(majiaIds.contains(majiaId)){//同一马甲不重复进入房间
								logger.info("user {} room majia id {} was duplicate ",uid,majiaId);
								countDown.countDown();
								continue;
							}
							logger.info("majia getin room excuted,user:{} room:{}",majiaId,roomId);
							RoomUtil.sendUserAdd(majiaId, roomId);
							jedisService.setValueToSetInShard(BicycleConstants.ROOM_MAJIAS+roomId, majiaId);
							RoomUtil.incUserCount(roomId);//.updateUserCount(roomId,true);//增加房间人数
							
							countDown.countDown();
						}
					}
					if(countDown.getCount()==1){
						countDown.await(5, TimeUnit.SECONDS);//至少等待5秒循环一次
						countDown.countDown();
					}
				}catch(Exception ex){
					ex.printStackTrace();
					logger.error("auto access majia exception.uid:"+uid, ex);
					countDown.countDown();
				}
			}			
		}
		
	}
	/**
	 * 房间自动添加人数、自动同步房间人数
	 * @author dmsong
	 *
	 */
	class AddUserCount implements Runnable{
		private String uid;
		private String roomId;
		AddUserCount(String uid,String roomId){
			this.uid = uid;
			this.roomId = roomId;
		}
		@Override
		public void run() {
			while(true){
				CountDownLatch countDown = new CountDownLatch(1);
				try{
					Double scoreValue = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
					if(scoreValue==null){
						logger.info("add user count in room stoped.user:{} live end in room:{}",uid,roomId);
						break;
					}
					
					String liveSetting = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "liveSetting");
					if(StringUtils.isNotBlank(liveSetting) && !"-1".equals(liveSetting)){
						Map<String,String> settings = jedisService.getMapByKey(BicycleConstants.LIVE_SETTING+liveSetting);
						logger.info("live setting for user count {},setting:{}",uid,settings);
						if(settings!=null && !settings.isEmpty() && "0".equals(settings.get("status")) && "0".equals(settings.get("userCountStat"))){
							String maxCount = settings.get("maxUserCount");
							String onlineNum = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "onlineNumber");
							if(onlineNum == null || Integer.parseInt(onlineNum)>Integer.parseInt(maxCount)){
								logger.info("加数到最大值,停止加数.room:{},user:{}",roomId,uid);
								countDown.await(30, TimeUnit.SECONDS);
								continue;
							}
							Set<String> majiaIds = jedisService.getSetFromShard(BicycleConstants.ROOM_MAJIAS+roomId);
							if(majiaIds==null || majiaIds.size()==0){
								logger.info("马甲数未达到加数阈值.room:{},user:{}",roomId,uid);
								countDown.await(30, TimeUnit.SECONDS);
								continue;
							}
							String countbegin = settings.get("majiaCountBegin");
							if(countbegin != null && majiaIds.size()>Integer.parseInt(countbegin)){
								String countStep = settings.get("userCountStep");
								int step = Integer.parseInt(countStep);
								String countPeriod = settings.get("userCountPeriod");
								String maxCountPeriod = settings.get("maxUserCountPeriod");
								int period = Integer.parseInt(countPeriod);
								int maxPeriod = Integer.parseInt(maxCountPeriod);
								int c = r.nextInt(maxPeriod-period);
								period = period + c;
								logger.info("add room user count wait {} second.room:{},user:{}",period,roomId,uid);
								countDown.await(period,TimeUnit.SECONDS);
								
								jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "onlineNumber",step);
								jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "maxAccessNumber",step);
								logger.info("add room user count ok.room:{},user:{}",period,roomId,uid);
//								RoomUtil.sendRoomUserCount(roomId);
								
								countDown.countDown();
							}
						}
					}
					if(countDown.getCount()==1){
						countDown.await(30, TimeUnit.SECONDS);
						logger.info("syincornize room user count.user:{} room:{}",uid,roomId);
//						RoomUtil.sendRoomUserCount(roomId);
						countDown.countDown();
					}
				}catch(Exception ex){
					ex.printStackTrace();
					logger.error("auto majia count exception.uid:"+uid, ex);
					countDown.countDown();
				}
			}			
		}
	}
	/**
	 * 贡献榜前三名自动推送
	 * @author dmsong
	 *
	 */
	class ContributionTop3 implements Runnable{
		private String uid;
		private String roomId;
		ContributionTop3(String uid,String roomId){
			this.uid = uid;
			this.roomId = roomId;
		}
		@Override
		public void run() {
			while(true){
				CountDownLatch countDown = new CountDownLatch(1);
				try{
					Double scoreValue = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
					if(scoreValue==null){
						logger.info("send ContributionTop3 in room stoped.user:{} live end in room:{}",uid,roomId);
						break;
					}
					
					Set<Tuple> idWithScores = jedisService.zrevrangeWithScores(BicycleConstants.LIVE_CONTRIBUTION_LIST + uid, 0l, 2l);
					if(idWithScores!=null && idWithScores.size()>0){
						List<Map<String,String>> result = new ArrayList<Map<String,String>>();
						for(Tuple tup:idWithScores){
							String member = tup.getElement();
							
							Double score = tup.getScore();
							Map<String,String> info = jedisService.getMapByKey(BicycleConstants.USER_INFO+member);
							Map<String,String> user = new HashMap<String,String>();
							user.put("id", member);
							user.put("name", info.get("name"));
							user.put("username", info.get("username"));
							user.put("signature", info.get("signature"));
							user.put("vipStat", info.get("vipStat"));
							user.put("pic", info.get("pic"));
							user.put("score", score.longValue()+"");
							
							result.add(user);
							
						}
						
						WsMessage m = new WsMessage();
						m.setCode(MessageConst.PROPS);
						m.setChildCode(MessageConst.PROPS_CONTRIBUTION);
						m.setRoomId(roomId);
						m.getExtra().put("contribution", result);
						
//						RoomUtil.sendToRoom(m,clientId);
						MessageMergeUtil.addMessage(roomId, m);
						
						countDown.await(30, TimeUnit.SECONDS);
						countDown.countDown();
					}
				}catch(Exception ex){
					ex.printStackTrace();
					logger.error("推送贡献榜前三名错误", ex);
					countDown.countDown();
				}
			}			
		}
	}
}
