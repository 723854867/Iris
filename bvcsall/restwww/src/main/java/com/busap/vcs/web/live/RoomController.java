package com.busap.vcs.web.live;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import redis.clients.jedis.Tuple;
import scala.actors.threadpool.Arrays;

import com.busap.vcs.base.Action;
import com.busap.vcs.base.Message;
import com.busap.vcs.base.Module;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.LiveNotice;
import com.busap.vcs.data.entity.Probe;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Settlement;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.Constants;
import com.busap.vcs.service.FootballGirlService;
import com.busap.vcs.service.GiftService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LiveActivityService;
import com.busap.vcs.service.LiveNoticeService;
import com.busap.vcs.service.PingPayService;
import com.busap.vcs.service.ProbeService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.SensitivewordFilter;
import com.busap.vcs.service.SettlementService;
import com.busap.vcs.service.impl.NotificationJPushUtil;
import com.busap.vcs.service.impl.SolrRoomService;
import com.busap.vcs.service.impl.SolrUserService;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import com.busap.vcs.service.utils.MapUtils;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.util.SmsSendUtil;

/**
 * 直播,房间相关controller
 *
 */
@Controller
@RequestMapping("/live")
public class RoomController extends CRUDController<Room, Long> {

	private Logger logger = LoggerFactory.getLogger(RoomController.class);

	@Autowired
	NotificationJPushUtil util;

	@Resource(name = "roomService")
	RoomService roomService;
	
	@Resource(name = "anchorService")
	private AnchorService anchorService;

	@Value("${files.localpath}")
	private String basePath;
	
	@Resource(name = "sensitivewordFilter")
	SensitivewordFilter sensitivewordFilter;
	
	@Autowired
    private PingPayService pingPayService;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name = "liveActivityService")
	private LiveActivityService liveActivityService;
	
	@Resource(name="footballGirlService") 
    FootballGirlService footballGirlService; 
	
    @Resource
    private SettlementService settlementService;
    
    @Resource
    private LiveNoticeService liveNoticeService;
    
    @Resource(name="attentionService")
	private AttentionService attentionService;
	
	@Resource(name="giftService")
	private GiftService giftService;
   	
	@Autowired
    KafkaProducer kafkaProducer;
	
	@Resource(name = "roomService")
	@Override
	public void setBaseService(BaseService<Room, Long> baseService) {
		this.baseService = baseService;
	}

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Resource(name = "probeService")
	ProbeService probeService;
	
	@Resource(name = "solrUserService")
	private SolrUserService solrUserService;
	
	@Resource(name = "solrRoomService")
	private SolrRoomService solrRoomService;
	

	/**
	 * 获得直播房间列表
	 * 
	 * @param page
	 * @param size
	 * @param isLive
	 *            是否直播中，1：是，0：否,2:所有状态
	 * @param userId
	 *            用户id，用于查询指定用户的播放记录，为空是查询所有用户
	 * @return 房间列表
	 */
	@RequestMapping("/getRoomList")
	@ResponseBody
	public RespBody getRoomList(Integer page, Integer size, Integer isLive,
			String userId) {
		logger.info("getRoomList,page={},size={},isLive={},userId={}",page,size,isLive,userId);
		List<Map<String,String>> list = roomService.getRoomList(page,size,isLive,userId);
		int listSize = roomService.getLivingRoomSize(isLive);
		return respBodyWriter.toSuccess("success",list, String.valueOf(listSize));
	}
	
	/**
	 * 获得直播房间列表-最热
	 * 
	 * @param page
	 * @param size
	 * @param type 1 最热、0 最新
	 *            
	 * @return 房间列表
	 */
	@RequestMapping("/getLiveAndRecordList")
	@ResponseBody
	public RespBody getLiveAndRecordList(Integer page, Integer size, String type,Long liveActivityId) {
		logger.info("getLiveAandRecordList,page={},size={},type={}",page,size,type);
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		int liveSize = roomService.getLivingRoomSize(1);
		Integer backSize = 0;
		int total = liveSize;
		List<Map<String,String>> liveList;
		if(StringUtils.isNotBlank(type) && "1".equals(type)){
			if(liveActivityId!=null && liveActivityId>0){ //活动下的直播列表，需要回放
//				backSize =roomService.getAllPlaybackCount();
				total = liveSize + backSize.intValue();
				
				liveList = roomService.getRoomList(liveActivityId,page,size);
				if(liveList!=null && liveList.size()>0){
					result.put("liveList", liveList);
				}
				
				if(liveList!=null && liveList.size()>0){
					if(liveList.size()<size){
						Integer liveBackEnd = size.intValue()-liveList.size();
//						List<Map<String,String>> backList = new ArrayList<Map<String,String>>();
						List<Map<String,String>> backList =roomService.findPlaybackList(liveActivityId, 0, liveBackEnd.intValue());
						result.put("backList", backList);
					}
				} else {
					Integer start = (page-1)*size;
					if(start<0){
						start = 0;
					}
					Integer end = size;
//					result.put("backList", new ArrayList<Map<String,String>>());
					result.put("backList", roomService.findPlaybackList(liveActivityId, start, end));
				}
			}else{//首页下的直播列表，不需要回放
				liveList = roomService.getRoomList(page,size,1,type);
				if(liveList!=null && liveList.size()>0){
					result.put("liveList", liveList);
				}
				
				if(liveList!=null && liveList.size()>0){
					if(liveList.size()<size){
						Integer liveBackEnd = size.intValue()-liveList.size();
						List<Map<String,String>> backList = new ArrayList<Map<String,String>>();
//						List<Map<String,String>> backList =roomService.findPlaybackList(liveActivityId, 0, liveBackEnd.intValue());
						result.put("backList", backList);
					}
				} else {
					Integer start = (page-1)*size;
					if(start<0){
						start = 0;
					}
					Integer end = size;
					result.put("backList", new ArrayList<Map<String,String>>());
//					result.put("backList", roomService.findPlaybackList(liveActivityId, start, end));
				}
			}
			
		} else {
			liveList = roomService.getNewRoomList(liveActivityId,page,size);
			if(liveList != null && liveList.size()>0){
				result.put("liveList", liveList);
			}
			
			if(liveList!=null && liveList.size()>0){
				if(liveList.size()<size){
					Integer liveBackEnd = size.intValue()-liveList.size();
					List<Map<String,String>> backList = new ArrayList<Map<String,String>>();//roomService.findNewPlaybackList(liveActivityId, 0, liveBackEnd.intValue());
					result.put("backList", backList);
				}
			} else {
				Integer start = (page-1)*size;
				if(start<0){
					start = 0;
				}
				Integer end = size;
				result.put("backList",new ArrayList<Map<String,String>>());//roomService.findNewPlaybackList(liveActivityId, start, end));
			}
		}
		
		if (!result.containsKey("liveList")) {
			result.put("liveList", new ArrayList<Map<String,String>>());
		}
		
		return respBodyWriter.toSuccess("success",result, String.valueOf(total));
	}

	/**
	 * 创建直播房间
	 * @param title 直播主题
	 * @param file 直播封面
	 * @return 房间信息
	 */
	@RequestMapping("/createRoom")
	@ResponseBody
	public RespBody createRoom(String title,@RequestParam MultipartFile file,String version,String appVersion
			,Long liveActivityId,Integer liveType,String osVersion,String longitude,String latitude,String area) {
		String uid = this.request.getHeader("uid");
		logger.info("live,createRoom,uid={}", uid);
		
		String localOutIp = getLocalOutIp(request);
		
		Ruser ruser = ruserService.find(Long.parseLong(uid));
		
		if (ruser.getStat() == 2){//封号不能直播
			return respBodyWriter.toError(ResponseCode.CODE_347.toString(),ResponseCode.CODE_347.toCode()); 
		}
		
		String identifySwitch = jedisService.get(BicycleConstants.IDENTIFY_SWITCH);
		if (StringUtils.isNotBlank(identifySwitch) && identifySwitch.equals("1")) {
			if (ruser.getIsAnchor() == 0) {  //不是主播，不能创建直播房间
				return respBodyWriter.toError(ResponseCode.CODE_347.toString(),ResponseCode.CODE_347.toCode()); 
			}
		} else {
			if ((version != null && !"".equals(version)) || (appVersion != null && !"".equals(appVersion)&& Float.parseFloat(appVersion.substring(0,1))>=3)) { //3.0版本判断直播权限
				if (!SmsSendUtil.isPhone(ruser.getUsername()) && (ruser.getBandPhone() == null || "".equals(ruser.getBandPhone()))) {  //如果不是手机号登陆，并且没有绑定手机号，不让直播
					return respBodyWriter.toError(ResponseCode.CODE_347.toString(),ResponseCode.CODE_347.toCode()); 
				}
			} else { //3.0之前版本判断直播权限
				if (ruser.getIsAnchor() == 0 && ruser.getVipStat() == 0) {  //不是主播且不是vip，不能创建直播房间
					return respBodyWriter.toError(ResponseCode.CODE_347.toString(),ResponseCode.CODE_347.toCode()); 
				}
			}
		}
		
		//判断主播是否被禁播
		String time = jedisService.get(BicycleConstants.LOCK_LIVE_USER+uid);
		if (time != null && !"".equals(time)){
			Long current = System.currentTimeMillis();
			Long remain = Long.parseLong(time) - current;
			if (remain<=0 || remain/1000/60 == 0){
				remain = 1l;
			} else {
				remain = remain/1000/60;
			}
			return respBodyWriter.toError("账户被冻结，"+remain+"分钟后可以直播",ResponseCode.CODE_352.toCode()); 
		}
		
		//敏感词过滤
		title = sensitivewordFilter.replaceSensitiveWord(title, SensitivewordFilter.maxMatchType, "*"); 
		
		String pic = "";
		if (file == null || file.getSize() ==0 ) { //上传文件为空，封面设置为用户头像
			pic = ruserService.find(Long.parseLong(uid)).getPic();
		} else {
			pic= uploadPic(file,"roomPic",uid);
			if ("".equals(pic)){ //上传文件失败，封面设置为用户头像
				pic = ruserService.find(Long.parseLong(uid)).getPic();
			}
		}
		//创建房间时，删除该主播之前正在直播的房间
		List<Room> list = roomService.getRoomListByUid(Long.parseLong(uid), 1);
		for (Room liveRoom:list){
			roomService.destroyRoom(String.valueOf(liveRoom.getId()));
		}
		
		boolean canChangeCDN = canChangeCDN(osVersion, appVersion);
		boolean canLinkMic = canLinkMic(uid,osVersion,appVersion); 
		
		String giveTips = "";
		String platform = "android";
		if (osVersion != null && osVersion.contains("ios")){
			platform = "ios";
		}
		String channel = ruser.getRegPlatform();
		if (channel == null) {
			channel = "";
		}
		Map<String,String> room = roomService.createRoom(title, pic, Long.parseLong(uid),liveActivityId,liveType == null?0:liveType,canChangeCDN,localOutIp,appVersion,platform,channel,longitude,latitude,area,canLinkMic);
		if (room != null && room.get("id") != null){
			giveTips = giveDiamond(ruser, appVersion, platform); //首次直播赠送金币
		}
		room.put("anchorSex", ruser.getSex());
		room.put("anchorName", ruser.getName());
		room.put("anchorPic", ruser.getPic());
		room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
		room.put("giveTips",giveTips);
		return respBodyWriter.toSuccess(room);
	}
	
	private String getLocalOutIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		try {
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getHeader("Proxy-Client-IP");
			}
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getRemoteAddr();
			}
			logger.info("getLocalOutIp,allIp={}",ip);
			if (StringUtils.isNotBlank(ip)) {
				String[] ss = ip.split(",");
				if (ss.length > 1) {
					ip = ss[0];
				}
			}
			logger.info("getLocalOutIp,ip={}",ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * 判断是否可以切换CDN
	 * @param osVersion
	 * @param appVersion
	 * @return 
	 */
	private boolean canChangeCDN(String osVersion,String appVersion){
		logger.info("canChangeCDN,osVersion={},appVersion={}",osVersion,appVersion);
		boolean flag1 = false;  //平台是否符合要求
		boolean flag2 = false;  //版本号是否符合要求
		try {
			if (osVersion != null && osVersion.contains("ios")){  //ios版本大于等于303用ucloud
				flag1= true;
				if (appVersion !=null && !"".equals(appVersion)) {
					String versionNumber = appVersion.replaceAll("\\D", "");//过滤掉非数字字符
					if (Integer.parseInt(versionNumber) >= 303) {
						flag2 = true;
					}
				}
			} else {  //android版本大于等于305用ucloud
				flag1= true;
				if (appVersion !=null && !"".equals(appVersion)) {
					String versionNumber = appVersion.replaceAll("\\D", "");//过滤掉非数字字符
					if (Integer.parseInt(versionNumber) >= 305) {
						flag2 = true;
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag1&&flag2;
	}
	
	/**
	 * 判断主播是否可以被连麦
	 * @param osVersion
	 * @param appVersion
	 * @return 
	 */
	private boolean canLinkMic(String uid,String osVersion,String appVersion){
		logger.info("canLinkMic,uid={},osVersion={},appVersion={}",uid,osVersion,appVersion);
		boolean flag1 = false;  //平台是否符合要求
		boolean flag2 = false;  //版本号是否符合要求
		boolean flag3 = false;  //主播金豆数是否符合要求
		try {
			if (osVersion != null && osVersion.contains("ios")){  //ios版本大于等于303用ucloud
				flag1= true;
				if (appVersion !=null && !"".equals(appVersion)) {
					String versionNumber = appVersion.replaceAll("\\D", "");//过滤掉非数字字符
					if (Integer.parseInt(versionNumber) >= 324) {
						flag2 = true;
						Long pointLimit = 20000l;
						if (StringUtils.isNotBlank(jedisService.get(BicycleConstants.CONNECT_MICROPHONE_DIAMONDS))){
							pointLimit = Long.parseLong(jedisService.get(BicycleConstants.CONNECT_MICROPHONE_DIAMONDS));
						}
						boolean inWhiteList = false;
						if (jedisService.zrank(BicycleConstants.CONNECT_MICROPHONE_WHITE_LIST, uid) != null && jedisService.zrank(BicycleConstants.CONNECT_MICROPHONE_WHITE_LIST, uid).intValue() >=0) {
							inWhiteList  = true;
						}
						if (anchorService.getAnchorByUserid(Long.parseLong(uid)).getTotalPointCount() >= pointLimit || inWhiteList) {
							flag3 = true;
						}
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag1&&flag2&&flag3;
	}
	
	private String giveDiamond(Ruser ruser, String appVersion, String platform) {
		String giveSwitch = jedisService.get(BicycleConstants.GIVE_DIAMOND_OF_LIVE);
		if (giveSwitch != null && "1".equals(giveSwitch)) {
			Integer times = roomService.getLiveTimes(ruser.getId());
			if (times == 1) { //首次直播，赠送金币
				Map<String, String> constantsMap = jedisService.getMapByKey(BicycleConstants.FIRST_LIVE_GIVE_GOLD);
				String diamondString = constantsMap.get("diamond");
				pingPayService.giveDiamondByActivity(ruser, Integer.parseInt(diamondString), Constants.GiveDiamondType.LIVE.getType(), System.currentTimeMillis() / 1000l, appVersion, platform);
				
				return "首次直播，您已获得"+diamondString+"金币";
			}
		}
		return "";
	}
	
	/**
	 * 删除房间
	 * @param roomId
	 * @return
	 */
	@RequestMapping("/destroyRoom")
	@ResponseBody
	public RespBody destroyRoom(String roomId) {
		logger.info("live,destroyRoom,roomId={}", roomId);
		roomService.destroyRoom(roomId);
		return respBodyWriter.toSuccess();
	}
	
	/**
	 * 判断房间是否正在直播
	 * @param roomId
	 * @return
	 */
	@RequestMapping("/isLive")
	@ResponseBody
	public RespBody isLive(String roomId) {
		logger.info("live,isLive,roomId={}", roomId);
		Map<String,Object> map = new HashMap<String,Object>();
		int isLive = roomService.isLive(roomId);
		map.put("isLive", isLive);
		if (isLive == 1){
			//判断set中是否存在房间ID
			if (jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId) != null) {
				Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);
				if (room != null && room.size() > 0 && room.get("id") != null) {
					//计算直播时长
					Long duration = System.currentTimeMillis() - Long.parseLong(room.get("createDate"));
					room.put("duration", String.valueOf(duration < 0 ? 0 : duration));
					//查询主播信息
					Ruser ruser = ruserService.find(Long.parseLong(room.get("creatorId")));
					room.put("anchorName", ruser.getName());
					room.put("anchorSex", ruser.getSex());
					room.put("anchorPic", ruser.getPic());
					room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
					room.put("anchorSignature", ruser.getSignature());
					map.put("roomInfo", room);
				} else {
					map.put("isLive", 0);
					map.put("roomInfo", null);
				}
			}else{
				map.put("isLive", 0);
				map.put("roomInfo", null);
			}
		} else {
			map.put("roomInfo", null);
		}
		return respBodyWriter.toSuccess(map);
	}

	/**
	 * 上传直播封面
	 * @param file
	 * @return 封面地址
	 */
	private String uploadPic(MultipartFile file,String catalog,String number) {
		String relUrl = "";
		String relPath = File.separator + catalog + File.separator
				+ DateFormatUtils.format(new Date(), "yyyy-MM-dd")
				+ File.separator;
		try {
			String fileName = file.getOriginalFilename();
			String sufix = fileName.substring(fileName.lastIndexOf("."));
			String originalFilename = DateFormatUtils.format(new Date(),
					"yyyy-MM-dd_HHmmss")+number + sufix;

			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(
					basePath + relPath, originalFilename));
			relUrl = relPath + originalFilename;
		} catch (IOException e) {
			logger.error("文件上传失败", e);
		}
		return relUrl;
	}
	
	/**
	 * 获得房间用户列表
	 * @param roomId
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("/getRoomUser")
	@ResponseBody
	public RespBody getRoomUser(String roomId,int page,int size) {
		String uid = this.request.getHeader("uid");
		logger.info("getRoomUserList,roomId={},page={},size={}",roomId,page,size);
		List<Map<String,String>> roomUser = roomService.getRoomUser(roomId,page,size,uid);
		return respBodyWriter.toSuccess(roomUser);
	}
	
	/**
	 * 获得房间内单个用户信息
	 * @param userId
	 * @param roomId
	 * @return
	 */
	@RequestMapping("/getSingleUser")
	@ResponseBody
	public RespBody getSingleUser(Long userId,String roomId) {
		String uid = this.request.getHeader("uid");
		logger.info("getSingleUser,uid={} userId={}",uid,userId);
		Map<String,String> singleUser = roomService.getSingleUser(userId,uid,roomId);
		return respBodyWriter.toSuccess(singleUser);
	}
	
	/**
	 * 客户端是否显示“现场”分类
	 * @return 1：显示，0：不显示
	 */
	@RequestMapping("/showLive")
	@ResponseBody
	public RespBody showLive() {
		String flag = jedisService.get(BicycleConstants.LIVE_SWITCH);
		if (flag != null && "1".equals(flag)) {
			return respBodyWriter.toSuccess(1);
		}
		return respBodyWriter.toSuccess(0);
	}
	
	/**
	 * 获得礼物列表
	 * @return
	 */
	@RequestMapping("/getGiftList")
	@ResponseBody
	public RespBody getGiftList(int type,Long liveActivityId) {
		String uid = this.request.getHeader("uid");
		List<Map<String,String>> giftList = new ArrayList<Map<String,String>>();
		Set<String> giftIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.GIFT_ID);
		for (String giftId:giftIds){
			Map<String,String> gift = jedisService.getMapByKey(BicycleConstants.GIFT+giftId);
			if (gift != null && gift.get("state") != null && "1".equals(gift.get("state")) && String.valueOf(type).equals(gift.get("giftPurpose"))) {
				if (gift.get("isFree").equals("1")) {//免费礼物，返回剩余条数
					Integer freeCount = Integer.parseInt(gift.get("freeCount"));
					if (uid == null || "".equals(uid)){
						gift.put("remainFreeCount", String.valueOf(freeCount));
					} else {
						String sendCount = jedisService.get(BicycleConstants.DAY_FREE_GIFT+uid+"_"+giftId);
						if (sendCount == null || "".equals(sendCount)){
							gift.put("remainFreeCount", String.valueOf(freeCount));
						} else {
							Integer remainFreeCount = freeCount - Integer.parseInt(sendCount);
							gift.put("remainFreeCount", remainFreeCount <= 0?"0":String.valueOf(remainFreeCount));
						}
					}
				}
				giftList.add(gift);
			}
		}
		giftList = giftFilter(giftList, liveActivityId);
		return respBodyWriter.toSuccess(giftList);
	}
	
	/**
	 * 过滤礼物列表，置顶活动专属礼物，去除非该活动的礼物
	 * @param giftList
	 * @param liveActivityId
	 * @return
	 */
	private List<Map<String,String>> giftFilter(List<Map<String,String>> giftList,Long liveActivityId){
		try {
			List<Map<String,String>> commenGifts = new ArrayList<Map<String,String>>();  //通用礼物列表
			List<Map<String,String>> exlusiveGifts = new ArrayList<Map<String,String>>();  //专属礼物列表
			if (liveActivityId !=null && liveActivityId.intValue() > 0) {
				//查询直播活动
				Map<String,String> liveActivity = jedisService.getMapByKey(BicycleConstants.LIVE_ACTIVITY_+liveActivityId);
				//查询直播活动专属礼物
				String[] exclusicGiftIds = null;
				if (liveActivity.get("giftIds") != null && !"".equals(liveActivity.get("giftIds"))){
					exclusicGiftIds = liveActivity.get("giftIds").split(",");
				}
				for (Map<String,String> gift:giftList) {
					if (gift.get("isExclusive") == null || "".endsWith(gift.get("isExclusive")) || "0".equals(gift.get("isExclusive"))) {//非活动直播，把礼物列表中的专属礼物去除
						commenGifts.add(gift);
					} else {
						if (exclusicGiftIds != null && exclusicGiftIds.length >0){
							//添加专属礼物到列表
							if (Arrays.asList(exclusicGiftIds).contains(gift.get("id"))){
								exlusiveGifts.add(gift);
							}
						}
					}
				}
				exlusiveGifts.addAll(commenGifts); //专属礼物在列表中置顶
				return exlusiveGifts;
			} else {  //非活动直播，把礼物列表中的专属礼物去除
				for (Map<String,String> gift:giftList) {
					if (gift.get("isExclusive") == null || "".endsWith(gift.get("isExclusive")) || "0".equals(gift.get("isExclusive"))) {
						commenGifts.add(gift);
					}
				}
				return commenGifts;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return giftList;
	}
	
	/**
	 * 获得钻石列表
	 * @return
	 */
	@RequestMapping("/getDiamondList")
	@ResponseBody
	public RespBody getDiamondList(Integer isApple) {
		List<Map<String,String>> diamondList = new ArrayList<Map<String,String>>();
		Set<String> diamondIds = null;
		if (isApple.intValue() == 1) {  //获取苹果支付对应的钻石购买列表
			diamondIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.DIAMOND_APPLE_ID);
		}else if(isApple.intValue() == 2) {
			diamondIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.DIAMOND_BIGCHARGE_ID);
		}else if(isApple.intValue() == 3) {
			diamondIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.DIAMOND_YINGYONGBAO_ID);
		}else {  //获得其他支付对应的购买钻石列表
			diamondIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.DIAMOND_OTHER_ID);
		}
		for (String diamondId:diamondIds){
			Map<String,String> diamond = jedisService.getMapByKey(BicycleConstants.DIAMOND+diamondId);
			if (diamond != null && diamond.get("state") != null && "1".equals(diamond.get("state"))) {
				diamondList.add(diamond);
			}
		}
		return respBodyWriter.toSuccess(diamondList);
	}
	
	/**
	 * 获得支付渠道
	 * @param platform  ios:苹果，android：安卓
	 * @return
	 */
	@RequestMapping("/getPayChannel")
	@ResponseBody
	public RespBody getPayChannel(String platform) {
		Set<String> payChannel = null;
		if (platform != null && "ios".equals(platform)) {
			payChannel = jedisService.getSetFromShard(BicycleConstants.PAY_CHANNEL_IOS);
		} else {
			payChannel = jedisService.getSetFromShard(BicycleConstants.PAY_CHANNEL_ANDROID);
		}
		return respBodyWriter.toSuccess(payChannel);
	}
	
	/**
	 * 获得用户账户信息，包括点数和钻石数
	 * @return
	 */
	@RequestMapping("/getAccountInfo")
	@ResponseBody
	public RespBody getAccountInfo() {
		String uid = this.request.getHeader("uid");
		Map<String,String> map = new HashMap<String,String>();
		map.put("diamond", "0");
		map.put("point", "0");
		if (uid != null && !"".equals(uid)) {
			Ruser ruser = ruserService.find(Long.parseLong(uid));
			if (ruser != null) {
				map.put("diamond", String.valueOf(ruser.getDiamondCount()));
			}
			Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(uid));
			if (anchor != null) {
				map.put("point", String.valueOf(anchor.getPointCount()));
			}
		}
		return respBodyWriter.toSuccess(map);
	}
	
	/**
	 * 获得结算明细
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getSettlement")
	@ResponseBody
	public RespBody getSettlement(Integer page,Integer rows) {
		String uid = this.request.getHeader("uid");
		List<Settlement> list = settlementService.queryAll(Long.parseLong(uid), page, rows);
		return respBodyWriter.toSuccess(list);
	}
	
    /**
     * 获得主播认证状态
     * @return
     */
    @RequestMapping("/getIdentifyStatus")
    @ResponseBody
    public RespBody getIdentifyStatus(){ 
    	
    	String uid = this.request.getHeader("uid");
    	Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(uid));
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("isExist", anchor == null || anchor.getStatus() == -2 ? "0":"1"); //认证信息是否存在，1：存在，0：不存在
    	map.put("status", anchor == null ?"":anchor.getStatus());
    	map.put("reason", anchor == null ?"":anchor.getRejectReason());
    	
    	return respBodyWriter.toSuccess(map); 
    }  
    
    
    
    @RequestMapping(value = {"/publishLiveNotice"}, method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RespBody publishLiveNotice(@RequestParam(value = "description", required = true)String description,
							    		@RequestParam(value = "time", required = true)String time,
							    		@RequestParam(value = "pic", required = true)MultipartFile pic) throws ParseException{ 
    	String uid = this.request.getHeader("uid");
    	
    	
    	if(uid!=null&&!uid.equals("")) {
    		Ruser rus=ruserService.find(Long.valueOf(uid));
    		if(rus==null) {
    			return respBodyWriter.toError("用户不存在", 204);
    		}else {
    			if(rus.getIsAnchor()!=1) {
    				return respBodyWriter.toError("不是主播", 205);
    			}
    		}
    	}else {
    		return respBodyWriter.toError("用户uid为空", 203);
    	}
    	
    	
    	SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date publishTime=formatter.parse(time);
        
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String showTime=formatter1.format(publishTime);
        
        SimpleDateFormat formatter2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        Date curretDate=new Date();
        String currDateStr=formatter2.format(curretDate);
        long l =  publishTime.getTime()-curretDate.getTime();
        long d = l/(1000*60);//当前时间与发布时间相差的小时数(20160323改为一分钟 毕)
        if(d-1<0) {//当前时间距发布时间不到一小时（分钟），则返回错误信息
//        	return respBodyWriter.toError("当前时间距发布时间不到一分钟，现服务器时间为: "+currDateStr+",  请重新选择发布时间", 201);
        	return respBodyWriter.toError("请选择在  "+currDateStr+"  以后的时间", 201);

        }
        
        List lnList=liveNoticeService.findLiveNoticeByCreatorId(Long.valueOf(uid));
        
        LiveNotice ln=new LiveNotice();
        if(lnList!=null&&lnList.size()>0) {
        	ln=(LiveNotice) lnList.get(0);
        }
        
        Date cDate=new Date();
        ln.setDescription(description);
        
        ln.setCreateDate(cDate);
        ln.setModifyDate(cDate);
        ln.setShowTime(publishTime);
        
        ln.setCreatorId(Long.valueOf(uid));
        ln.setStatus(1);//老版本发布预告，默认为可用
        
        String fileName = pic.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
		String relPath = File.separator + "liveNotice" +File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
		String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;

		//上传
		String relUrl = "";
		try {
			FileUtils.copyInputStreamToFile(pic.getInputStream(), new File(basePath+relPath, originalFilename));
			relUrl = relPath + originalFilename;
		} catch (IOException e) {
			logger.error("文件[" + originalFilename + "]上传失败",e);
		}
		
		ln.setPic(relUrl);
		liveNoticeService.save(ln);
		
		List<Long> fansIds = attentionService.selectAllFansIdWithoutMajia(Long.valueOf(uid));
		for(Long fansId:fansIds) {
			Message msg = new Message();
			msg.setModule(Module.LIVE_NOTICE);
			msg.setAction(Action.INSERT);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", String.valueOf(uid));
			map.put("fansId", String.valueOf(fansId));
			map.put("showTime", showTime);
			msg.setDataMap(map);
	    	kafkaProducer.send("push-msg-topic", msg);
		}
		
		Ruser sru=ruserService.find(Long.valueOf(uid));
    	if(sru!=null) {
    		if(uid!=null&&!uid.equals("")) {
    			sru.setIsAttention(attentionService.isAttention(Long.valueOf(uid), sru.getId()));
    		}
    		
    		ln.setUser(sru);
    	}
		return respBodyWriter.toSuccess(ln);
        
    }
    
    public static long getCompareDate(String startDate,String endDate) throws ParseException {
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        Date date1=formatter.parse(startDate);    
        Date date2=formatter.parse(endDate);
        long l = date2.getTime() - date1.getTime();
        long d = l/(24*60*60*1000);
        return d;
    } 
    
    
    @RequestMapping(value = {"/getLiveNoticeList"}, method = {RequestMethod.GET})
    @ResponseBody
    public RespBody getLiveNoticeList(Long page,Long rows) throws Exception{ 
    	
    	HashMap<String,Object> map = new HashMap<String, Object>();
    	
    	String uid = this.request.getHeader("uid");
    	
    	if(uid!=null&&!uid.equals("")) {
    		List lnsList=liveNoticeService.findLiveNoticeByCreatorId(Long.valueOf(uid));
            
            LiveNotice ln=new LiveNotice();
            if(lnsList!=null&&lnsList.size()>0) {
            	ln=(LiveNotice) lnsList.get(0);
            	
            	Date newDate=new Date();
            	System.out.println(newDate.getTime());
            	
            	if(ln.getShowTime()!=null&&ln.getShowTime().longValue()>=newDate.getTime()) {
            		Ruser sru=ruserService.find(Long.valueOf(uid));
                	if(sru!=null) {
                		if(uid!=null&&!uid.equals("")) {
                			sru.setIsAttention(attentionService.isAttention(Long.valueOf(uid), sru.getId()));
                		}
                		
                		ln.setUser(sru);
                	}
                	
                	map.put("selfLN", ln);
            	}
            	
            }
    	}
    	
    	
    	
    	
    	
    	if(page==null) {
			page=1l;
        }
        if(rows==null) {
        	rows=20l;
        }
        
        StringBuffer jpql = new StringBuffer();
        StringBuffer countJpql = new StringBuffer();
        List<ParameterBean> paramList=new ArrayList<ParameterBean>();
        
        jpql.append("FROM LiveNotice ln where 1=1 and status=1 and ln.showTime >=now() ");
        countJpql.append("SELECT COUNT(*) FROM LiveNotice ln where 1=1 and status=1 and ln.showTime >=now()   ");
        
        
		
		List<OrderByBean> orderByList=new ArrayList<OrderByBean>();
		
		OrderByBean orderByObject1=new OrderByBean("showTime",0,"ln");
        orderByList.add(orderByObject1);
        
        
        
        
        List lnList=liveNoticeService.getObjectByJpql(jpql, page.intValue(), rows.intValue(), "ln", paramList, null, orderByList);
        
        List<Long> userIdList=new ArrayList<Long>();
        
        for (Object object : lnList) {
			
        	LiveNotice lnt=(LiveNotice) object;
        	
        	userIdList.add(lnt.getCreatorId());
			
			
		}
        
        if(userIdList!=null&&userIdList.size()>0) {
        	List ruserList=ruserService.findUsersByIds(userIdList);
    		
    		
    		List orderUserList=new ArrayList();
    		for(int i=0;i<lnList.size();i++) {
    			LiveNotice lnTemp=(LiveNotice)lnList.get(i);
    			for(int j=0;j<ruserList.size();j++) {
    				Ruser ruTemp=(Ruser)ruserList.get(j);
    				if(lnTemp.getCreatorId().longValue()==ruTemp.getId().longValue()) {
    					if(uid!=null&&!uid.equals("")) {
							int isAttention = 0;
							if (attentionService.isAttention(Long.valueOf(uid), ruTemp.getId()) == 1) {
								isAttention = 1 + attentionService.isAttention(ruTemp.getId(), Long.valueOf(uid));
							}
							ruTemp.setIsAttention(isAttention);
                		}
    					lnTemp.setUser(ruTemp);
    					break;
    				}
    			}
    			
    		}
        }
        
       
        
        Long totalCount=liveNoticeService.getObjectCountByJpql(countJpql, paramList);
        
        
		
		
		map.put("lnList",lnList);
		return respBodyWriter.toSuccess(map);
        
    }
    
    @RequestMapping(value = {"/getLiveNoticeDetailByUserId"}, method = {RequestMethod.GET})
    @ResponseBody
    public RespBody getLiveNoticeDetailByUserId(Long userId) throws Exception{ 
    	
    	HashMap<String,Object> map = new HashMap<String, Object>();
    	
    	String uid = this.request.getHeader("uid");
    	LiveNotice ln=new LiveNotice();
    	
    	if(userId!=null) {
    		List lnsList=liveNoticeService.findLiveNoticeByCreatorId(Long.valueOf(userId));
            
            
            if(lnsList!=null&&lnsList.size()>0) {
            	ln=(LiveNotice) lnsList.get(0);
            	
            	Ruser sru=ruserService.find(Long.valueOf(userId));
            	if(sru!=null) {
            		if(uid!=null&&!uid.equals("")) {
            			sru.setIsAttention(attentionService.isAttention(Long.valueOf(uid), sru.getId()));
            		}
            		ln.setUser(sru);
            	}
            	
            	respBodyWriter.toSuccess(ln);
            }
    	}
    	
    	
    	
    	
    	
		return respBodyWriter.toSuccess(ln);
        
    }
    
    /**
     * 根据直播预告id查询直播预告详情
     * @param liveNoticeId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/getLiveNoticeDetailById"}, method = {RequestMethod.GET})
    @ResponseBody
    public RespBody getLiveNoticeDetailById(Long liveNoticeId) throws Exception{ 
    	String uid = this.request.getHeader("uid");
    	LiveNotice ln = liveNoticeService.find(liveNoticeId);
        if(ln != null) {
        	Ruser sru = ruserService.find(ln.getCreatorId());
        	if(sru != null) {
        		if(uid != null && !"".equals(uid)) {
        			sru.setIsAttention(attentionService.isAttention(Long.valueOf(uid), sru.getId()));
        		}
        		ln.setUser(sru);
        	}
        }
        return respBodyWriter.toSuccess(ln);
    }

	/**
	 * 根据用户Id获取直播房间信息
	 * @param userId 用户ID
	 * @return
	 */
	@RequestMapping("getLivingRoomByUserId")
	@ResponseBody
	public RespBody getLivingRoomByUserId(Long userId) {
		Room room = roomService.queryLivingRoomByUserId(userId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (room == null) {
			//暂无正在直播的房间信息
			map.put("isLive", 0);
			map.put("roomInfo", null);
		} else {
			//System.out.println(room.getId());
			if (jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, String.valueOf(room.getId())) != null) {
				Map<String, String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_ + room.getId());
				if (roomInfo != null && roomInfo.size() > 0 && roomInfo.get("id") != null) {
					//计算直播时长
					Long duration = System.currentTimeMillis() - Long.parseLong(roomInfo.get("createDate"));
					roomInfo.put("duration", String.valueOf(duration < 0 ? 0 : duration));
					//查询主播信息
					Ruser ruser = ruserService.find(Long.parseLong(roomInfo.get("creatorId")));
					roomInfo.put("anchorName", ruser.getName());
					roomInfo.put("anchorPic", ruser.getPic());
					roomInfo.put("anchorVstat", String.valueOf(ruser.getVipStat()));
					roomInfo.put("anchorSignature", ruser.getSignature());
					map.put("isLive", 1);
					map.put("roomInfo", roomInfo);
				} else {
					map.put("isLive", 0);
					map.put("roomInfo", null);
				}
			} else {
				map.put("isLive", 0);
				map.put("roomInfo", null);
			}
		}
		return respBodyWriter.toSuccess(map);
	}
	
	/**
	 * 根据房间Id获取房间信息
	 * @param roomId 房间ID
	 * @return
	 */
	@RequestMapping("getRoomById")
	@ResponseBody
	public RespBody getRoomById(Long roomId) {
		Room room = roomService.find(roomId);
		return respBodyWriter.toSuccess(room);
	}
	
	@RequestMapping(value = {"/probeAdd"}, method = {RequestMethod.POST, RequestMethod.PUT})
	@ResponseBody
	public RespBody probeAdd(Probe probe,
							String localOutIP,		//本地出口IP
							String tracerouteList,	//路由跟踪
							String DNSList,			//DNS列表
							String TargetDNS,		//链接的DNS
							String pingLog,			//ping记录
							String publishHost,		//发包域名
							String piliHost,		//接包域名
							String publishSpeed,	//发包速度
							String piliSpeed,		//接包速度	
							String clientType,		//客户端类型
							String fullLog			//完整log
							
			){
		String uid = this.request.getHeader("uid");
		
		if(uid!=null&&!uid.equals("")) {
			probe.setCreatorId(Long.valueOf(uid));
		}
		
//		localOutIP=this.request.getHeader("x-forwarded-for");;		//本地出口IP
		
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		
		if (StringUtils.isNotBlank(ip)) {
			String[] ss = ip.split(",");
			if (ss.length > 1) {
				ip = ss[0];
			}
		}
		
		logger.info("************************************************************************************"+ip);
		probe.setLocalOutIP(ip);

		
		
		probeService.save(probe);
		
		return respBodyWriter.toSuccess();
	}
	/**
	 * 直播贡献排行榜
	 * @param userId
	 * @return
	 */
	@RequestMapping("contributionList")
	@ResponseBody
	public RespBody liveContributionList(Long userId){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		String uid = this.request.getHeader("uid");
		boolean isRank = false;
		
		Set<Tuple> idWithScores = jedisService.zrevrangeWithScores(BicycleConstants.LIVE_CONTRIBUTION_LIST + userId, 0l, 9l);
		for(Tuple tup:idWithScores){
			String member = tup.getElement();
			if(member.equals(uid)){
				isRank = true;
			}
			Double score = tup.getScore();
			Map<String,String> info = jedisService.getMapByKey(BicycleConstants.USER_INFO+member);
			if(!info.isEmpty()){
				info.put("score", score.longValue()+"");
				result.add(info);
			}
		}
		
		if(!isRank){//自己未上榜排最后一位
			Double score = jedisService.zscore(BicycleConstants.LIVE_CONTRIBUTION_LIST + userId, uid);
			if(score != null && score>0){
				Map<String,String> info = jedisService.getMapByKey(BicycleConstants.USER_INFO+uid);
				if(!info.isEmpty()){
					info.put("score", score.longValue()+"");
					result.add(info);
				}
			}
			
		}
		
		return this.respBodyWriter.toSuccess(result);
	}
	
	/**
	 * 获取主播收到的金豆总数
	 * @param userId
	 * @return
	 */
	@RequestMapping("getTotalBeans")
	@ResponseBody
	public RespBody getAnchorTotalBeans(Long userId,Long liveActivityId){
		String uid = this.request.getHeader("uid");
		Long total = giftService.excuteAnchorTotalPoints(userId);
		if(total == null){
			total = 0l;
		}
		//计算足球宝贝人气
//		createFootballRank(uid, userId);
		//计算h5直播活动人气
		createH5ActivityRank(uid,userId,liveActivityId);
		if (liveActivityId != null && liveActivityId.intValue() > 0){ //如果是直播活动，返回用户当前收到的专属礼物金豆数
			//获得用户自己收到专属礼物金豆数
			String selfGiftNumber = String.valueOf(jedisService.zscore(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+liveActivityId, String.valueOf(userId)));
			if (selfGiftNumber != null && selfGiftNumber.contains(".")) {
				selfGiftNumber = selfGiftNumber.substring(0,selfGiftNumber.indexOf("."));
			}
			return this.respBodyWriter.toSuccess(RespBody.MESSAGE_OK, total, (selfGiftNumber == null || "".equals(selfGiftNumber) || "null".equals(selfGiftNumber))?"0":String.valueOf(selfGiftNumber));
		}
		
		return this.respBodyWriter.toSuccess(total);
	}
	
	private void createVoiceRank(String uid, Long userId) {
		try {
			if (StringUtils.isNotBlank(uid)){
				String voiceIds = jedisService.get("voice_uids");
				if (StringUtils.isNotBlank(voiceIds)){
					String[] uidArray = voiceIds.split(",");
					if (Arrays.asList(uidArray).contains(String.valueOf(userId))){
						jedisService.setValueToSetInShard("voice_rank_"+userId, uid);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createFootballRank(String uid, Long userId) {
		try {
			if (StringUtils.isNotBlank(uid)){
				if (footballGirlService.isUidExist(userId)) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String date = format.format(new Date());
					jedisService.setValueToSetInShard("football_rank_"+userId, uid+"_"+date);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//创建h5直播活动排行榜
	private void createH5ActivityRank(String uid,Long userId,Long liveActivityId) {
		logger.info("createH5ActivityRank,uid={},userId={},liveActivityId={}",uid,userId,liveActivityId);
		try {
			if (StringUtils.isNotBlank(uid) && liveActivityId != null && liveActivityId.intValue() > 0) {
				Map<String,String> liveActivity = jedisService.getMapByKey(BicycleConstants.LIVE_ACTIVITY_+liveActivityId);
				if (StringUtils.isNotBlank(liveActivity.get("type")) && "2".equals(liveActivity.get("type"))) {  //如果是h5直播活动，计算人气榜
					jedisService.setValueToSetInShard("h5_popularity_"+liveActivityId+"_"+userId, uid);  //计算每个活动下每个主播的人气
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存直播回放
	 * @param roomId
	 * @return
	 */
	@RequestMapping("savePlayback")
	@ResponseBody
	public RespBody savePlayback(Long roomId){
		roomService.savePlayback(roomId);
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("/getSolrRoomList")
	@ResponseBody
	public RespBody getSolrRoomList(Integer page, Integer size, String title) {
		logger.info("getRoomList,page={},size={},isLive={},userId={}",page,size,title);
		Map<String,List> list = roomService.getSolrRoomList(page,size,title);
//		int listSize = roomService.getLivingRoomSize(isLive);
		return respBodyWriter.toSuccess("success",list);
	}
	/**
	 * 发送弹幕
	 * @param roomId
	 * @param message
	 * @return
	 */
	@RequestMapping("sendBarrage")
	@ResponseBody
	public RespBody sendBarrage(Long roomId,String message){
		String uid = this.request.getHeader("uid");
		if(StringUtils.isNumeric(uid)){
			if(jedisService.keyExists("RoomBan_"+roomId+"_"+uid)){
				return new RespBody(false,"您已被禁言，消息无法到达.",null,-1);
			}
			message = sensitivewordFilter.replaceSensitiveWord(message, SensitivewordFilter.maxMatchType, "*");
			Integer code = roomService.sendBarrage(Long.parseLong(uid),roomId,message);
			if(code >=0){
				return this.respBodyWriter.toSuccess(code);
			}else if(code == -1){
				return new RespBody(false,"余额不足，请及时充值",null,-1);
			}else{
				new RespBody(false,"服务器错误",null,-9);
			}
		}else{
			return respBodyWriter.toError("用户不存在", 204);
		}
		return null;
	}
	
	/**
	 * 发送弹幕
	 * @param roomId
	 * @param message
	 * @return
	 */
	@RequestMapping("sendBarrageNew")
	@ResponseBody
	public RespBody sendBarrageNew(Long roomId,String message){
		String uid = this.request.getHeader("uid");
		if(StringUtils.isNumeric(uid)){
			if(jedisService.keyExists("RoomBan_"+roomId+"_"+uid)){
				return new RespBody(false,"您已被禁言，消息无法到达.",null,-1);
			}
			message = sensitivewordFilter.replaceSensitiveWord(message, SensitivewordFilter.maxMatchType, "*");
			Integer code = roomService.sendBarrageNew(Long.parseLong(uid),roomId,message);
			if(code >=0){
				return this.respBodyWriter.toSuccess(code);
			}else if(code == -1){
				return new RespBody(false,"余额不足，请及时充值",null,-1);
			}else{
				new RespBody(false,"服务器错误",null,-9);
			}
		}else{
			return respBodyWriter.toError("用户不存在", 204);
		}
		return null;
	}
	
	/**
	 * 保存客户端直播截图
	 * @param file
	 * @return
	 */
	@RequestMapping("/saveScreenshot")
	@ResponseBody
	public RespBody saveScreenshot(@RequestParam MultipartFile file) {
		String uid = this.request.getHeader("uid");
		logger.info("live,createRoom,uid={}", uid);
		
		String url = uploadPic(file, "screenshot", uid);
		
		return respBodyWriter.toSuccess(url);
	}
	
	@RequestMapping("/initInfo")
	@ResponseBody
	public RespBody initInfo() {
		Map<String,Object> map = new HashMap<String,Object>();
		String pingSwitch = jedisService.get(BicycleConstants.PING_SWITCH);
		if (pingSwitch == null || "".equals(pingSwitch)){
			pingSwitch = "0";
		}
		map.put("pingSwitch", pingSwitch); //探针开关
		map.put("pushDomain", "ucloud.wopaitv.com");//ucloud推流域名
		map.put("pullDomain", "ucloud-rtmp.wopaitv.com");//ucloud播放域名
		map.put("identifyType", jedisService.get(BicycleConstants.IDENTIFY_TYPE));//认证方式:1.资料认证，2：手机号绑定
		return respBodyWriter.toSuccess(map);
	}
	
	//打开音乐功能
	@RequestMapping("/playMusic")
	@ResponseBody
	public RespBody playMusic(Long roomId) {
		Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);
		if (room != null) {
			jedisService.setValueToMap(BicycleConstants.ROOM_+ roomId, "music", "on");
		}
		return respBodyWriter.toSuccess();
	}
	
	/** * 判断字符串是否是整数 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@RequestMapping("/findUserAndLiveList")
	@ResponseBody
	public RespBody findUserAndLiveList(String key) {
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		
		
		
		
		if (key != null && !"".equals(key)) {
			
			List<Map<String,String>> liveList = new ArrayList<Map<String,String>>();
			
			Set<String> roomIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER, 0, 10000);
			int count=0;
			for (String roomId : roomIds) {  
				try {
					Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);
					if (room != null && room.size() > 0 && room.get("id") != null) {
						if(room.get("title")!=null&&!room.get("title").equals("")&&room.get("title").indexOf(key)>=0) {
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
							
							
							Anchor anchor=anchorService.getAnchorByUserid(Long.parseLong(room.get("creatorId")));
							if(anchor!=null) {
								room.put("points",anchor.getTotalPointCount().toString());
							}else {
								room.put("points","0");
							}
							
							liveList.add(room);
							count++;
							if(count>=10) {
								break;
							}
						}
						
					}
				} catch (Exception e) {
					logger.error("getRoomList error:roomId is " + roomId);
				}
			}
			
			Collections.sort(liveList, new Comparator<Map<String,String>>(){

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
			
			if (liveList!=null&&liveList.size()>0) {
				result.put("liveList", liveList);
			}else {
				result.put("liveList", new ArrayList<Map<String,String>>());
			}
			
			
			int size=0;
			Ruser user=null;
			List orderUserList=new ArrayList();
			
			boolean flg=isInteger(key);
			if(flg) {
				user=ruserService.find(Long.valueOf(key));
				String uid = (String)this.request.getHeader("uid");
				if(user!=null) {
					user.setIsAttention(attentionService.isAttention(Long.valueOf(uid), user.getId()) == 1 ?
							1 + attentionService.isAttention(user.getId(), Long.valueOf(uid)) : 0);
				}
			}
			if(user!=null) {
				size=9;
			}else {
				size=10;
			}
			
			
			List userIdList=new ArrayList();
			try {
				userIdList = solrUserService.search(key, 1, size);
				
				if(userIdList!=null&&userIdList.size()>0) {
					String uid = (String)this.request.getHeader("uid");
					
					List ruserList=ruserService.findUsersByIds(userIdList);
					
					
					if(uid!=null&&!uid.equals("")&&ruserList!=null&&ruserList.size()>0) {
						for (Object object : ruserList) {
							
							Ruser ru=(Ruser) object;

							ru.setIsAttention(attentionService.isAttention(Long.valueOf(uid), ru.getId()) == 1 ?
									1 + attentionService.isAttention(ru.getId(), Long.valueOf(uid)) : 0);
							
						}
						
					}
					
					
					for(int i=0;i<userIdList.size();i++) {
						for(int j=0;j<ruserList.size();j++) {
							if(((Long)userIdList.get(i)).longValue()==((Ruser)ruserList.get(j)).getId().longValue()) {
								orderUserList.add(ruserList.get(j));
								break;
							}
						}
						
					}
				}
				
				if(user!=null) {
					orderUserList.add(0, user);
				}
				
				
				
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result.put("userList", orderUserList);
			
			
			List backList=new ArrayList();
			
			try {
				backList=solrRoomService.searchByStart(key, 0, 10);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
			
			
			
			for (Object obj : backList) {  
				HashMap roomMap=(HashMap) obj;
				//查询用户信息
				if(roomMap.get("anchorId")!=null) {
					Ruser ruser = ruserService.find( (Long) roomMap.get("anchorId"));
					if(ruser!=null) {
						roomMap.put("anchorName", ruser.getName());
						roomMap.put("anchorPic", ruser.getPic());
						roomMap.put("anchorVstat", String.valueOf(ruser.getVipStat()));
						roomMap.put("anchorSignature", ruser.getSignature());
						roomMap.put("type","2");
						roomMap.put("anchorSex", ruser.getSex());
					}
					
				}
				
			}
						
			result.put("backList", backList);
			
			
			
			
		} 
		
		return respBodyWriter.toSuccess("success",result);
	}
	
	
	@RequestMapping("/findPlaybackList")
	@ResponseBody
	public RespBody findPlaybackList(String key,Integer page, Integer size) {
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		
		
		
		
		if (key != null && !"".equals(key)) {
			
			
			
			
			if(page==null) {
				page=1;
			}
			if(size==null) {
				size=10;
			}
			
			
			List backList=new ArrayList();
			
			try {
				backList=solrRoomService.searchByStart(key, (page-1)*size, size);
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
			
			
			for (Object obj : backList) {  
				HashMap roomMap=(HashMap) obj;
				//查询用户信息
				if(roomMap.get("anchorId")!=null) {
					Ruser ruser = ruserService.find( (Long) roomMap.get("anchorId"));
					if(ruser!=null) {
						roomMap.put("anchorName", ruser.getName());
						roomMap.put("anchorPic", ruser.getPic());
						roomMap.put("anchorVstat", String.valueOf(ruser.getVipStat()));
						roomMap.put("anchorSignature", ruser.getSignature());
						roomMap.put("type","2");
						roomMap.put("anchorSex", ruser.getSex());
					}
					
				}
				
			}
						
			result.put("backList", backList);
			
			
			
			
		} 
		
		return respBodyWriter.toSuccess("success",result);
	}

	/**
	 * 获取当前观看用户附近的直播信息
	 * @param userLongitude 经度
	 * @param userLatitude 纬度
	 * @param rows 行数
	 * @param page 页码
	 * @return RespBody 直播列表信息
	 */
	@RequestMapping("/getNearbyLiveInfo")
	@ResponseBody
	public RespBody getNearbyLiveInfo(String userLongitude,String userLatitude,Integer page, Integer rows){
		if(page == null || page <= 0 || rows == null || rows <= 0){
			return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
		}
		if(StringUtils.isBlank(userLongitude) || StringUtils.isBlank(userLatitude)){
			return respBodyWriter.toError(ResponseCode.CODE_458.toString(), ResponseCode.CODE_458.toCode());
		}

		List<Map<String,String>> roomList = new ArrayList<Map<String, String>>();
		//当前直播中的房间ID信息
		Set<String> roomIds = jedisService.getSortedSetFromShardByDesc(BicycleConstants.ROOM_ORDER);
		for (String roomId : roomIds){
			//获取直播房间信息，比对当前用户坐标和直播用户坐标信息
			Map<String, String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_ + roomId);
			if(roomInfo != null && roomInfo.size() > 0 && StringUtils.isNotBlank(roomInfo.get("id")) && StringUtils.isNotBlank(roomInfo.get("createDate"))) {
				String roomLongitude = roomInfo.get("longitude"); //房间经度
				String roomLatitude = roomInfo.get("latitude"); //房间纬度
				//计算直播时长
				//System.out.println(System.currentTimeMillis() + "=========" + roomInfo.get("createDate"));
				Long duration = System.currentTimeMillis() - Long.parseLong(roomInfo.get("createDate"));

				roomInfo.put("duration", String.valueOf(duration < 0 ? 0 : duration));

				//当前直播房间开启位置信息
				if (StringUtils.isNotBlank(roomLongitude) && StringUtils.isNotBlank(roomLatitude)) {
					//计算距离
					double distance = MapUtils.getDistance(userLongitude, userLatitude, roomLongitude, roomLatitude);
					roomInfo.put("sortDistance", String.valueOf(distance));
					String distanceStr = "";
					if (distance < 1) {//1公里以内，换算为m
						distance = distance * 1000;
						distanceStr = distance + "";
						distanceStr = distanceStr.substring(0, distanceStr.indexOf("."));
						roomInfo.put("distance", distanceStr + "m");
					} else if (distance >= 1 && distance <= 25) {//大于等于1公里小于等于25公里，单位为公里km
						distance = Math.round(distance * 10000) / 10000;
						distanceStr = distance + "";
						distanceStr = distanceStr.substring(0, distanceStr.indexOf("."));
						roomInfo.put("distance", distanceStr + "km");
					} else {//大于25km 显示城市信息
						distanceStr = StringUtils.isBlank(roomInfo.get("area"))?jedisService.get(BicycleConstants.B_STAR_NAME):roomInfo.get("area");
						roomInfo.put("distance", distanceStr);
					}
					//查询主播信息
					if (StringUtils.isNotBlank(roomInfo.get("creatorId"))) {
						Map<String, String> userMap = jedisService.getMapByKey(BicycleConstants.USER_INFO + roomInfo.get("creatorId"));
						roomInfo.put("anchorName", userMap.get("name"));
						roomInfo.put("anchorPic", userMap.get("pic"));
						roomInfo.put("anchorVstat", userMap.get("vipStat"));
						roomInfo.put("anchorSignature", userMap.get("signature"));
						roomList.add(roomInfo);
					}
				}

			}
		}
		sortByDistance(roomList);
		int totalCount = roomList.size();
		int m = totalCount % rows;
		int pageCount = 0;
		if (m > 0) {
			pageCount = totalCount / rows + 1;
		} else {
			pageCount = totalCount / rows;
		}
		if (m == 0) {
			if (totalCount >= rows * (page)) {
				roomList = roomList.subList((page - 1) * rows, rows * (page));
			} else {
				roomList = null;
			}
		} else {
			if (page == pageCount) {
				roomList = roomList.subList((page - 1) * rows, totalCount);
			} else {
				if (totalCount >= rows * (page)) {
					roomList = roomList.subList((page - 1) * rows, rows * (page));
				} else {
					roomList = null;
				}
			}
		}
		return respBodyWriter.toSuccess(roomList);
	}
	/**
	 * 按照距离排序（从近到远）
	 * @param result
	 * @return RespBody 直播列表信息
	 */
	private void sortByDistance(List<Map<String,String>> result){
			Collections.sort(result, new Comparator<Map<String, String>>() {
				@Override
				public int compare(Map<String, String> o1, Map<String, String> o2) {
					Double distance1 = Double.valueOf(o1.get("sortDistance"));
					Double distance2 = Double.valueOf(o2.get("sortDistance"));
					return distance1.compareTo(distance2);
				}

			});
	}

}

