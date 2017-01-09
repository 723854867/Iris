package com.busap.vcs.service.impl;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.busap.vcs.data.model.OrganizationAnchorDisplay;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.base.Action;
import com.busap.vcs.base.Filter;
import com.busap.vcs.base.Message;
import com.busap.vcs.base.Module;
import com.busap.vcs.base.WsMessage;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.ConsumeRecord;
import com.busap.vcs.data.entity.Label;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.enums.VideoStatus;
import com.busap.vcs.data.mapper.RoomDao;
import com.busap.vcs.data.mapper.RuserDAO;
import com.busap.vcs.data.mapper.VideoDAO;
import com.busap.vcs.data.model.LiveDayDetailDisplay;
import com.busap.vcs.data.model.LiveDetailDisplay;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.ConsumeRecordRepository;
import com.busap.vcs.data.repository.LabelRepository;
import com.busap.vcs.data.repository.RoomRepository;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import com.busap.vcs.service.kafka.producer.WsMessageProducer;
import com.busap.vcs.service.utils.QiniuUtil;
import com.pili.Stream;

@Service("roomService")
public class RoomServiceImpl extends BaseServiceImpl<Room, Long> implements
		RoomService {
	
	private Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

	@Resource(name="roomRepository")
	private RoomRepository roomRepository;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name="attentionService")
	private AttentionService attentionService;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name="videoService") 
    private VideoService videoService; 
	
	@Resource(name = "solrRoomService")
	private SolrRoomService solrRoomService;
	
	@Resource(name="wsMessageProducer")
	private WsMessageProducer wsMessageProducer;
	
	@Resource(name = "consumeRecordRepository")
    private ConsumeRecordRepository consumeRecordRepository;
	
	@Resource
    private VideoDAO videoDAO;
	
	@Autowired
	private RuserDAO ruserDAO;
	
	@Resource(name = "anchorService")
	private AnchorService anchorService;
	
	@Autowired
    KafkaProducer kafkaProducer;
	
	@Resource(name="labelRepository")
	private LabelRepository labelRepository;
	
	@Autowired
	private SolrWoPaiTagService solrWoPaiTagService;
	
	@Resource(name = "solrWoPaiLiveTagService")
	private SolrWoPaiLiveTagService solrWoPaiLiveTagService;

	@Resource
	private RoomDao roomDao;

	@Resource(name="roomRepository")
	@Override
	public void setBaseRepository(BaseRepository<Room, Long> baseRepository) {
		super.setBaseRepository(roomRepository);
	}

	@Override
	public List<Map<String,String>> getRoomList(Integer page, Integer size, Integer isLive,
			String userId) {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if (isLive == 1) { //直播中的房间信息，去redis查
			Set<String> roomIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER, (page-1)*size, page*size-1);
			for (String roomId : roomIds) {  
				try {
					Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);
					if (room != null && room.size() > 0 && room.get("id") != null) {
						//计算直播时长
						Long duration = System.currentTimeMillis() - Long.parseLong(room.get("createDate")); 
						room.put("duration", String.valueOf(duration < 0?0:duration));
						//查询用户信息
						Ruser ruser = ruserService.find(Long.parseLong(room.get("creatorId")));
						room.put("anchorName", ruser.getName());
						room.put("anchorPic", ruser.getPic());
						room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
						room.put("anchorSignature", ruser.getSignature());
						room.put("anchorSex", ruser.getSex());
						room.put("type","1");
						if (StringUtils.isBlank(room.get("onlineNumber"))) {
							room.put("onlineNumber", "0");
						} else {
							Long onlineNumber = Long.parseLong(room.get("onlineNumber"));
							if (onlineNumber < 0) {
								room.put("onlineNumber", "0");
							}
						}
						Boolean userShield = jedisService.ifKeyExists(BicycleConstants.SHIELD_LIVE_USER+room.get("creatorId"));
						if(!userShield) {
							list.add(room);
						}
					}
				} catch (Exception e) {
					logger.error("getRoomList error:roomId is " + roomId);
				}
			}  
		} else {
			List<Room> roomList = roomRepository.getRoomByStatus(0, (page-1)*size,size);
			for (Room roomEntity:roomList) {
				Map<String,String> room = new HashMap<String,String>();
				Ruser ruser = ruserService.find(roomEntity.getCreatorId());
				room.put("roomPic", roomEntity.getRoomPic());
				room.put("duration",String.valueOf(roomEntity.getDuration()));
				room.put("maxAccessNumber",String.valueOf(roomEntity.getMaxAccessNumber()));
				room.put("praiseNumber", String.valueOf(roomEntity.getPraiseNumber()));
				room.put("mjPraiseNumber", String.valueOf(roomEntity.getMjPraiseNumber()));
				room.put("anchorName", ruser.getName());
				room.put("anchorPic", ruser.getPic());
				room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
				room.put("anchorSignature", ruser.getSignature());
				room.put("creatorId", String.valueOf(roomEntity.getCreatorId()));
				room.put("anchorSex", ruser.getSex());
				list.add(room);
			}
		}
		return list;
	}
	
	@Override
	public List<Map<String, String>> getRoomList(Long activityId,Integer page, Integer size) {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		long count = jedisService.zCard(BicycleConstants.ROOM_ORDER);
		int start = (page-1)*size;
		if(count <= start){
			return list;
		}
		Set<String> roomIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER, 0, -1);
		for (String roomId : roomIds) {  
			try {
				Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);
				if (room != null && room.size() > 0 && room.get("id") != null) {
					if(activityId != null && StringUtils.isNotBlank(room.get("liveActivityId")) && String.valueOf(activityId).equals(room.get("liveActivityId"))){
						//计算直播时长
						Long duration = System.currentTimeMillis() - Long.parseLong(room.get("createDate")); 
						room.put("duration", String.valueOf(duration < 0?0:duration));
						//查询用户信息
						Ruser ruser = ruserService.find(Long.parseLong(room.get("creatorId")));
						room.put("anchorName", ruser.getName());
						room.put("anchorPic", ruser.getPic());
						room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
						room.put("anchorSignature", ruser.getSignature());
						room.put("anchorSex", ruser.getSex());
						room.put("type","1");
						if (StringUtils.isBlank(room.get("onlineNumber"))) {
							room.put("onlineNumber", "0");
						} else {
							Long onlineNumber = Long.parseLong(room.get("onlineNumber"));
							if (onlineNumber < 0) {
								room.put("onlineNumber", "0");
							}
						}
						
						list.add(room);
					}
				}
			} catch (Exception e) {
				logger.error("getRoomList error:roomId is " + roomId);
			}
		}  
		
		Integer lsize = list.size();
		if(lsize<=start){
			return null;
		}
		
		Integer end = start+size;
		if(lsize<end){
			end = lsize;
		}
		
		Collections.sort(list, new Comparator<Map<String,String>>(){

			@Override
			public int compare(Map<String, String> o1, Map<String, String> o2) {
				Long create1 = jedisService.zrank(BicycleConstants.ROOM_ORDER, o1.get("id"));
				 
				Long create2 = jedisService.zrank(BicycleConstants.ROOM_ORDER, o2.get("id"));
				
				return create2.compareTo(create1);
			}
			
		});
		list = list.subList(start, end);
		
		return list;
	}

/*	@Override
	public Integer queryDailyLiveNum(String date) {
		 return roomRepository.queryDailyLiveNum(date + " 00:00:00", date + "23:59:59");
	}*/

	@Override
	public Map<String, String> createRoom(String title, String pic, Long uid,Long liveActivityId,Integer liveType,boolean canChangeCDN,String localOutIp,String appVersion,String platform,String channel,String longitude,String latitude,String area,boolean canLinkMic) {
		//如果主播信息不存在，创建主播信息
		Anchor anchor = anchorService.getAnchorByUserid(uid);
		if ( anchor == null) { //没有通过主播认证，依靠v的身份进行直播（主要是为了兼容老版本）
			anchor = new Anchor();
			anchor.setCreatorId(uid);
			anchor.setQiniuStreamId("");
			anchor.setStatus(-2); //
			anchor.setCertificateType(1);
			anchorService.save(anchor);
		}
		
		//创建房间信息
		Room room = new Room();
		room.setLiveType(liveType);
		room.setCreateDate(roomRepository.getDbTime());
		room.setStatus(1);
		room.setTitle(title);
		room.setRoomPic(pic);
		room.setAdditionalNumber(0);
		room.setCreatorId(uid);
		room.setLocalOutIp(localOutIp);
		Map<String,String> streamInfo = getSteamInfo(String.valueOf(uid),canChangeCDN,localOutIp);
		room.setStreamJson(streamInfo.get("streamJson"));
		room.setRtmpLiveUrl(streamInfo.get("rtmpLiveUrl"));
		room.setHlsLiveUrl(streamInfo.get("hlsLiveUrl"));
		room.setLiveActivityId(liveActivityId);
		room.setAppVersion(appVersion);
		room.setPlatform(platform);
		room.setChannel(channel);
		room.setLongitude(longitude);
		room.setLatitude(latitude);
		room.setArea(area);
		room.setCanLinkMic(canLinkMic?1:0);
		room.setLinkMicPushUrl(MessageFormat.format(jedisService.get(BicycleConstants.LINK_MIC_PUSH_MAIN), streamInfo.get("streamId")));
		room.setStreamId(streamInfo.get("streamId"));
		roomRepository.save(room);
		
		if (room != null && room.getId() != null) {  //创建房间成功，将房间信息放到redis中
			jedisService.saveAsMap(BicycleConstants.ROOM_+ room.getId(), room);

			Map<String,String> resultMap = jedisService.getMapByKey(BicycleConstants.ROOM_+ room.getId());
			
			String flag = jedisService.get(BicycleConstants.LIVE_PUSH_SWITCH);
			
			if (flag != null && "1".equals(flag)) {
				if (resultMap != null && resultMap.size()>=0){  //创建房间成功，米push通知粉丝
					List<Long> fansIds = attentionService.selectAllFansIdWithoutMajia(uid);
					Message msg = new Message();
					msg.setModule(Module.LIVE);
					msg.setAction(Action.INSERT);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("uid", String.valueOf(uid));
					map.put("fansId", fansIds);
					map.put("roomId", String.valueOf(room.getId()));
					map.put("rtmpLiveUrl", room.getRtmpLiveUrl());
					msg.setDataMap(map);
			    	kafkaProducer.send("push-msg-topic", msg);
				}
			}
			
			//计算每天最大同时直播主播人数
			Long current = jedisService.zCard(BicycleConstants.ROOM_ORDER);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = format.format(roomRepository.getDbTime());
			String max = jedisService.get(BicycleConstants.MAX_ANCHOR_BY_DAY_+dateString);
			if (max != null && !"".equals(max)) {
				if (current+1>Long.parseLong(max)) {
					jedisService.set(BicycleConstants.MAX_ANCHOR_BY_DAY_+dateString, String.valueOf(current+1));
				}
			} else {
				jedisService.set(BicycleConstants.MAX_ANCHOR_BY_DAY_+dateString, String.valueOf(current+1));
			}
			
			return resultMap;
		}
		return null;
	}
	
	@Override
	public List<Map<String, String>> getRoomUser(String roomId, int page,int size,String uid) {
		if(page == 0)
			page = 1;
		if(size == 0)
			size = 100;
		int start = (page-1) * size;
		int end = page * size;
//		Set<String> set = jedisService.getSetFromShard(BicycleConstants.ROOM_USERS+roomId);
		Set<String> set = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_USERS+roomId, start, end);
		List<Map<String, String>> resultList = new ArrayList<Map<String,String>>();
		if (set !=null && set.size() >0 ){
//			List<Filter> ls = new ArrayList<Filter>(1);
//			ls.add(new Filter("id", "in", set));
//			Pageable pr = new PageRequest(page, size);
//			Page<Ruser> p = ruserService.findAll(pr, ls);
//			List<Ruser> list= p.getContent();
	    	for(String userId:set){
	    		Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
	    		if(userInfo != null && !userInfo.isEmpty()){
		    		Map<String, String> map = new HashMap<String, String>();
		    		map.put("id", userInfo.get("id"));
		    		map.put("name", userInfo.get("name"));
		    		map.put("username", userInfo.get("username"));
		    		map.put("signature", userInfo.get("signature"));
		    		map.put("vipStat", userInfo.get("vipStat"));
		    		map.put("pic", userInfo.get("pic"));
		    		map.put("sex", userInfo.get("sex"));
		    		if("majia".equals(userInfo.get("type"))){
		    			map.put("isMajia", "1");
		    		} else if(jedisService.isSetMemberInShard(BicycleConstants.ROOM_USERS_TOPLIST, userId)) {
		    			map.put("isTop", "1");
		    		}
		    		
		    		Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+roomId);
		    		if (room  != null && room.size() >0 && room.get("id") != null){
		    			if (room.get("creatorId").equals(userInfo.get("id"))){
		    				map.put("isAnchor", "1");
		    			} else {
		    				map.put("isAnchor", "0");
		    			}
		    		} else {
		    			map.put("isAnchor", "0");
		    		}
		    		if (uid != null && !"".equals(uid)){
		    			map.put("isAttention", String.valueOf(attentionService.isAttention(Long.parseLong(uid), Long.parseLong(userInfo.get("id")))));
		    		} else {
		    			map.put("isAttention", "0");
		    		}
		    		resultList.add(map);
	    		}
	    	}
		}
		return resultList;
	}

	@Override
	public void destroyRoom(String roomId) {
		Map<String,String> roomCache = jedisService.getMapByKey(BicycleConstants.ROOM_+roomId);
		if (roomCache != null && roomCache.size()>0 && roomCache.get("id") != null){
			Room room = find(Long.parseLong(roomId));
			if (room != null) {
				//将缓存中的房间信息更新到数据库
				Date finishTime = roomRepository.getDbTime();
				room.setModifyDate(new Date());
				
				//获得缓存中的结束时间和时长
				String cacheFinishDate = roomCache.get("finishDate");
				String cacheDuration = roomCache.get("duration");
				
				if (cacheFinishDate != null && !"".equals(cacheFinishDate)) {
					room.setFinishDate(new Date(Long.parseLong(cacheFinishDate)));
				} else {
					if (finishTime.getTime()-room.getCreateDate() >1000*60*60*2) {
						room.setFinishDate(new Date(room.getCreateDate()+1000*60*60*2)); //结束时间没有的，且大于两小时的，默认按开播两小时计算
					} else {
						room.setFinishDate(finishTime); 
					}
				}
				
				if (cacheDuration != null && !"".equals(cacheDuration)) {
					room.setDuration(Long.parseLong(cacheDuration));
				} else {
					String praiseNumber = roomCache.get("praiseNumber");
					if (praiseNumber != null && !"".equals(praiseNumber) && Integer.parseInt(praiseNumber)>0){//根据点赞数判断是否连上聊天服务器
						if (finishTime.getTime()-room.getCreateDate() >1000*60*60*2) {
							room.setDuration(1000*60*60*2l);//结束时间没有的，且大于两小时的，默认按开播两小时计算
						} else {
							room.setDuration(finishTime.getTime()-room.getCreateDate()); 
						}
						
					} else {
						room.setDuration(0l);
					}
				}
				room.setChatCount(StringUtils.isNotBlank(roomCache.get("chatCount"))?Integer.parseInt(roomCache.get("chatCount")):0);
				room.setAnchorChatCount(StringUtils.isNotBlank(roomCache.get("anchorChatCount"))?Integer.parseInt(roomCache.get("anchorChatCount")):0);
				room.setMaxAccessNumber(Integer.parseInt(roomCache.get("maxAccessNumber")));
				room.setNormalUserCount(StringUtils.isNotBlank(roomCache.get("normalUserCount"))?Integer.parseInt(roomCache.get("normalUserCount")):0);
				room.setPraiseNumber(Integer.parseInt(roomCache.get("praiseNumber")));
				room.setMjPraiseNumber(Integer.parseInt(roomCache.get("mjPraiseNumber")));
				room.setStatus(0);
				room.setUv(StringUtils.isNotBlank(roomCache.get("UV"))?Integer.parseInt(roomCache.get("UV")):0);
				//更新本场的收到的礼物数和收到的金豆数
				String giftNumberStr = jedisService.get(BicycleConstants.GIFT_NUMBER_BY_ROOM_+roomId);
				String pointNumberStr = jedisService.get(BicycleConstants.POINT_NUMBER_BY_ROOM_+roomId);
				if (giftNumberStr == null || "".equals(giftNumberStr)) {
					room.setGiftNumber(0);
				} else {
					room.setGiftNumber(Integer.parseInt(giftNumberStr));
				}
				if (pointNumberStr == null || "".equals(pointNumberStr)) {
					room.setPointNumber(0);
				} else {
					room.setPointNumber(Integer.parseInt(pointNumberStr));
				}
				this.update(room);
				jedisService.delete(BicycleConstants.GIFT_NUMBER_BY_ROOM_+roomId);
				jedisService.delete(BicycleConstants.POINT_NUMBER_BY_ROOM_+roomId);
				jedisService.deleteValueFromMap(BicycleConstants.USER_INFO+room.getCreatorId(), "liveRoom");
			}
		}
		//删除缓存中的房间信息
		jedisService.delete(BicycleConstants.ROOM_+roomId);
		jedisService.delete(BicycleConstants.ROOM_USERS+roomId);
		jedisService.deleteSortedSetItemFromShard(BicycleConstants.ROOM_ORDER, roomId);
		jedisService.delete("ROOM_USER_ALL_"+roomId);//
	}
	
	@Override
	public int isLive(String roomId) {
		Map<String,String> roomCache = jedisService.getMapByKey(BicycleConstants.ROOM_+roomId);
		if (roomCache != null && roomCache.size()>0 && roomCache.get("id") != null){
			return 1;
		}
		return 0;
	}

	@Override
	public List<Room> getRoomListByUid(Long uid, int status) {
		return roomRepository.findByCreatorIdAndStatus(uid, status);
	}

	@Override
	public Map<String, String> getSingleUser(Long userId, String uid,String roomId) {
		Map<String, String> map = new HashMap<String, String>();
		Ruser ruser = ruserService.find(userId);
		if (ruser != null){
			map.put("id", String.valueOf(ruser.getId()));
			map.put("name",ruser.getName() );
			map.put("signature",ruser.getSignature() );
			map.put("pic",ruser.getPic() );
			map.put("vipStat", String.valueOf(ruser.getVipStat()));
			map.put("fansCount", String.valueOf(ruser.getFansCount()));
			map.put("attentionCount", String.valueOf(ruser.getAttentionCount()));
		}
		if (uid != null && !"".equals(uid)){
			int isAttention = 0;
			if (attentionService.isAttention(Long.parseLong(uid), userId) == 1) {
				isAttention = 1 + attentionService.isAttention(userId, Long.parseLong(uid));
			}
			map.put("isAttention", String.valueOf(isAttention));
		} else {
			map.put("isAttention", "0");
		}
		if (jedisService.ifKeyExists("RoomBan_"+roomId+"_"+userId)){
			map.put("isFobidden", "1");
		} else {
			map.put("isFobidden", "0");
		}

		//判断用户是否被加入黑名单
		if(jedisService.isSetMemberInShard(BicycleConstants.BLACK_LIST_USER_ID + uid, String.valueOf(userId))){
			map.put("isBlackList", "1");
		}else{
			map.put("isBlackList", "0");
		}

		return map;
	}
	
	/**
	 * 获得直播流信息，返回map
	 * @param userId
	 * @return
	 */
	private Map<String,String> getSteamInfo(String userId,boolean canChangeCDN,String localOutIp) {
		Map<String,String> result = new HashMap<String, String>();
		Map<String,Object> streamMap = new HashMap<String, Object>();
		
//		String title = "anchor"+userId; 
		String title = MD5(UUID.randomUUID().toString());
		
		streamMap.put("id","wopai");
		streamMap.put("title", title);
		streamMap.put("hub", "busappstream");
		streamMap.put("publishKey", "wopai");
		streamMap.put("publishSecurity", "static");
		streamMap.put("disabled", false);
		streamMap.put("profiles", new String[]{"320p","480p"});
		
		Map<String,Object> hosts = new HashMap<String, Object>();
		
		String type = "qiniu";  //默认是七牛cdn
		if (canChangeCDN) {  //如果当前客户端允许切换cdn，则从缓存中查询配置的cdn名字
			type = jedisService.get(BicycleConstants.CDN_NAME);
		}
		
		Set<String> wangsuTestUids = jedisService.getSetFromShard("wangsu_test_uids");
		if (wangsuTestUids != null && wangsuTestUids.size() >0 && wangsuTestUids.contains(String.valueOf(userId))) { //测试临时使用,如果直播账号在测试账号组中，使用网宿cdn
			type = "wangsu";
		}
		
		Set<String> ucloudTestUids = jedisService.getSetFromShard("ucloud_test_uids");
		if (ucloudTestUids != null && ucloudTestUids.size() >0 && ucloudTestUids.contains(String.valueOf(userId))) { //测试临时使用,如果直播账号在测试账号组中，使用ucloud cdn
			type = "ucloud";
		}
		
		Set<String> leshiTestUids = jedisService.getSetFromShard("leshi_test_uids");
		if (leshiTestUids != null && leshiTestUids.size() >0 && leshiTestUids.contains(String.valueOf(userId))) { //测试临时使用,如果直播账号在测试账号组中，使用乐视 cdn
			type = "leshi";
		}
		
		Set<String> dilianTestUids = jedisService.getSetFromShard("dilian_test_uids");
		if (dilianTestUids != null && dilianTestUids.size() >0 && dilianTestUids.contains(String.valueOf(userId))) { //测试临时使用,如果直播账号在测试账号组中，使用帝联 cdn
			type = "dilian";
		}
		
		Set<String> xingyuTestUids = jedisService.getSetFromShard("xingyu_test_uids");
		if (xingyuTestUids != null && xingyuTestUids.size() >0 && xingyuTestUids.contains(String.valueOf(userId))) { //测试临时使用,如果直播账号在测试账号组中，使用星域 cdn
			type = "xingyu";
		}
		
		Set<String> aliTestUids = jedisService.getSetFromShard("ali_test_uids");
		if (aliTestUids != null && aliTestUids.size() >0 && aliTestUids.contains(String.valueOf(userId))) { //测试临时使用,如果直播账号在测试账号组中，使用阿里 cdn
			type = "ali";
		}
		
		if ("qiniu".equals(type)) {
			Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(userId));
			if (anchor != null ) {
				QiniuUtil util = new QiniuUtil();
				if ("".equals(anchor.getQiniuStreamId())) {  //七牛直播流id为空，创建直播流并返回，并将直播流id存到主播表里
					Stream stream = util.createStream(title);
					anchor.setQiniuStreamId(stream.getStreamId());
					anchorService.save(anchor);
					
					result.put("streamJson", stream.toJsonString());
					result.put("hlsLiveUrl", util.getLiveUrl(stream, "hls"));
					result.put("rtmpLiveUrl", util.getLiveUrl(stream, "rtmp"));
					
					return result;
				} else {   //七牛直播流id不为空，直接查询直播流并返回
					Stream stream = util.getStream(anchor.getQiniuStreamId());
					
					result.put("streamJson", stream.toJsonString());
					result.put("hlsLiveUrl", util.getLiveUrl(stream, "hls"));
					result.put("rtmpLiveUrl", util.getLiveUrl(stream, "rtmp"));
					
					return result;
				}
			}
		} else if ("lanxun".equals(type)) {
			Map<String,Object> publish = new HashMap<String, Object>();
			publish.put("rtmp", "test.wopaitv.com");
			
			Map<String,Object> play = new HashMap<String, Object>();
			play.put("rtmp", "view.wopaitv.com");
			play.put("hls", "view.wopaitv.com");
			
			hosts.put("publish", publish);
			hosts.put("play", play);
			
			result.put("hlsLiveUrl", "http://view.wopaitv.com/busappstream/"+title);
			result.put("rtmpLiveUrl", "rtmp://view.wopaitv.com/busappstream/"+title);
		} else if ("wangsu".equals(type)) {
			Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(userId));
			if (anchor != null ) {
				if (anchor.getStreamId() == null || "".equals(anchor.getStreamId())) {  //直播流id为空，创建直播流id并返回，并将直播流id存到主播表里
					anchor.setStreamId(title);
					anchorService.save(anchor);
				} else {   //直播流id不为空,使用主播之前生成好的直播流id
					title = anchor.getStreamId();
				}
			}
			streamMap.put("id","wopai");
			streamMap.put("title", title);
			streamMap.put("hub", "blive");
			streamMap.put("publishKey", "");
			streamMap.put("publishSecurity", "static");
			streamMap.put("disabled", false);
			streamMap.put("profiles", new String[]{"320p","480p"});
			
			Map<String,Object> publish = new HashMap<String, Object>();
			publish.put("rtmp", "wstl.wopaitv.com");
			
			//ngb解析
			if (StringUtils.isNotBlank(localOutIp)) {
				String ngbUrl = getNgbUrl("wstl.wopaitv.com/blive/"+title, localOutIp);
				if (StringUtils.isNotBlank(ngbUrl) && ngbUrl.contains("?") && ngbUrl.contains("rtmp")) {
					String[] ss = ngbUrl.split("\n");
					String url1 = ss[0];
					String pingUrl = "";
					for (int i=0;i<ss.length;i++) {
						int beginIndex = ss[i].indexOf("rtmp://")+7;
						int endIndex = ss[i].indexOf("/", beginIndex);
						pingUrl += ss[i].substring(beginIndex,endIndex)+"|";
					}
					if (pingUrl.endsWith("|")) {
						pingUrl = pingUrl.substring(0,pingUrl.length()-1);
					}
					streamMap.put("pingUrl", pingUrl);
					try {
						streamMap.put("title", title+"?"+url1.split("\\?")[1]);
						int beginIndex = url1.indexOf("rtmp://")+7;
						int endIndex = url1.indexOf("/", beginIndex);
						publish.put("rtmp", url1.substring(beginIndex,endIndex));
					} catch (Exception e) {
						streamMap.put("title", title);
						publish.put("rtmp", "wstl.wopaitv.com");
						e.printStackTrace();
					}
					String specialUrl = jedisService.get("wangsu_special_"+userId);
					if (StringUtils.isNotBlank(specialUrl)) { //指定用户，用指定的url，可以为域名或者ip
						streamMap.put("pingUrl", specialUrl);
						publish.put("rtmp", specialUrl);
					}
				}
			}
			
			Map<String,Object> play = new HashMap<String, Object>();
			play.put("rtmp", "wsflv.wopaitv.com");
			play.put("hls", "wshls.wopaitv.com");
			
			hosts.put("publish", publish);
			hosts.put("play", play);
			
			result.put("hlsLiveUrl", "http://wshls.wopaitv.com/blive/"+title+"/playlist.m3u8");
			result.put("rtmpLiveUrl", "http://wsflv.wopaitv.com/blive/"+title+".flv");
		} else if ("ucloud".equals(type)) {
			Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(userId));
			if (anchor != null ) {
				if (anchor.getStreamId() == null || "".equals(anchor.getStreamId())) {  //直播流id为空，创建直播流id并返回，并将直播流id存到主播表里
					anchor.setStreamId(title);
					anchorService.save(anchor);
				} else {   //直播流id不为空,使用主播之前生成好的直播流id
					title = anchor.getStreamId();
				}
			}
			String filename=title+"_"+System.currentTimeMillis();//回放的文件名
			streamMap.put("id","wopai");
			streamMap.put("title", title+"?record=true&filename="+filename);
			streamMap.put("hub", "ucloud-test");
			streamMap.put("publishKey", "");
			streamMap.put("publishSecurity", "static");
			streamMap.put("disabled", false);
			streamMap.put("profiles", new String[]{"320p","480p"});
			
			
			Map<String,Object> publish = new HashMap<String, Object>();
			publish.put("rtmp", "ucloud.wopaitv.com");
			
			String specialUrl = jedisService.get("ucloud_special_"+userId);
			if (StringUtils.isNotBlank(specialUrl)) { //指定用户，用指定的url，可以为域名或者ip
				publish.put("rtmp", specialUrl);
			}
			
			Map<String,Object> play = new HashMap<String, Object>();
			play.put("rtmp", "ucloud-rtmp.wopaitv.com");
			play.put("hls", "ucloud-hls.wopaitv.com");
			play.put("flv", "ucloud-flv.wopaitv.com");
			
			hosts.put("publish", publish);
			hosts.put("play", play);
			
			result.put("hlsLiveUrl", "http://ucloud-hls.wopaitv.com/ucloud-test/"+title+"/playlist.m3u8");
//			result.put("rtmpLiveUrl", "rtmp://ucloud-rtmp.wopaitv.com/ucloud-test/"+title);
			result.put("rtmpLiveUrl", "http://ucloud-rtmp.wopaitv.com/ucloud-test/"+title+".flv");
			result.put("flvLiveUrl", "http://ucloud-rtmp.wopaitv.com/ucloud-test/"+title+".flv");
		} else if ("ali".equals(type)) {
			Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(userId));
			if (anchor != null ) {
				if (anchor.getStreamId() == null || "".equals(anchor.getStreamId())) {  //直播流id为空，创建直播流id并返回，并将直播流id存到主播表里
					anchor.setStreamId(title);
					anchorService.save(anchor);
				} else {   //直播流id不为空,使用主播之前生成好的直播流id
					title = anchor.getStreamId();
				}
			}
			streamMap.put("id","wopai");
			streamMap.put("title", title+"?vhost=blive.wopaitv.com");
			streamMap.put("hub", "blive");
			streamMap.put("publishKey", "");
			streamMap.put("publishSecurity", "static");
			streamMap.put("disabled", false);
			streamMap.put("profiles", new String[]{"320p","480p"});
			
			
			Map<String,Object> publish = new HashMap<String, Object>();
			publish.put("rtmp", "video-center.alivecdn.com");
			
			Map<String,Object> play = new HashMap<String, Object>();
			play.put("rtmp", "blive.wopaitv.com");
			play.put("hls", "blive.wopaitv.com");
			play.put("flv", "blive.wopaitv.com");
			
			hosts.put("publish", publish);
			hosts.put("play", play);
			
			result.put("hlsLiveUrl", "http://blive.wopaitv.com/blive/"+title+".m3u8");
			result.put("rtmpLiveUrl", " rtmp://blive.wopaitv.com/blive/"+title);
			result.put("flvLiveUrl", "http://blive.wopaitv.com/blive/"+title+".flv");
		} else if ("leshi".equals(type)) {
			Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(userId));
			if (anchor != null ) {
				if (anchor.getStreamId() == null || "".equals(anchor.getStreamId())) {  //直播流id为空，创建直播流id并返回，并将直播流id存到主播表里
					anchor.setStreamId(title);
					anchorService.save(anchor);
				} else {   //直播流id不为空,使用主播之前生成好的直播流id
					title = anchor.getStreamId();
				}
			}
			streamMap.put("id","wopai");
			streamMap.put("title", title);
			streamMap.put("hub", "blive");
			streamMap.put("publishKey", "");
			streamMap.put("publishSecurity", "static");
			streamMap.put("disabled", false);
			streamMap.put("profiles", new String[]{"320p","480p"});
			
			
			Map<String,Object> publish = new HashMap<String, Object>();
			publish.put("rtmp", "psls.wopaitv.com");
			
			Map<String,Object> play = new HashMap<String, Object>();
			play.put("rtmp", "plls.wopaitv.com");
			play.put("hls", "plls.wopaitv.com");
			play.put("flv", "plls.wopaitv.com");
			
			hosts.put("publish", publish);
			hosts.put("play", play);
			
			result.put("hlsLiveUrl", "http://plls.wopaitv.com/blive/"+title+"/desc.m3u8");
//			result.put("rtmpLiveUrl", "rtmp://ucloud-rtmp.wopaitv.com/ucloud-test/"+title);
			result.put("rtmpLiveUrl", "http://plls.wopaitv.com/blive/"+title+".flv");
			result.put("flvLiveUrl", "http://plls.wopaitv.com/blive/"+title+".flv");
		} else if ("dilian".equals(type)) {
			Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(userId));
			if (anchor != null ) {
				if (anchor.getStreamId() == null || "".equals(anchor.getStreamId())) {  //直播流id为空，创建直播流id并返回，并将直播流id存到主播表里
					anchor.setStreamId(title);
					anchorService.save(anchor);
				} else {   //直播流id不为空,使用主播之前生成好的直播流id
					title = anchor.getStreamId();
				}
			}
			streamMap.put("id","wopai");
			streamMap.put("title", title);
			streamMap.put("hub", "blive");
			streamMap.put("publishKey", "");
			streamMap.put("publishSecurity", "static");
			streamMap.put("disabled", false);
			streamMap.put("profiles", new String[]{"320p","480p"});
			
			
			Map<String,Object> publish = new HashMap<String, Object>();
			publish.put("rtmp", "psdl.wopaitv.com");
			
			Map<String,Object> play = new HashMap<String, Object>();
			play.put("rtmp", "plls.wopaitv.com");
			play.put("hls", "pldlhls.wopaitv.com");
			play.put("flv", "pldl.wopaitv.com");
			
			hosts.put("publish", publish);
			hosts.put("play", play);
			
			result.put("hlsLiveUrl", "http://pldlhls.wopaitv.com/blive/"+title+"/index.m3u8");
//			result.put("rtmpLiveUrl", "rtmp://ucloud-rtmp.wopaitv.com/ucloud-test/"+title);
			result.put("rtmpLiveUrl", "http://pldl.wopaitv.com/blive/"+title+".flv");
			
			result.put("flvLiveUrl", "http://pldl.wopaitv.com/blive/"+title+".flv");
		} else if ("xingyu".equals(type)) {
			Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(userId));
			if (anchor != null ) {
				if (anchor.getStreamId() == null || "".equals(anchor.getStreamId())) {  //直播流id为空，创建直播流id并返回，并将直播流id存到主播表里
					anchor.setStreamId(title);
					anchorService.save(anchor);
				} else {   //直播流id不为空,使用主播之前生成好的直播流id
					title = anchor.getStreamId();
				}
			}
			streamMap.put("id","wopai");
			streamMap.put("title", title);
			streamMap.put("hub", "blive");
			streamMap.put("publishKey", "");
			streamMap.put("publishSecurity", "static");
			streamMap.put("disabled", false);
			streamMap.put("profiles", new String[]{"320p","480p"});
			
			
			Map<String,Object> publish = new HashMap<String, Object>();
			publish.put("rtmp", "psxy.wopaitv.com");
			
			Map<String,Object> play = new HashMap<String, Object>();
			play.put("rtmp", "psxy.wopaitv.com");
			play.put("hls", "plxyhls.wopaitv.com");
			play.put("flv", "plxy.wopaitv.com");
			
			hosts.put("publish", publish);
			hosts.put("play", play);
			
			result.put("hlsLiveUrl", "http://plxyhls.wopaitv.com/blive/"+title+".m3u8");
//			result.put("rtmpLiveUrl", "rtmp://ucloud-rtmp.wopaitv.com/ucloud-test/"+title);
			result.put("rtmpLiveUrl", "http://plxy.wopaitv.com/blive/"+title+".flv");
			
			result.put("flvLiveUrl", "http://plxy.wopaitv.com/blive/"+title+".flv");
		}
		streamMap.put("hosts", hosts);
		JSONObject json = JSONObject.fromObject(streamMap);
		
		result.put("streamJson", json.toString());
		result.put("streamId", title);
		return result;
	}
	
	private String getNgbUrl(String wsUrl,String localOutIp) {
		logger.info("ngb,wsUrl={},localOutIp={}",wsUrl,localOutIp);
		HttpClient client = new HttpClient();
		client.setConnectionTimeout(2000);
		client.setTimeout(2000);
		String url="http://sdkoptedge.chinanetcenter.com";
		GetMethod method = new GetMethod(url);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		method.addRequestHeader("WS_URL", wsUrl);
		method.addRequestHeader("WS_RETIP_NUM", "3");
		method.addRequestHeader("WS_URL_TYPE", "3");
		method.addRequestHeader("UIP", localOutIp);
		String respContent = "";
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				respContent = new String(method.getResponseBody(),"utf-8");
			} else {
				logger.error("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		logger.info("ngbUrl={}",respContent);
		return respContent.trim();
	}
	public static void main(String[] args) {
		RoomServiceImpl r = new RoomServiceImpl();
		r.getNgbUrl("wstl.wopaitv.com/blive/560f45f1774e6712", "192.168.108.160");
	}

	@Override
	public Date getDbTime() {
		Date date = roomRepository.getDbTime();
		return date == null?new Date():date;
	}
	
	public  String MD5(String sourceStr) {
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
            result = buf.toString().substring(8, 24);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

	@Override
	public Room queryLivingRoomByUserId(Long userId){
		return roomRepository.selectLivingRoomByUserId(userId);
	}

	/**
	 * 获得直播直播房间数量
	 * @param isLive 1正在直播 0直播结束
	 * @return
	 */
	@Override
	public int getLivingRoomSize(Integer isLive) {
		List<String> list = new ArrayList<String>();
		//正在直播
		if (isLive == 1) {
			Set<String> rooms = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER);
//			for (String roomId : rooms) {
//				Map<String, String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_ + roomId);
//				if (roomInfo != null && roomInfo.size() > 0 && roomInfo.get("id") != null) {
//					list.add(roomId);
//				}
//			}
			return rooms.size();
		} else {
			return roomRepository.getRoomCountByStatus(0);
		}
	}

	@Override
	public void savePlayback(Long roomId) {
		
		boolean isFail = jedisService.isSetMemberInShard(BicycleConstants.CHECK_FAIL_ROOM_ID, String.valueOf(roomId));
		if (isFail) { //如果该房间审核不通过，不保存回放
			jedisService.deleteSetItemFromShard(BicycleConstants.CHECK_FAIL_ROOM_ID, String.valueOf(roomId));
		} else {
			Room room = roomRepository.findOne(roomId);
			if (room != null) {
				String streamJson = room.getStreamJson();
				String playbackUrl = "";
				String type = "";
				
				if (streamJson != null && streamJson.contains("ucloud")){ //如果为“ucloud”,则保存ucloud的回放地址
					JSONObject json = JSONObject.fromObject(streamJson);
					String title = json.get("title").toString();
					String fileName = title.substring(title.indexOf("filename=")+9);
					String suffix = "mp4";
					if (StringUtils.isNotBlank(jedisService.get(BicycleConstants.PLAY_BACK_SUFFIX))){
						suffix = jedisService.get(BicycleConstants.PLAY_BACK_SUFFIX);
					}
					playbackUrl = "http://blive.ufile.ucloud.com.cn/"+fileName+"."+suffix;
				} else if (streamJson != null && streamJson.contains("wstl.wopaitv.com")){ //如果为“wstl.wopaitv.com”,则进行网宿的回放保存
//					//默认保存所有回放，但是状态为delete，用户在客户端点击保存后，将此回放信息状态改为check_ok
//					Video playback = videoService.getDeletePlaybackByRoomId(roomId);
//					if (playback != null) {
//						playback.setFlowStat("check_ok");
//						videoService.update(playback);
//					}
					type = "callback";
				} else if (streamJson != null && streamJson.contains("psxy.wopaitv.com")){ //如果为“psxy.wopaitv.com”,则进行星域的回放保存
//					//默认保存所有回放，但是状态为delete，用户在客户端点击保存后，将此回放信息状态改为check_ok
//					Video playback = videoService.getDeletePlaybackByRoomId(roomId);
//					if (playback != null) {
//						playback.setFlowStat("check_ok");
//						videoService.update(playback);
//					}
					type = "callback";
				} else { //默认按七牛的回放处理
					playbackUrl = getPlaybackUrl(room);
				}
				
				if ((playbackUrl != null && !"".equals(playbackUrl)) || "callback".equals(type)) {
					
					Ruser ruser=ruserService.find(room.getCreatorId());
					int liveWeight = ruser.getLiveWeight();
					
					Video video = new Video();
					
					video.setCreatorId(room.getCreatorId());
					video.setCreateDate(room.getCreateDateStr());
					video.setPublishTime(room.getFinishDateStr());//发布时间改为直播结束时间（毕，小瓶盖）
					video.setDataFrom("back");
					
					Long duration = 0l;
					if (room.getDuration() != null && room.getDuration().intValue()>0) {
						duration = room.getDuration();
					}
					
					video.setDuration(String.valueOf(duration));
					video.setVideoPic(room.getRoomPic());
					if ("callback".equals(type)) { //回放是回调形式的，playkey为空，等待回调后更新此字段
						video.setPlayKey("");
						video.setFlowStat(VideoStatus.已删除.getName()); //回放是回调形式的，默认删除状态，等待回调后更新此字段值
					} else {
						video.setPlayKey(playbackUrl);
						video.setFlowStat(VideoStatus.审核通过.getName());
					}
					video.setDescription(room.getTitle());
					video.setType(2);
					video.setPlayCountToday(Long.valueOf(room.getMaxAccessNumber()));//直播回放时,存的最大观看人数
					video.setLiveNoticeId(room.getId());//直播回放时,存的直播房间id
					//设置回放权重
//					video.setPlaybackWeight(liveWeight+room.getOnlineNumber()+room.getPraiseNumber()/100);//回放权重取消点赞数
					video.setPlaybackWeight(liveWeight+room.getOnlineNumber());
					
					videoService.save(video);
					
					try {
//						solrRoomService.addRoom(video.getId(), room.getTitle(), ruser.getId().toString(), ruser.getPic(), playbackUrl, room.getRoomPic());
						solrRoomService.addRoom(video.getId().longValue(), video.getPublishTime().toString(), playbackUrl, String.valueOf(duration), video.getDescription(), video.getVideoPic(), video.getCreateDate().toString(), video.getCreatorId().longValue(),Long.valueOf(room.getMaxAccessNumber()));
					} catch (SolrServerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (Exception e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
					
			    	
					
					String content = room.getTitle();
			    	Set<String> tags = new HashSet<String>();
			    	if (org.apache.commons.lang3.StringUtils.isNotBlank(content)) {
			    		Pattern pattern = Pattern.compile("(#([^#]+)#)");
			    		Matcher matcher = pattern.matcher(content);
			    		while (matcher.find()) {
			    			String tag = matcher.group(2);
			    			tags.add(tag);
			    		}
					}
			    	
					for(String _tag : tags){
						
						
						Long nCount=labelRepository.findByName(_tag);
						
						Label label = new Label();
						label.setName(_tag);
						label.setNum(0L);
						
						if(nCount.intValue()==0) {
							
							
							labelRepository.save(label);
							
							try {
								solrWoPaiTagService.index(label.getId(), label.getName(),label.getNum());
							} catch (SolrServerException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}catch (Exception e) {
				    			// TODO Auto-generated catch block
				    			e.printStackTrace();
				    		}
						}else {
							List labelList=labelRepository.findLabelIdByName(_tag);
							if(labelList!=null&&labelList.size()>0) {
								label.setId((Long) labelList.get(0));
							}
						}
						
						try {
							solrWoPaiLiveTagService.index(label.getId()+"_"+video.getId(), _tag, video.getId(),0);
						} catch (SolrServerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}catch (Exception e) {
			    			// TODO Auto-generated catch block
			    			e.printStackTrace();
			    		}
					}
					
			    	
				}
			}
		}
		
	}
	
	private String getPlaybackUrl(Room room) {
		QiniuUtil util = new QiniuUtil();
		Anchor anchor = anchorService.getAnchorByUserid(room.getCreatorId());
		Stream stream = util.getStream(anchor.getQiniuStreamId());
		Long start = room.getCreateDate()/1000;
		Long end = null;
		if (room.getFinishDate() != null && room.getFinishDate().intValue()>0){
			end = room.getFinishDate()/1000;
		} else {
			end = roomRepository.getDbTime().getTime()/1000;
		}
		
		return util.getHlsPlaybackUrl(stream, start, end);
	}

	@Override
	public Integer getAllPlaybackCount() {
		// TODO Auto-generated method stub
		return videoDAO.getLiveBackCount();
	}

	@Override
	public List<Map<String,String>> findPlaybackList(Long liveActivityId,Integer start,Integer end) {
		Map<String,Object> params = new HashMap<String,Object>();
		if(liveActivityId!=null){
			params.put("liveActivityId", liveActivityId);
		}
		params.put("start", start);
		params.put("end", end);
		Long time = System.currentTimeMillis();
		Long timeLimit = time - 1000*60*60*24;
		params.put("timeLimit", new Date(timeLimit));
		
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		
		List<Video> videoList = null;
		if(liveActivityId!=null){
			videoList = videoDAO.findLiveBackList(params);
		}
		if(videoList!=null && videoList.size()>0){
			for(Video v:videoList){
				Map<String,String> m = new HashMap<String,String>();
				m.put("type","0");
				m.put("createDate", v.getCreateDate().toString());
				m.put("publishTime", v.getPublishTime().toString());
				m.put("videoPic", v.getVideoPic());
				m.put("playKey", v.getPlayKey());
				m.put("description", v.getDescription());
				m.put("videoId", v.getId().toString());
				m.put("duration", v.getDuration());
				m.put("maxAccessNumber", v.getPlayCountToday().toString());
				m.put("liveNoticeId", String.valueOf(v.getLiveNoticeId()));

				if(liveActivityId != null){
					m.put("liveActivityId", liveActivityId.toString());
				}
				
				Ruser ruser = ruserService.find(v.getCreatorId()==null?0:v.getCreatorId());
				if(ruser!=null){
					m.put("anchorId", ruser.getId().toString());
					m.put("anchorSex", ruser.getSex());
					m.put("anchorName", ruser.getName());
					m.put("anchorPic", ruser.getPic());
					m.put("anchorVstat", String.valueOf(ruser.getVipStat()));
					m.put("anchorSignature", ruser.getSignature());
				}
				result.add(m);
			}
		}
		
		return result;
	}
	
	@Override
	public List<Map<String,String>> findNewPlaybackList(Long liveActivityId,Integer start,Integer end) {
		Map<String,Object> params = new HashMap<String,Object>();
		if(liveActivityId!=null){
			params.put("liveActivityId", liveActivityId);
		}
		params.put("start", start);
		params.put("end", end);
		Long time = System.currentTimeMillis();
		Long timeLimit = time - 1000*60*60*24;
		params.put("timeLimit", new Date(timeLimit));
		
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		
		List<Video> videoList = null;
		if(liveActivityId!=null){
			videoList = videoDAO.findNewLiveBackList(params);
		}
		if(videoList!=null && videoList.size()>0){
			for(Video v:videoList){
				Map<String,String> m = new HashMap<String,String>();
				m.put("type","0");
				m.put("createDate", v.getCreateDate().toString());
				m.put("publishTime", v.getPublishTime().toString());
				m.put("videoPic", v.getVideoPic());
				m.put("playKey", v.getPlayKey());
				m.put("description", v.getDescription());
				m.put("videoId", v.getId().toString());
				m.put("duration", v.getDuration());
				m.put("maxAccessNumber", v.getPlayCountToday().toString());
				
				if(liveActivityId != null){
					m.put("liveActivityId", liveActivityId.toString());
				}
				
				Ruser ruser = ruserService.find(v.getCreatorId()==null?0:v.getCreatorId());
				if(ruser != null){
					m.put("anchorId", ruser.getId().toString());
					m.put("anchorSex", ruser.getSex());
					m.put("anchorName", ruser.getName());
					m.put("anchorPic", ruser.getPic());
					m.put("anchorVstat", String.valueOf(ruser.getVipStat()));
					m.put("anchorSignature", ruser.getSignature());
				}
				
				result.add(m);
			}
		}
		
		return result;
	}

	@Override
	public List<Map<String, String>> getNewRoomList(Long liveActivityId,Integer page, Integer size) {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		long count = jedisService.zCard(BicycleConstants.ROOM_ORDER);
		int start = (page-1)*size;
		if(count <= start){
			return list;
		}
		Set<String> roomIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER, 0, -1);
		for (String roomId : roomIds) {  
			try {
				Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);
				if (room != null && room.size() > 0 && room.get("id") != null) {
					if(liveActivityId != null){
						if(!String.valueOf(liveActivityId).equals(room.get("liveActivityId")) && StringUtils.isNotBlank(room.get("liveActivityId"))){
							continue;
						}
					}
					//计算直播时长
					Long duration = System.currentTimeMillis() - Long.parseLong(room.get("createDate")); 
					room.put("duration", String.valueOf(duration < 0?0:duration));
					//查询用户信息
					Ruser ruser = ruserService.find(Long.parseLong(room.get("creatorId")));
					room.put("anchorName", ruser.getName());
					room.put("anchorPic", ruser.getPic());
					room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
					room.put("anchorSignature", ruser.getSignature());
					room.put("anchorSex", ruser.getSex());
					room.put("type","1");
					if (StringUtils.isBlank(room.get("onlineNumber"))) {
						room.put("onlineNumber", "0");
					} else {
						Long onlineNumber = Long.parseLong(room.get("onlineNumber"));
						if (onlineNumber < 0) {
							room.put("onlineNumber", "0");
						}
					}
					
					list.add(room);
				}
			} catch (Exception e) {
				logger.error("getRoomList error:roomId is " + roomId);
			}
		}  
		
		Integer lsize = list.size();
		if(lsize<=start){
			return null;
		}
		
		Integer end = start+size;
		if(lsize<end){
			end = lsize;
		}
		
		Collections.sort(list, new Comparator<Map<String,String>>(){

			@Override
			public int compare(Map<String, String> o1, Map<String, String> o2) {
				String createTime1 = o1.get("createDate");
				if(!StringUtils.isNumeric(createTime1)){
					return -1;
				}
				String createTime2 = o2.get("createDate");
				if(!StringUtils.isNumeric(createTime2)){
					return 0;
				}
				Long create1 = Long.parseLong(createTime1);
				Long create2 = Long.parseLong(createTime2);
				
				return create2.compareTo(create1);
			}
			
		});
		list = list.subList(start, end);
		
		return list;
	}
	
	public Map<String,List> getSolrRoomList(Integer page, Integer size, String title) {
		if(page==null) {
			page=1;
		}
		if(size==null) {
			size=10;
		}
		if(title==null) {
			title="";
		}
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		Set<String> roomIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER, 0, 10000);
		for (String roomId : roomIds) {  
			try {
				Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);
				if (room != null && room.size() > 0 && room.get("id") != null) {
					if(room.get("title")!=null&&!room.get("title").equals("")&&room.get("title").indexOf(title)>=0) {
						//计算直播时长
						Long duration = System.currentTimeMillis() - Long.parseLong(room.get("createDate")); 
						room.put("duration", String.valueOf(duration < 0?0:duration));
						//查询用户信息
						Ruser ruser = ruserService.find(Long.parseLong(room.get("creatorId")));
						room.put("anchorName", ruser.getName());
						room.put("anchorPic", ruser.getPic());
						room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
						room.put("anchorSignature", ruser.getSignature());
						room.put("anchorSex", ruser.getSex());
						room.put("type","1");
						if (StringUtils.isBlank(room.get("onlineNumber"))) {
							room.put("onlineNumber", "0");
						} else {
							Long onlineNumber = Long.parseLong(room.get("onlineNumber"));
							if (onlineNumber < 0) {
								room.put("onlineNumber", "0");
							}
						}
						
						
						Anchor anchor=anchorService.getAnchorByUserid(Long.parseLong(room.get("creatorId")));
						if(anchor!=null) {
							room.put("points",anchor.getTotalPointCount().toString());
						}else {
							room.put("points","0");
						}
						
						list.add(room);
					}
					
				}
			} catch (Exception e) {
				logger.error("getRoomList error:roomId is " + roomId);
			}
		}
		
		Collections.sort(list, new Comparator<Map<String,String>>(){

			@Override
			public int compare(Map<String, String> o1, Map<String, String> o2) {
				String createTime1 = o1.get("createDate");
				if(!org.apache.commons.lang3.StringUtils.isNumeric(createTime1)){
					return -1;
				}
				String createTime2 = o2.get("createDate");
				if(!org.apache.commons.lang3.StringUtils.isNumeric(createTime2)){
					return 0;
				}
				Long create1 = Long.parseLong(createTime1);
				Long create2 = Long.parseLong(createTime2);
				
				return create2.compareTo(create1);
			}
			
		});
		
		
		List roomList=new ArrayList();
		List backList=new ArrayList();
		
		if(list.size()>=(page-1)*size+size) {
			roomList=list.subList((page-1)*size, (page-1)*size+size);
		}else if(list.size()>=(page-1)*size&&list.size()<(page-1)*size+size) {
			roomList=list.subList((page-1)*size, list.size());
			
			if(BicycleConstants.VIDEO_BACK_FLG) {
				try {
					backList = solrRoomService.searchByStart(title, 0, (page-1)*size+size-list.size());
				} catch (SolrServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
			}else {
				
			}
			
//			roomList.add(rList);
			
		}else if(list.size()<(page-1)*size){
			if(BicycleConstants.VIDEO_BACK_FLG) {
				try {
					backList=solrRoomService.searchByStart(title, (page-1)*size-list.size(), size);
				} catch (SolrServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
			}else {
				
			}
			
//			roomList.add(rList);
		}
		
		Map map=new HashMap();
		
		map.put("liveList", roomList);
		
		for (Object obj : backList) {  
			HashMap roomMap=(HashMap) obj;
			//查询用户信息
			Ruser ruser = ruserService.find( (Long) roomMap.get("anchorId"));
			roomMap.put("anchorName", ruser.getName());
			roomMap.put("anchorPic", ruser.getPic());
			roomMap.put("anchorVstat", String.valueOf(ruser.getVipStat()));
			roomMap.put("anchorSignature", ruser.getSignature());
			roomMap.put("type","2");
			roomMap.put("anchorSex", ruser.getSex());
			
		}
					
		map.put("back", backList);
		
		return map;
	}

	@Transactional(readOnly = true,rollbackFor = Exception.class)
	@Override
	public Integer sendBarrage(Long uid, Long roomId, String message) {
		Ruser user = ruserService.find(uid);
//		if(user!=null && user.getDiamondCount()>1){
//			user.setDiamondCount(user.getDiamondCount()-1);
//			Ruser r = ruserService.update(user);
			
			WsMessage wsMessage = new WsMessage();
			wsMessage.setChildCode("1001");
			wsMessage.setCode("100");
			wsMessage.setRoomId(roomId+"");
			wsMessage.setContent(message);
			wsMessage.setSenderName(user.getName());
			wsMessage.getExtra().put("userid", uid);
			wsMessage.getExtra().put("sex", user.getSex());
			wsMessage.getExtra().put("name", user.getName());
			wsMessage.getExtra().put("username", user.getName());
			wsMessage.getExtra().put("signature", user.getSignature());
			wsMessage.getExtra().put("vipStat", user.getVipStat());
			wsMessage.getExtra().put("pic", user.getPic());
			
			wsMessageProducer.send("chat_topic_", wsMessage);
			
//			ConsumeRecord record = new ConsumeRecord();
//			record.setCreateDate(new Date());
//			record.setCreatorId(uid);
//			record.setDiamondCount(0);
//			record.setGiftName("弹幕");
//			record.setRoomId(roomId);
//			
//			consumeRecordRepository.saveAndFlush(record);
			
			return user.getDiamondCount();
//		}else{
//			return -1;
//		}
	}
	
	@Transactional(readOnly = true,rollbackFor = Exception.class)
	@Override
	public Integer sendBarrageNew(Long uid, Long roomId, String message) {
		Ruser user = ruserService.find(uid);
		Integer diamondCount = 1;
		if(user!=null && user.getDiamondCount()>=diamondCount){
			//扣减用户金币
			int result = ruserService.reduceDiamond(uid, diamondCount);
			if (result >0 ){
				WsMessage wsMessage = new WsMessage();
				wsMessage.setChildCode("1004");
				wsMessage.setCode("100");
				wsMessage.setRoomId(roomId+"");
				wsMessage.setContent(message);
				wsMessage.setSenderName(user.getName());
				wsMessage.getExtra().put("userid", uid);
				wsMessage.getExtra().put("sex", user.getSex());
				wsMessage.getExtra().put("name", user.getName());
				wsMessage.getExtra().put("username", user.getName());
				wsMessage.getExtra().put("signature", user.getSignature());
				wsMessage.getExtra().put("vipStat", user.getVipStat());
				wsMessage.getExtra().put("pic", user.getPic());
				
				wsMessageProducer.send("chat_topic_", wsMessage);
				
				ConsumeRecord record = new ConsumeRecord();
				record.setCreateDate(new Date());
				record.setCreatorId(uid);
				record.setDiamondCount(diamondCount);
				record.setGiftName("弹幕");
				record.setRoomId(roomId);
				
				consumeRecordRepository.saveAndFlush(record);
				
				return user.getDiamondCount()-diamondCount;
			} else {
				return -1;
			}
		}else{
			return -1;
		}
	}

	@Override
	public Integer getLiveTimes(Long userId) {
		return roomRepository.getLiveTimes(userId);
	}

	@Override
	public Long queryMaxRoomId(){
		return roomRepository.queryMaxRoomId();
	}

	@Override
	public List<LiveDetailDisplay> queryLiveDetailRecord(Map<String,Object> params){
		return roomDao.selectLiveDetailRecord(params);
	}

/*	@Override
	public Long queryNewRegUserLiveCount(String date){
		return roomRepository.queryNewRegUserLiveCount(date + " 00:00:00", date + "23:59:59");
	}

	@Override
	public Long queryNewLiveCount(String date){
		return roomRepository.queryNewLiveCount(date + " 00:00:00", date + "23:59:59");
	}*/


	/**
	 * 下线操作
	 * @param roomId 房间ID
	 * @param uid 后台操作ID
	 * @param message 消息
	 **/
	@Override
	public void offlineRoom(Long roomId,Long uid,String message){
		WsMessage msg = this.buildLiveEndMsg(String.valueOf(roomId), String.valueOf(uid), message);
		
		wsMessageProducer.send("chat_topic_", msg);
		long messId = jedisService.incr(BicycleConstants.LIVE_MESSAGE_COUNT);
		msg.getExtra().put("messageId", messId);
		jedisService.setValueToSortedSetInShard(BicycleConstants.LIVE_MESSAGE_QUEUE + roomId, messId, msg.toString());

		logger.info("直播管理,发送强制下线消息：{}",msg);
		jedisService.deleteSortedSetItemFromShard(BicycleConstants.ROOM_ORDER, roomId.toString());//默认清除缓存
	}

	private WsMessage buildLiveEndMsg(String roomId, String adminId, String content) {
		WsMessage m = new WsMessage();
		m.setCode("400");
		m.setChildCode("4003");
		m.setRoomId(roomId);
		m.setSenderId(adminId);
		m.setContent(content);
		m.setTitle("直播终结");
		return m;
	}

	@Override
	public Map<String, String> queryLiveDataByLiveActivityId(Long liveActivityId) {
		return roomDao.selectLiveDataByLiveActivityId(liveActivityId);
	}
	
	@Override
	public Map<String, String> selectLiveDataByLiveActivityIdAndUserId(Map params) {
		return roomDao.selectLiveDataByLiveActivityIdAndUserId(params);
	}
	
	@Override
	public Long findSenderCount(Map<String,Object> params) {
		return roomDao.findSenderCount(params);
	}
	
	@Override
	public Long findSumDPByLiveActivityId(Map<String,Object> params) {
		return roomDao.findSumDPByLiveActivityId(params);
	}

	@Override
	public Long queryDailyDataNewLiveCount(Map<String, Object> params) {
		return roomDao.selectDailyDataNewLiveCount(params);
	}

	@Override
	public Long queryDailyDataNewRegLiveCount(Map<String, Object> params) {
		return roomDao.selectDailyDataNewRegLiveCount(params);
	}

	@Override
	public Long queryDailyDataLiveCount(Map<String, Object> params) {
		return roomDao.selectDailyDataLiveCount(params);
	}

	@Override
	public List<LiveDayDetailDisplay> queryDailyDataLiveDetailCount(Map<String, Object> params) {
		return roomDao.selectDailyDataLiveDetailCount(params);
	}

	@Override
	public Long queryDailyDataLiveTotalCount(Map<String,Object> params){
		return roomDao.selectDailyDataLiveTotalCount(params);
	}

	@Override
	public Long queryDistinctLiveNumByLiveActivityId(Long liveActivityId) {
		return roomDao.selectDistinctLiveNumByLiveActivityId(liveActivityId);
	}

	@Override
	public OrganizationAnchorDisplay queryUserLiveDurationInfo(Map<String, Object> params) {
		return roomDao.selectUserLiveDurationInfo(params);
	}

	@Override
	public List<String> queryPeriodFirstTimeLiveUser(Map<String, Object> params) {
		return roomDao.selectPeriodFirstTimeLiveUser(params);
	}

	@Override
	public Room queryRoomByPersistentId(Map<String, Object> params) {
		return roomDao.selectRoomByPersistentId(params);
	}

	@Override
	public List<Room> queryRoomListByUserId(Map<String, Object> params) {
		return roomDao.selectRoomListByUserId(params);
	}
}
