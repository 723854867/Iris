package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Tuple;

import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.LiveNotice;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.RuserLiveActivity;
import com.busap.vcs.data.entity.ShareManage;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.vo.LoadConfigUrlVO;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LiveNoticeService;
import com.busap.vcs.service.LoadConfigUrlService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.RuserLiveActivityService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.ShareManageService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.utils.RequestValidateUtil;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

/**
 * h5页面关注的controller
 *
 */
@Controller
@RequestMapping("/live")
public class LiveH5Controller extends BaseH5Controller {

	private Logger logger = LoggerFactory.getLogger(LiveH5Controller.class);

	@Autowired
	protected HttpServletRequest request;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name = "roomService")
	private RoomService roomService;
	
	@Resource(name = "liveNoticeService")
	private LiveNoticeService liveNoticeService;
	
	@Resource(name="videoService") 
    VideoService videoService; 
	
	@Resource(name = "anchorService")
	private AnchorService anchorService;
	
	@Resource(name = "ruserLiveActivityService")
	private RuserLiveActivityService ruserLiveActivityService;
	
	@Resource(name = "respBodyBuilder")
    protected RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Resource(name = "requestValidateUtil")
	private RequestValidateUtil requestValidateUtil;
	
	@Resource(name = "shareManageService")
	private ShareManageService shareManageService;
	
	@Resource(name = "loadConfigUrlService")
	private LoadConfigUrlService loadConfigUrlService;

	/**
	 * 直播分享
	 * @param roomId
	 * @return
	 */
	@RequestMapping("/shareLive")
	public String shareLive(String roomId) {
		try {
			logger.info("H5,shareLive");
			logger.info("roomId={}",roomId);

			Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);
			if (room != null && room.size() > 0 && room.get("id") != null && jedisService.isSortedSetMemberInShard(BicycleConstants.ROOM_ORDER, roomId) != null) {
				//计算直播时长
				Long duration = System.currentTimeMillis() - Long.parseLong(room.get("createDate"));
				room.put("duration", String.valueOf(duration < 0?0:duration));
				//查询用户信息
				Ruser ruser = ruserService.find(Long.parseLong(room.get("creatorId")));
				room.put("anchorName", ruser.getName());
				room.put("anchorPic", ruser.getPic());
				room.put("anchorVstat", String.valueOf(ruser.getVipStat()));
				room.put("anchorSignature", ruser.getSignature());
			} else {
				room = new HashMap<String,String>();
				Room roomEntity = roomService.find(Long.parseLong(roomId));
				if (roomEntity != null) {
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
					room.put("id", roomId);
				}

			}
			
			//查询回放信息
			Video video = videoService.getPlaybackByRoomId(Long.parseLong(roomId));
			this.request.setAttribute("video", video);
			this.request.setAttribute("room", room);
			this.request.setAttribute("shareImg", room.get("roomPic")); //分享图片
			
			
			//获得直播分享文案
			try {
				ShareManage sm = new ShareManage();
				StringBuffer hql = new StringBuffer();
		        hql.append("FROM ShareManage share where share.shareType=").append(4);

		        List shareList = shareManageService.getObjectByJpql(hql, 1, 10, "share",
		                new ArrayList<ParameterBean>(), null, new ArrayList<OrderByBean>());
		        if (shareList.size() > 0) {
		        	sm = (ShareManage)shareList.get(0);
		        } 
		        this.request.setAttribute("shareTitle", sm.getShareTitle().replace("{NickName}", room.get("anchorName")));
		        this.request.setAttribute("shareText", sm.getShareText().replace("{NickName}", room.get("anchorName")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//获取微信签名
			try {
				String paramString = this.request.getQueryString();
				if (paramString != null && !"".equals(paramString)) {
					paramString ="?"+paramString;
				} else {
					paramString = "";
				}
				Map<String,String> wxConfig = createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/resth5/live/shareLive");
				
				this.request.setAttribute("timestamp", wxConfig.get("timestamp"));
				this.request.setAttribute("noncestr", wxConfig.get("noncestr"));
				this.request.setAttribute("signature", wxConfig.get("signature"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/live/live";
	}
	
	private String loadConfigUrl(String clientPf,String urlType){

		List<LoadConfigUrlVO> loadConfigUrlList = loadConfigUrlService.findLoadConfigUrlByClientPf(clientPf);
		for(LoadConfigUrlVO lcuv:loadConfigUrlList){
			if(lcuv.getType().equals(urlType)){
				return lcuv.getUrl();
			}
		}
		return null;
	}
	
	private Map<String, String> createWxConfig(String paramString,String url) {
		
		String requestUrl = "";
		String res = "";
		JSONObject jsonObj = null;
		
		String access_token = jedisService.get("wexin_token");
		
		if (access_token == null || "".equals(access_token)) {

			logger.info("createWxConfig begin");
			requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx9c59e9a110527d17&secret=f46786de9a5a40f7e5d87cfbbcd8aef0";
			res = httpGet(requestUrl);
			logger.info("H5,wechat globe access_token:{}",res);
			
			jsonObj = JSONObject.fromObject(res);
			access_token = jsonObj.getString("access_token");
			
			jedisService.set("wexin_token", access_token);
			jedisService.expire("wexin_token", 120*60); 
		}
		
		
		String jsapi_ticket = jedisService.get("jsapi_ticket");
		
		if (jsapi_ticket == null || "".equals(jsapi_ticket)) {
			requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
			logger.info("get wechat jsapi_ticket url is {}",requestUrl);
			res = httpGet(requestUrl);
			logger.info("H5,wechat jsapi_ticket:{}",res);
			jsonObj = JSONObject.fromObject(res);
			jsapi_ticket = jsonObj.getString("ticket");
			
			jedisService.set("jsapi_ticket", jsapi_ticket);
			jedisService.expire("jsapi_ticket", 120*60); 
		}
		
		
		url = url+paramString;
		logger.info("url is {}",url);
		
		String timestamp = String.valueOf(System.currentTimeMillis()/1000);
		String noncestr = getRandomString(10);
		
		String sting1 = "jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
		logger.info("sting1 is {}",sting1);
		
		String signature = DigestUtils.shaHex(sting1);
		logger.info("signature is {}",signature);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("timestamp", timestamp);
		map.put("noncestr", noncestr);
		map.put("signature", signature);
		
//		jedisService.setValueToMap(WX_CONFIG, map);
//		jedisService.expire(WX_CONFIG, 90*60); 
		
		return map;
	}

	public String httpGet(String url) {
		logger.info("httpclient,request url is :{}",url);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		String respContent = "";
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);
			System.out.println(statusCode);
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
		return respContent;
	}
	
	private String getRandomString(int length) { 
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";  
	    Random random = new Random();  
	    StringBuffer sb = new StringBuffer();  
	    for (int i = 0; i < length; i++) {  
	        int number = random.nextInt(base.length());  
	        sb.append(base.charAt(number));  
	    }  
	    return sb.toString();  
	 } 
	
	@RequestMapping("/identify")
	public String identify() {
		String uid = this.request.getParameter("uid");
		if (uid == null || "".equals(uid)){
			uid = this.request.getHeader("uid");
		}
		String accessToken = this.request.getParameter("access_token");
		if (accessToken == null || "".equals(accessToken)) {
			accessToken = this.request.getHeader("access_token");
		}
		Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(uid));
		//用户的证件信息照片地址不返回
		if (anchor != null) {
			anchor.setPicOne("");
			anchor.setPicTwo("");
			anchor.setPicThree("");
		}
		try {
			this.request.setAttribute("uid", uid);
			this.request.setAttribute("accessToken", accessToken);
			this.request.setAttribute("anchor", anchor);
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/app_within/liveIdentify/index";
	}
	
	//直播预告分享
	@RequestMapping("/shareLiveNotice")
	public String shareLiveNotice(String noticeId) {
//		return "redirect:http://www.wopaitv.com";
		try {
			logger.info("H5,shareLiveNotice");
			logger.info("noticeId={}",noticeId);
			
			LiveNotice notice = liveNoticeService.find(Long.parseLong(noticeId));
			if (notice != null) {
				Ruser ruser = ruserService.find(notice.getCreatorId());
				this.request.setAttribute("ruser", ruser);
			}
			Video video = videoService.getVideoByLiveNoticeId(Long.parseLong(noticeId));
			if (video != null) {
				this.request.setAttribute("video", video);
			}
			this.request.setAttribute("notice", notice);
			
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/app_within/live_forenotice/index";
	}
	
	//直播预告分享
	@RequestMapping("/liveActivityRank")
	public String liveActivityRank(Long liveActivityId) {
//		return "redirect:http://www.wopaitv.com";
		try {
			logger.info("H5,liveActivityRank");
			logger.info("liveActivityId={}",liveActivityId);
			
			this.request.setAttribute("liveActivityId", liveActivityId);
			
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/live/liveActivityRank";
	}
	
	@RequestMapping("/mic")
	public String mic(Long liveActivityId,HttpServletResponse response) {
		requestValidateUtil.referReqKey(response);
		this.request.setAttribute("liveActivityId", liveActivityId);
    	return "html5/app_within/mic/index";
	}
	
	@RequestMapping("/busEntertainment")
	public String busEntertainment() {
		//获取微信签名
		try {
			String paramString = this.request.getQueryString();
			if (paramString != null && !"".equals(paramString)) {
				paramString ="?"+paramString;
			} else {
				paramString = "";
			}
			Map<String,String> wxConfig = createWxConfig(paramString,loadConfigUrl("html5", "interfaceurl")+"/resth5/live/busEntertainment");
			
			this.request.setAttribute("timestamp", wxConfig.get("timestamp"));
			this.request.setAttribute("noncestr", wxConfig.get("noncestr"));
			this.request.setAttribute("signature", wxConfig.get("signature"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "html5/app_activity/busEntertainment/index";
	}
	
	
	//人气榜
	@RequestMapping("/popularityRank")
	@ResponseBody
	public RespBody popularityRank(Long liveActivityId,HttpServletResponse response) {
		if (!requestValidateUtil.validateReqKey(request, response)) {
			return respBodyWriter.toError("非法请求", -1);
		}
    	return respBodyWriter.toSuccess(getPopularityRank(liveActivityId));
	}
	
	private List<Map<String,Object>> getPopularityRank(Long liveActivityId) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	Set<String> uids = jedisService.getSetFromShard("h5_popularity_user_"+liveActivityId);
    	if (uids != null && uids.size() >0) {
    		for (String uid:uids){
    			if (jedisService.getSetSizeFromShard("h5_popularity_"+liveActivityId+"_"+uid)>0) {
    				Map<String,Object> map = new HashMap<String,Object>();
    				map.put("uid", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "id"));
    				map.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "name"));
    				map.put("vipStat", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "vipStat"));
    				map.put("signature", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "signature"));
    				map.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "pic"));
    				map.put("popularity", jedisService.getSetSizeFromShard("h5_popularity_"+liveActivityId+"_"+uid));
    				map.put("area", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "activityArea"));
    				map.put("school", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "activitySchool"));
    				list.add(map);
    			}
    		}
    		
    		Collections.sort(list, new Comparator<Map<String,Object>>(){
    			
    			@Override
    			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
    				String popularity1 = String.valueOf(o1.get("popularity"));
    				String popularity2 =  String.valueOf(o2.get("popularity"));
    				Long create1 = Long.parseLong(popularity1);
    				Long create2 = Long.parseLong(popularity2);
    				
    				return create2.compareTo(create1);
    			}
    			
    		});
    	}
    	return list;
	}
	
	//专属礼物金豆榜
	@RequestMapping("/beanRank")
	@ResponseBody
	public RespBody beanRank(Long liveActivityId,HttpServletResponse response) {
		if (!requestValidateUtil.validateReqKey(request, response)) {
			return respBodyWriter.toError("非法请求", -1);
		}
		return respBodyWriter.toSuccess(getBeanRank(liveActivityId));
	}
	
	private List<Map<String,Object>> getBeanRank(Long liveActivityId) {
		List<Map<String,Object>> ranklist = new ArrayList<Map<String,Object>>();
		logger.info("beanRank,liveActivityId={}",liveActivityId);
		Set<Tuple> set = jedisService.zrevrangeWithScores(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+liveActivityId, 0l, -1l);
		for (Tuple tuple:set) {
			String receiverId = tuple.getElement();//获得接受者id
			String giftNumber = String.valueOf(tuple.getScore());//获得收到专属礼物金豆数
			if (giftNumber != null && giftNumber.contains(".")) {
				giftNumber = giftNumber.substring(0,giftNumber.indexOf("."));
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("uid", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "id"));
			map.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "name"));
			map.put("signature", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "signature"));
			map.put("vipStat", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "vipStat"));
			map.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "pic"));
			map.put("bean", giftNumber);
			map.put("area", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "activityArea"));
			map.put("school", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "activitySchool"));
			ranklist.add(map);
		}
		return ranklist;
	}
	
	
	//专属礼物金币榜
	@RequestMapping("/diamondRank")
	@ResponseBody
	public RespBody diamondRank(Long liveActivityId,HttpServletResponse response) {
		if (!requestValidateUtil.validateReqKey(request, response)) {
			return respBodyWriter.toError("非法请求", -1);
		}
		return respBodyWriter.toSuccess(getDiamondRank(liveActivityId));
	}
	
	private List<Map<String,Object>> getDiamondRank(Long liveActivityId) {
		List<Map<String,Object>> ranklist = new ArrayList<Map<String,Object>>();
		logger.info("diamondRank,liveActivityId={}",liveActivityId);
		Set<Tuple> set = jedisService.zrevrangeWithScores(BicycleConstants.EXCLUSIVE_GIFT_RECORD_DIAMOND_+liveActivityId, 0l, -1l);
		for (Tuple tuple:set) {
			String senderId = tuple.getElement();//获得赠送者id
			String giftNumber = String.valueOf(tuple.getScore());//获得收到专属礼物金豆数
			if (giftNumber != null && giftNumber.contains(".")) {
				giftNumber = giftNumber.substring(0,giftNumber.indexOf("."));
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("uid", jedisService.getValueFromMap(BicycleConstants.USER_INFO+senderId, "id"));
			map.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+senderId, "name"));
			map.put("signature", jedisService.getValueFromMap(BicycleConstants.USER_INFO+senderId, "signature"));
			map.put("vipStat", jedisService.getValueFromMap(BicycleConstants.USER_INFO+senderId, "vipStat"));
			map.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+senderId, "pic"));
			map.put("bean", giftNumber);
			map.put("area", jedisService.getValueFromMap(BicycleConstants.USER_INFO+senderId, "activityArea"));
			map.put("school", jedisService.getValueFromMap(BicycleConstants.USER_INFO+senderId, "activitySchool"));
			ranklist.add(map);
		}
		return ranklist;
	}
	
	//总榜（金豆+人气）
	@RequestMapping("/totalRank")
	@ResponseBody
	public RespBody totalRank(Long liveActivityId,HttpServletResponse response) {
		if (!requestValidateUtil.validateReqKey(request, response)) {
			return respBodyWriter.toError("非法请求", -1);
		}
		List<Map<String,Object>> popularityRank = getPopularityRank(liveActivityId);
		List<Map<String,Object>> beanRank = getBeanRank(liveActivityId);
		List<Map<String,Object>> ranklist = new ArrayList<Map<String,Object>>();
		
		if (popularityRank != null && popularityRank.size()>0){
			for (Map<String,Object> p:popularityRank){
				String uid = String.valueOf(p.get("uid"));
				Double popularity = Double.parseDouble(String.valueOf(p.get("popularity")));
				String giftNumber = String.valueOf(jedisService.zscore(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+liveActivityId, uid));//获得该用户获得的专属礼物金豆数
				if (giftNumber != null && giftNumber.contains(".")) {
					giftNumber = giftNumber.substring(0,giftNumber.indexOf("."));
				}
				if (giftNumber == null || "null".equals(giftNumber)) {
					giftNumber = "0";
				}
				p.put("total",popularity*50*0.7+Double.parseDouble(giftNumber)*0.3);
				ranklist.add(p);
			}
		}
		Collections.sort(ranklist, new Comparator<Map<String,Object>>(){
			
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				String total1 = String.valueOf(o1.get("total"));
				String total2 =  String.valueOf(o2.get("total"));
				Double create1 = Double.parseDouble(total1);
				Double create2 = Double.parseDouble(total2);
				
				return create2.compareTo(create1);
			}
			
		});
		
		return respBodyWriter.toSuccess(ranklist);
	}
	
	/**
	 * 报名h5活动
	 * @param id 直播活动id
	 * @return
	 */
	@RequestMapping("/joinLiveActivity")
	@ResponseBody
	public RespBody joinLiveActivity(String uid,Long liveActivityId,String area,String school,HttpServletResponse response) {
		if (!requestValidateUtil.validateReqKey(request, response)) {
			return respBodyWriter.toError("非法请求", -1);
		}
		if (StringUtils.isBlank(uid)) {
			return respBodyWriter.toError("用户id不能为空", -1);
		}
		Ruser ruser = ruserService.find(Long.parseLong(uid));
		if (ruser == null) {
			return respBodyWriter.toError("用户不存在", -1);
		}
		if (StringUtils.isBlank(area)) {
			return respBodyWriter.toError("请选择地区", -1);
		}
		if (StringUtils.isBlank(school)) {
			return respBodyWriter.toError("请填写学校名称", -1);
		}
		logger.info("h5,joinLiveActivity,uid={},liveActivityId={}",uid,liveActivityId);
		if (ruserLiveActivityService.isJoin(Long.parseLong(uid), liveActivityId) == 0){ //没参加过的，允许参加活动
			RuserLiveActivity ruserLiveActivity = new RuserLiveActivity();
			ruserLiveActivity.setCreatorId(Long.parseLong(uid));
			ruserLiveActivity.setLiveActivityId(liveActivityId);
			ruserLiveActivityService.save(ruserLiveActivity);
			area = area.replace("|", "");
			school = school.replace("|", "");
			jedisService.setValueToSetInShard("h5_popularity_user_"+liveActivityId, uid);  //存放参与活动的主播id
			jedisService.setValueToMap(BicycleConstants.USER_INFO+uid, "activityArea", area); //将报名者地区存到缓存
			jedisService.setValueToMap(BicycleConstants.USER_INFO+uid, "activitySchool", school); //将报名者学校存到缓存
		} else {
			return respBodyWriter.toError("您已经报名", -1);
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("name", ruser.getName());
		map.put("area", area);
		map.put("school", school);
		return respBodyWriter.toSuccess(map);
	}
}
