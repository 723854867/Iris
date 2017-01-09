//package com.busap.vcs.chat.service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.Set;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import redis.clients.jedis.Tuple;
//
//import com.busap.vcs.base.WsMessage;
//import com.busap.vcs.chat.util.ChatUtil;
//import com.busap.vcs.chat.util.MessUtil;
//import com.busap.vcs.chat.util.SpringUtils;
//import com.busap.vcs.constants.BicycleConstants;
//import com.busap.vcs.constants.MessageConst;
//
//public class LiveThreadService {
//	private static final Logger logger = LoggerFactory.getLogger(LiveThreadService.class);
//	//线程池
//	private ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
//	
//	private String roomId;//房间Id
//	private String uid;//主播Id
//	private JedisService jedisService;
//	
//	public LiveThreadService(String roomId,String uid){
//		this.setRoomId(roomId);
//		this.setUid(uid);
//		jedisService = (JedisService)SpringUtils.getBean("jedisService");
//	}
//	
//	public void run(){
//		logger.debug("room {} live started.thread pool in service.",this.roomId);
//		final Random r = new Random();
//		//机器人自动进房间
//		service.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				try{
//					CountDownLatch countDown = new CountDownLatch(1);
//					String liveSetting = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "liveSetting");
//					if(StringUtils.isNotBlank(liveSetting) && !"-1".equals(liveSetting)){
//						Map<String,String> settings = jedisService.getMapByKey(BicycleConstants.LIVE_SETTING+liveSetting);
//						logger.info("live setting for user{},setting:{}",uid,settings);
//						
//						if(settings!=null && !settings.isEmpty() && "0".equals(settings.get("status"))){
//							String majiaperiod = settings.get("majiaPeriod");
//							int period = Integer.parseInt(majiaperiod);
//							String maxMajiaperiod = settings.get("maxMajiaPeriod");
//							int maxPeriod = Integer.parseInt(maxMajiaperiod);
//							int c = r.nextInt(maxPeriod-period);
//							period = period + c;
//							countDown.await(period, TimeUnit.SECONDS);
//							
//							String majiaCount = jedisService.getValueFromMap(BicycleConstants.LIVE_SETTING+liveSetting, "majiaCount");
//							int max = Integer.parseInt(majiaCount);
//							if(max !=-1 && jedisService.scard(BicycleConstants.ROOM_MAJIAS+roomId)>=max){
//								logger.info("user {} room majia size more or equal the max {}",uid,majiaCount);
//								return;
//							}
//							
//							String majiaId = jedisService.getSetRanomMembers(BicycleConstants.MAJIA_UID);
//							if(jedisService.isSetMemberInShard(BicycleConstants.ROOM_MAJIAS+roomId, majiaId)){//同一马甲不重复进入房间
//								logger.info("user {} room majia id {} was duplicate ",uid,majiaId);
//								return;
//							}
//							
//							Map<String,String> majiaInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+majiaId);
//							if(majiaInfo!=null && !majiaInfo.isEmpty()){
//								majiaInfo.put("isMajia", "1");
//								MessUtil.sendUserAdd(majiaId, roomId, majiaInfo);
//								jedisService.setValueToSetInShard(BicycleConstants.ROOM_MAJIAS+roomId, majiaId);
//								ChatUtil.incUserCount(roomId);//.updateUserCount(roomId,true);//增加房间人数
//							}
//							
//						}
//					}
//				}catch(Exception ex){
//					logger.error("auto access majia exception.uid:"+uid, ex);
//				}
//			}
//		},10,5,TimeUnit.SECONDS);
//		
//		//自动给房间加人数
//		service.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				logger.info("add user count start in room:{},uid:{}",roomId,uid);
//				try{
//					CountDownLatch countDown = new CountDownLatch(1);
//					String liveSetting = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "liveSetting");
//					if(StringUtils.isNotBlank(liveSetting) && !"-1".equals(liveSetting)){
//						Map<String,String> settings = jedisService.getMapByKey(BicycleConstants.LIVE_SETTING+liveSetting);
//						logger.info("live setting for user count {},setting:{}",uid,settings);
//						if(settings!=null && !settings.isEmpty() && "0".equals(settings.get("status")) && "0".equals(settings.get("userCountStat"))){
//							String maxCount = settings.get("maxUserCount");
//							String onlineNum = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "onlineNumber");
//							if(onlineNum == null || Integer.parseInt(onlineNum)>Integer.parseInt(maxCount)){
//								logger.info("加数到最大值,停止加数.room:{},user:{}",roomId,uid);
//								return;
//							}
////							Set<String> majiaIds = LiveUtil.roomMajias.get(roomId);
////							if(majiaIds==null || majiaIds.size()==0){
////								logger.info("马甲数未达到加数阈值.room:{},user:{}",roomId,uid);
////								return;
////							}
//							String countbegin = settings.get("majiaCountBegin");
//							if(countbegin != null && jedisService.scard(BicycleConstants.ROOM_MAJIAS+roomId)>Integer.parseInt(countbegin)){
//								String countStep = settings.get("userCountStep");
//								int step = Integer.parseInt(countStep);
//								String countPeriod = settings.get("userCountPeriod");
//								String maxCountPeriod = settings.get("maxUserCountPeriod");
//								int period = Integer.parseInt(countPeriod);
//								int maxPeriod = Integer.parseInt(maxCountPeriod);
//								int c = r.nextInt(maxPeriod-period);
//								period = period + c;
//								
//								countDown.await(period,TimeUnit.SECONDS);
//								
//								jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "onlineNumber",step);
//								jedisService.getIncrValueFromMap(BicycleConstants.ROOM_+roomId, "maxAccessNumber",step);
//								
//								MessUtil.sendRoomUserCount(roomId,null);
//							}
//						}
//					}
//				}catch(Exception ex){
//					logger.error("auto majia count exception.uid:"+uid, ex);
//				}
//			}
//		},10,10,TimeUnit.SECONDS);
//
//		//自动发言		
//		service.scheduleWithFixedDelay(new Runnable() {
//			@Override
//			public void run() {
//				try{
//					logger.info("start auto chat");
//					String liveSetting = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "liveSetting");
//					if(StringUtils.isNotBlank(liveSetting) && !"-1".equals(liveSetting)){
//						Map<String,String> settings = jedisService.getMapByKey(BicycleConstants.LIVE_SETTING+liveSetting);
//						logger.info("live setting for user{},setting:{}",uid,settings);
//						
//						if(settings!=null && !settings.isEmpty() && "0".equals(settings.get("status"))){
//							String typeid = settings.get("typeId");
//						
//							if(jedisService.scard(BicycleConstants.ROOM_MAJIAS+roomId)==0){
//								return;
//							}
//							CountDownLatch countDown = new CountDownLatch(1);
//							int i = 0;
//							while(i<3){
//								logger.info("has majia,excute:{}",i);
//								i++;
//								Set<String> majiaIds = jedisService.getSetFromShard(BicycleConstants.ROOM_MAJIAS+roomId);
//								String majiaId = null;
//								Integer size = majiaIds.size();
//								Random r = new Random();
//								int t = r.nextInt(size);
//								
//								for(String majia:majiaIds){
//									t--;
//									if(t==0){
//										majiaId = majia;
//										break;
//									}
//								}
//								logger.info("random majiaId:{}",majiaId);
//								if(StringUtils.isBlank(majiaId)){
//									continue;
//								}
//								String word = null;
//								if(jedisService.ifKeyExists(BicycleConstants.AUTOCHAT_WORDS+uid)){
//									int count = (int)jedisService.zCount(BicycleConstants.AUTOCHAT_WORDS+uid, Integer.MAX_VALUE, 0);
//									int range = r.nextInt(count);
//									Set<String> result = jedisService.getSortedSetFromShardByDesc(BicycleConstants.AUTOCHAT_WORDS+uid, range, range);
//									if(result != null){
//										for(String chat:result){
//											word = chat;
//											break;
//										}
//									}
//									
//									logger.info("get word by uid:{}",word);
//								}else if(jedisService.ifKeyExists(BicycleConstants.AUTOCHAT_WORDS+typeid)){
//									int count = (int)jedisService.zCount(BicycleConstants.AUTOCHAT_WORDS+typeid, Integer.MAX_VALUE, 0);
//									int range = r.nextInt(count);
//									Set<String> result = jedisService.getSortedSetFromShardByDesc(BicycleConstants.AUTOCHAT_WORDS+typeid, range, range);
//									if(result != null){
//										for(String chat:result){
//											word = chat;
//											break;
//										}
//									}
//									
//									logger.info("get word by typeId:{}",word);
//								}else{
//									continue;
//								}
//								String key = BicycleConstants.USED_AUTOCHAT_WORDS+roomId+"_"+majiaId+"_"+word;
//								if(jedisService.ifKeyExists(key)){
//									logger.info("send word exists ,key：{}",key);
//									continue;
//								}
//								int await = r.nextInt(50);
//								logger.info("thread await {} seconds.",await);
//								countDown.await(await, TimeUnit.SECONDS);//
//								
//								logger.info("start build message.majia:{}",majiaId);
//								WsMessage m = new WsMessage();
//								m.setChildCode(MessageConst.CHAT_PUBLIC);
//								m.setCode(MessageConst.CHAT);
//								m.setRoomId(roomId);
//								m.setSenderId(majiaId);
//								m.setContent(word);
//								Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+majiaId);
//								m.setSenderName(userInfo.get("name"));
//								m.getExtra().put("userid", majiaId);
//								m.getExtra().put("sex", userInfo.get("sex"));
//								m.getExtra().put("name", userInfo.get("name"));
//								m.getExtra().put("username", userInfo.get("name"));
//								m.getExtra().put("signature", userInfo.get("signature"));
//								m.getExtra().put("vipStat", userInfo.get("vipStat"));
//								m.getExtra().put("pic", userInfo.get("pic"));
//								
//								MessUtil.sendToMessageQueue(m);
//								logger.info("majia send message,mgId:{} roomId:{} words:{}",majiaId,roomId,word);
//								jedisService.set(key, "1");
//								jedisService.expire(key, 5);
//							}
//						}
//					}
//				}catch(Exception ex){
//					logger.error("send autochat error."+ex.getMessage(), ex);
//				}
//			}
//		
//		},20,10,TimeUnit.SECONDS);
//			
//
//		
//		
////		//机器人自动点赞
////		service.scheduleAtFixedRate(new Runnable() {
////			@Override
////			public void run() {
////				try{
////					WsMessage m = new WsMessage();
////					m.setCode(MessageConst.CHAT);
////					m.setChildCode(MessageConst.CHAT_PRAISE);
////					m.setRoomId(roomId);
////					Double scoreValue = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
////					if(scoreValue!=null && scoreValue>=0){//不在线房间不更新
////						Random r = new Random();
////						int t = r.nextInt(5) + 5;//30-50 20160301
////						
////						for(int i=0;i<t;i++){
////							MessUtil.sendToMessageQueue(roomId, m);
////						}
////						
////						jedisService.getIncrValueFromMap(BicycleConstants.ROOM_ + roomId, "mjPraiseNumber", t);//.setValueToMap(BicycleConstants.ROOM_ + roomId, "mjPraiseNumber", praise.toString());
////					} else {
////						service.shutdownNow();
////						logger.info("房间{}已下线，关闭房间不存在的服务",roomId);
////					}
////				}catch(Exception ex){
////					logger.error("自动点赞错误", ex);
////				}
////			}
////		},10,2,TimeUnit.SECONDS);
//		
//		//推送贡献榜前三名
//		service.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				try{
//					Set<Tuple> idWithScores = jedisService.zrevrangeWithScores(BicycleConstants.LIVE_CONTRIBUTION_LIST + uid, 0l, 2l);
//					if(idWithScores!=null && idWithScores.size()>0){
//						List<Map<String,String>> result = new ArrayList<Map<String,String>>();
//						for(Tuple tup:idWithScores){
//							String member = tup.getElement();
//							
//							Double score = tup.getScore();
//							Map<String,String> info = jedisService.getMapByKey(BicycleConstants.USER_INFO+member);
//							Map<String,String> user = new HashMap<String,String>();
//							user.put("id", member);
//							user.put("name", info.get("name"));
//							user.put("username", info.get("username"));
//							user.put("signature", info.get("signature"));
//							user.put("vipStat", info.get("vipStat"));
//							user.put("pic", info.get("pic"));
//							user.put("score", score.longValue()+"");
//							
//							result.add(user);
//							
//						}
//						
//						WsMessage m = new WsMessage();
//						m.setCode(MessageConst.PROPS);
//						m.setChildCode(MessageConst.PROPS_CONTRIBUTION);
//						m.setRoomId(roomId);
//						m.getExtra().put("contribution", result);
//						
//						MessUtil.sendToMessageQueue(m);;
//					}
//				}catch(Exception ex){
//					logger.error("推送贡献榜前三名错误", ex);
//				}
//			}
//		},20,30,TimeUnit.SECONDS	);
//		
//		// 推送网警提示
//		service.scheduleAtFixedRate(new Runnable() {
//			@Override
//			public void run() {
//				try{
//					String notice = jedisService.get("LOOP_LIVE_NOTICE");
//					if(StringUtils.isNotBlank(notice)){
//						MessUtil.sendLoopNotice(roomId,notice,"提示");
//					}	
//				}catch(Exception ex){
//					logger.error("loop live notice error.", ex);
//				}
//			}
//		}, 5,5,TimeUnit.MINUTES);
//		
//	}
//
//	/**
//	 * 更新房间热度(直播权重+在线人数+点赞数/100)
//	 * @param roomId
//	 */
//	public void updateUserCount(String roomId) {
//		Double scoreValue = jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId);
//		if(scoreValue!=null && scoreValue>=0){//房间已经下线，数据不再更新
//			String online = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "onlineNumber");
//			String additional = jedisService.getValueFromMap(BicycleConstants.ROOM_+roomId, "additionalNumber");
//			
//			Integer onlineNum = 0;
//			if(StringUtils.isNumeric(online)){
//				onlineNum = Integer.parseInt(online);
//			}
//			
//			double addNum = 0;
//			if(StringUtils.isNumeric(additional)){
//				addNum = Double.parseDouble(additional);
//			}
//			
//			double score = addNum + onlineNum.doubleValue();// + praiseNumber.doubleValue()/100;
//			jedisService.setValueToSortedSetInShard(BicycleConstants.ROOM_ORDER, score, roomId);
//		}
//	}
//	
//	public void stop(){
//		List<Runnable> excutingThreads = service.shutdownNow();
//		try {
//			while (!service.awaitTermination(1, TimeUnit.MILLISECONDS)) {  
//			    logger.info("线程池没有关闭,room{} user{}",this.roomId,this.uid);  
//			}  
//			logger.info("线程池已经关闭,room{} user{}",this.roomId,this.uid);  
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		logger.info("threadpool shutdown in room[{}],has {} excuting task.",this.roomId,excutingThreads==null?0:excutingThreads.size());
//	}
//
//	public String getRoomId() {
//		return roomId;
//	}
//
//	public void setRoomId(String roomId) {
//		this.roomId = roomId;
//	}
//
//	public String getUid() {
//		return uid;
//	}
//
//	public void setUid(String uid) {
//		this.uid = uid;
//	}
//}
