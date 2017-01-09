package com.busap.vcs.web;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.LiveActivity;
import com.busap.vcs.data.entity.RuserLiveActivity;
import com.busap.vcs.service.Constants;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LiveActivityService;
import com.busap.vcs.service.RuserLiveActivityService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

//import com.busap.vcs.data.mapper.AttentionDAO;

@Controller()
@RequestMapping("liveActivity")
public class LiveActivityController {

	private Logger logger = LoggerFactory.getLogger(LiveActivityController.class);

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Autowired
    JedisService jedisService;
	
	@Resource(name = "liveActivityService")
	private LiveActivityService liveActivityService;
	
	@Resource(name = "ruserLiveActivityService")
	private RuserLiveActivityService ruserLiveActivityService;
	
	/**
	 * 根据id获得直播活动信息
	 * @return
	 */
	@RequestMapping("/findLiveActivityById")
	@ResponseBody
	public RespBody findLiveActivityById(Long id) {
		String uid = request.getHeader("uid");
		logger.info("findLiveActivityById,id={}",id);
		LiveActivity liveActivity = liveActivityService.find(id);
		liveActivity.setIsJoin(ruserLiveActivityService.isJoin(Long.parseLong(uid), id));
		return respBodyWriter.toSuccess(liveActivity);
	}
	
	/**
	 * 报名活动
	 * @param id 直播活动id
	 * @return
	 */
	@RequestMapping("/joinLiveActivity")
	@ResponseBody
	public RespBody joinLiveActivity(Long id) {
		String uid = request.getHeader("uid");
		logger.info("joinLiveActivity,uid={},liveActivityId={}",uid,id);
		if (ruserLiveActivityService.isJoin(Long.parseLong(uid), id) == 0){ //没参加过的，允许参加活动
			RuserLiveActivity ruserLiveActivity = new RuserLiveActivity();
			ruserLiveActivity.setCreatorId(Long.parseLong(uid));
			ruserLiveActivity.setLiveActivityId(id);
			ruserLiveActivityService.save(ruserLiveActivity);
		}
		return respBodyWriter.toSuccess();
	}
	
	
	/**
	 * 查询用户参与的直播活动
	 * @param findAll 1：查询所有（包括过期和下线的活动）0：查询有效期内的活动
	 * @return
	 */
	@RequestMapping("/findMyLiveActivity")
	@ResponseBody
	public RespBody findMyLiveActivity(int findAll) {
		String uid = request.getHeader("uid");
		logger.info("findMyLiveActivity,uid={},findAll={}",uid,findAll);
		List<LiveActivity> list = liveActivityService.findMyLiveActivity(Long.parseLong(uid),findAll);
		if (list != null) {
			for(LiveActivity liveActivity:list) {
				//获得用户自己的排名
				Long rank = jedisService.zrevRank(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+liveActivity.getId(), uid);
				if (rank == null){
					rank = 0l;
				} else {
					rank++;
				}
				liveActivity.setSelfPosition(String.valueOf(rank));
				//获得用户自己收到专属礼物金豆数
				String selfGiftNumber = String.valueOf(jedisService.zscore(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+liveActivity.getId(), uid));
				if (selfGiftNumber != null && selfGiftNumber.contains(".")) {
					selfGiftNumber = selfGiftNumber.substring(0,selfGiftNumber.indexOf("."));
				}
				liveActivity.setSelfGiftNumber((selfGiftNumber == null || "".equals(selfGiftNumber) || "null".equals(selfGiftNumber))?"0":String.valueOf(selfGiftNumber));
			}
		}
		return respBodyWriter.toSuccess(list);
	}
	
	@RequestMapping("/getRank")
	@ResponseBody
	public RespBody getRank(Long id,Integer page,Integer size) {
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String,Object>> ranklist = new ArrayList<Map<String,Object>>();
		String uid = request.getHeader("uid");
		logger.info("getRank,uid={},liveActivityId={}",uid,id);
		Set<Tuple> set = jedisService.zrevrangeWithScores(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+id, (page-1)*size.longValue(), page*size.longValue()-1);
		Map<String,String> liveActivity = jedisService.getMapByKey(BicycleConstants.LIVE_ACTIVITY_+id);
		int showCountOfTop = 20;
		if (liveActivity != null && liveActivity.get("showCountOfTop") != null && !"".equals(liveActivity.get("showCountOfTop"))){
			showCountOfTop = Integer.parseInt(liveActivity.get("showCountOfTop"));
		}
		for (Tuple tuple:set) {
			if(showCountOfTop <= 0){
				break;
			}
			Map<String,Object> rankInfoMap = new HashMap<String,Object>();
			String receiverId = tuple.getElement();//获得接受者id
			String giftNumber = String.valueOf(tuple.getScore());//获得收到专属礼物金豆数
			if (giftNumber != null && giftNumber.contains(".")) {
				giftNumber = giftNumber.substring(0,giftNumber.indexOf("."));
			}
			rankInfoMap.put("userInfo", jedisService.getMapByKey(BicycleConstants.USER_INFO+receiverId));
			rankInfoMap.put("giftNumber", (giftNumber==null || "".equals(giftNumber) || "null".equals(giftNumber))?0:giftNumber);
			ranklist.add(rankInfoMap);
			showCountOfTop--;
		}
		result.put("rank", ranklist);
		//获得用户自己的排名
		Long rank = jedisService.zrevRank(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+id, uid);
		if (rank == null){
			rank = 0l;
		} else {
			rank++;
		}
		result.put("selfPosition", rank);
		//获得用户自己收到的专属礼物金豆数量
		String selfGiftNumber = String.valueOf(jedisService.zscore(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+id, uid));
		if (selfGiftNumber!= null && selfGiftNumber.contains(".")) {
			selfGiftNumber = selfGiftNumber.substring(0,selfGiftNumber.indexOf("."));
		}
		result.put("selfGiftNumber", (selfGiftNumber == null || "".equals(selfGiftNumber) || "null".equals(selfGiftNumber))?0:selfGiftNumber);
		return respBodyWriter.toSuccess(result);
	}


	/**
	 * 取提示语
	 * @param type 1:分享直播 2:开启直播
	 * @return result
	 */
	@RequestMapping("getTipsByType")
	@ResponseBody
	public RespBody getTipsByType(Integer type) {
		if (Constants.GiveDiamondType.LIVE.getType() == type) {
			Map<String, String> constantsMap = jedisService.getMapByKey(BicycleConstants.FIRST_LIVE_GIVE_GOLD);
			return respBodyWriter.toSuccess(constantsMap.get("tips"));
		} else if (Constants.GiveDiamondType.SHARE.getType() == type) {
			//获取分享按钮提示文案
			Map<String, String> constantsMap = jedisService.getMapByKey(BicycleConstants.SHARE_GIVE_GOLD_BUTTON);
			return respBodyWriter.toSuccess(constantsMap.get("tips"));
		}
		return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
	}
}
