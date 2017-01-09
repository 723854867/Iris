package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.busap.vcs.data.enums.TimeTypeEnum;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller()
@RequestMapping("rank")
public class RankController extends CRUDController<Video, Long> {
	
	@Resource(name = "videoService")
	private VideoService videoService;

	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name="attentionService")
	private AttentionService attentionService;
	
	@Override
	public void setBaseService(BaseService<Video, Long> baseService) {
		this.baseService = baseService;
	}
    
  //视频热度日排行
    @RequestMapping("/findDayHotVideosRank")
    @ResponseBody
    public RespBody findDayHotVideosRank(@RequestParam(value = "start", required = false)Integer start,@RequestParam(value = "count", required = false)Integer count){
    	String uid = this.request.getHeader("uid");
    	return respBodyWriter.toSuccess(videoService.findDayHotVideosRank(StringUtils.isNotBlank(uid)?Long.parseLong(uid):null,start,count)); 
    }
    
  //视频热度日排行更新时间
    @RequestMapping("/findVideoRankUpdate")
    @ResponseBody
    public RespBody findHotVideosUpdateTime(){
    	String time = jedisService.get(BicycleConstants.HOT_VIDEOS_UPDATE_TIME);
    	if(StringUtils.isBlank(time)){
    		time = String.valueOf(new Date().getTime());
    		jedisService.set(BicycleConstants.HOT_VIDEOS_UPDATE_TIME,time);
    	}
    	return respBodyWriter.toSuccess(time); 
    }
    
  //视频热度日排行更新时间
    @RequestMapping("/findUserPopularityRankUpdate")
    @ResponseBody
    public RespBody findUserPopularityUpdateTime(){
    	String time = jedisService.get(BicycleConstants.USER_POPULARITY_UPDATE_TIME);
    	if(StringUtils.isBlank(time)){
    		time = String.valueOf(new Date().getTime());
    		jedisService.set(BicycleConstants.USER_POPULARITY_UPDATE_TIME,time);
    	}
    	return respBodyWriter.toSuccess(time); 
    }
    
  //用户人气日排行
    @RequestMapping("/findDayUserPopularityRank")
    @ResponseBody
    public RespBody findDayUserPopularityRank(@RequestParam(value = "start", required = false)Integer start,@RequestParam(value = "count", required = false)Integer count){
    	String uid = this.request.getHeader("uid");
    	return respBodyWriter.toSuccess(ruserService.findDayUserPopularity(StringUtils.isNotBlank(uid)?Long.parseLong(uid):null,start,count)); 
    }

    /**
	 * 视频收视排行榜更新时间
	 *
	 * @param type 时间类型 0 24小时 1 周 2 月 3 年
	 * @return
	 */
	@RequestMapping("/findHotVideoRankingListUpdateTime")
	@ResponseBody
	public RespBody findHotVideoRankingListUpdateTime(Integer type) {
		String time = "";
		switch (TimeTypeEnum.valueByCode(type)){
			case day:
				time = jedisService.get(BicycleConstants.TWENTY_FOUR_HOUR_HOT_VIDEOS_UPDATE_TIME);
				if (StringUtils.isBlank(time)) {
					time = String.valueOf(new Date().getTime());
					jedisService.set(BicycleConstants.TWENTY_FOUR_HOUR_HOT_VIDEOS_UPDATE_TIME, time);
				}
				break;
			case week:
				time = jedisService.get(BicycleConstants.WEEK_HOT_VIDEOS_UPDATE_TIME);
				if (StringUtils.isBlank(time)) {
					time = String.valueOf(new Date().getTime());
					jedisService.set(BicycleConstants.WEEK_HOT_VIDEOS_UPDATE_TIME, time);
				}
				break;
			case month:
				time = jedisService.get(BicycleConstants.MONTH_HOT_VIDEOS_UPDATE_TIME);
				if (StringUtils.isBlank(time)) {
					time = String.valueOf(new Date().getTime());
					jedisService.set(BicycleConstants.MONTH_HOT_VIDEOS_UPDATE_TIME, time);
				}
				break;
			case year:
				time = jedisService.get(BicycleConstants.YEAR_HOT_VIDEOS_UPDATE_TIME);
				if (StringUtils.isBlank(time)) {
					time = String.valueOf(new Date().getTime());
					jedisService.set(BicycleConstants.YEAR_HOT_VIDEOS_UPDATE_TIME, time);
				}
				break;
		}
		return respBodyWriter.toSuccess(time);
	}

	/**
	 * 视频收视排行榜
	 *
	 * @param type 时间类型 0 24小时 1 周 2 月 3 年
	 * @param start 开始条数
	 * @param count 总数
	 * @return
	 */
	@RequestMapping("/findHotVideoRankingList")
	@ResponseBody
	public RespBody findHotVideoRankingList(Integer type,
											@RequestParam(value = "start", required = false) Integer start,
											@RequestParam(value = "count", required = false) Integer count) {
		String uid = this.request.getHeader("uid");
		String typeStr = "";
		if (type == 0) {
			typeStr = "day";
		} else if (type == 1) {
			typeStr = "week";
		} else if (type == 2) {
			typeStr = "month";
		} else if (type == 3) {
			typeStr = "year";
		}
		return respBodyWriter.toSuccess(videoService.queryHotVideoRankingList(typeStr, StringUtils.isNotBlank(uid) ? Long.parseLong(uid) : null, start, count));
	}

	/**
	 * 用户人气排行榜更新时间
	 *
	 * @param type 时间类型 0 24小时 1 周 2 月 3 年
	 * @return
	 */
	//用户人气排行榜更新时间
	@RequestMapping("/findUserPopularityRankingListUpdateTime")
	@ResponseBody
	public RespBody findUserPopularityRankingListUpdateTime(Integer type) {
		String time = "";
		switch (TimeTypeEnum.valueByCode(type)){
			case day:
				time = jedisService.get(BicycleConstants.TWENTY_FOUR_HOUR_USER_POPULARITY_UPDATE_TIME);
				if (StringUtils.isBlank(time)) {
					time = String.valueOf(new Date().getTime());
					jedisService.set(BicycleConstants.TWENTY_FOUR_HOUR_USER_POPULARITY_UPDATE_TIME, time);
				}
				break;
			case week:
				time = jedisService.get(BicycleConstants.WEEK_USER_POPULARITY_UPDATE_TIME);
				if (StringUtils.isBlank(time)) {
					time = String.valueOf(new Date().getTime());
					jedisService.set(BicycleConstants.WEEK_USER_POPULARITY_UPDATE_TIME, time);
				}
				break;
			case month:
				time = jedisService.get(BicycleConstants.MONTH_USER_POPULARITY_UPDATE_TIME);
				if (StringUtils.isBlank(time)) {
					time = String.valueOf(new Date().getTime());
					jedisService.set(BicycleConstants.MONTH_USER_POPULARITY_UPDATE_TIME, time);
				}
				break;
			case year:
				time = jedisService.get(BicycleConstants.YEAR_USER_POPULARITY_UPDATE_TIME);
				if (StringUtils.isBlank(time)) {
					time = String.valueOf(new Date().getTime());
					jedisService.set(BicycleConstants.YEAR_USER_POPULARITY_UPDATE_TIME, time);
				}
				break;
		}
		return respBodyWriter.toSuccess(time);
	}

	/**
	 * 用户人气排行榜
	 *
	 * @param type 时间类型 0 24小时 1 周 2 月 3 年
	 * @param start 开始条数
	 * @param count 总数
	 * @return
	 */
	@RequestMapping("findUserPopularityRankingList")
	@ResponseBody
	public RespBody findUserPopularityRankingList(Integer type,
												  @RequestParam(value = "start", required = false) Integer start,
												  @RequestParam(value = "count", required = false) Integer count) {
		String uid = this.request.getHeader("uid");
		String typeStr = "";
		if (type == 0) {
			typeStr = "day";
		} else if (type == 1) {
			typeStr = "week";
		} else if (type == 2) {
			typeStr = "month";
		} else if (type == 3) {
			typeStr = "year";
		}
		return respBodyWriter.toSuccess(ruserService.queryUserPopularityList(typeStr, StringUtils.isNotBlank(uid) ? Long.parseLong(uid) : null, start, count));
	}

	/**
	 * 土豪日榜
	 *
	 * @return
	 */
	@RequestMapping("findRichRankingDayList")
	@ResponseBody
    public RespBody findRichRankingDayList() {
		
		String uid = this.request.getHeader("uid");
		
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		Set<String> userIds = null;
		userIds = getSortedSet(BicycleConstants.RICH_DAY_RANKING_ZSET);
		for (String userId : userIds) {
            Map<String, String> user = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
            Map<String, String> u = new HashMap<String, String>();
            u.put("id", user.get("id"));
            u.put("pic", user.get("pic"));
            u.put("name", user.get("name"));
            u.put("isAnchor", user.get("isAnchor"));
            u.put("vipStat", user.get("vipStat"));
            u.put("sex", user.get("sex"));
            
//            attentionService.isAttention(Long.valueOf(uid), Long.valueOf(userId))).toString()
            u.put("isAttention", Integer.valueOf(attentionService.isAttention(Long.parseLong(uid), Long.parseLong(userId)) == 1 ?1 + attentionService.isAttention(Long.valueOf(userId), Long.parseLong(uid)) : 0).toString()   );
            
            Double points = jedisService.zscore(BicycleConstants.RICH_DAY_RANKING_ZSET, String.valueOf(userId));
            u.put("points",String.valueOf(points.longValue()) );
            if(user.get("stat")!=null&&(!user.get("stat").equals("0")||!user.get("stat").equals("1"))) {
            	userList.add(u);
            }
        }
		
		
		return respBodyWriter.toSuccess(userList);
	}
	
	/**
	 * 土豪周榜
	 *
	 * @return
	 */
	@RequestMapping("findRichRankingWeekList")
	@ResponseBody
    public RespBody findRichRankingWeekList() {
		
		String uid = this.request.getHeader("uid");
		
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		Set<String> userIds = null;
		userIds = getSortedSet(BicycleConstants.RICH_WEEK_RANKING_ZSET);
		for (String userId : userIds) {
            Map<String, String> user = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
            Map<String, String> u = new HashMap<String, String>();
            u.put("id", user.get("id"));
            u.put("pic", user.get("pic"));
            u.put("name", user.get("name"));
            u.put("isAnchor", user.get("isAnchor"));
            u.put("vipStat", user.get("vipStat"));
            u.put("sex", user.get("sex"));
            
            u.put("isAttention", Integer.valueOf(attentionService.isAttention(Long.parseLong(uid), Long.valueOf(userId)) == 1 ?1 + attentionService.isAttention(Long.valueOf(userId), Long.parseLong(uid)) : 0).toString()   );
            
            Double points = jedisService.zscore(BicycleConstants.RICH_WEEK_RANKING_ZSET, String.valueOf(userId));
            u.put("points",String.valueOf(points.longValue()) );
            if(user.get("stat")!=null&&(!user.get("stat").equals("0")||!user.get("stat").equals("1"))) {
            	userList.add(u);
            }
        }
		
		
		return respBodyWriter.toSuccess(userList);
	}
	
	/**
	 * 土豪月榜
	 *
	 * @return
	 */
	@RequestMapping("findRichRankingMonthList")
	@ResponseBody
    public RespBody findRichRankingMonthList() {
		
		String uid = this.request.getHeader("uid");
		
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		Set<String> userIds = null;
		userIds = getSortedSet(BicycleConstants.RICH_MONTH_RANKING_ZSET);
		for (String userId : userIds) {
            Map<String, String> user = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
            Map<String, String> u = new HashMap<String, String>();
            u.put("id", user.get("id"));
            u.put("pic", user.get("pic"));
            u.put("name", user.get("name"));
            u.put("isAnchor", user.get("isAnchor"));
            u.put("vipStat", user.get("vipStat"));
            u.put("sex", user.get("sex"));
            
            u.put("isAttention", Integer.valueOf(attentionService.isAttention(Long.parseLong(uid), Long.valueOf(userId)) == 1 ?1 + attentionService.isAttention(Long.valueOf(userId), Long.parseLong(uid)) : 0).toString()   );
            
            Double points = jedisService.zscore(BicycleConstants.RICH_MONTH_RANKING_ZSET, String.valueOf(userId));
            u.put("points",String.valueOf(points.longValue()) );
            if(user.get("stat")!=null&&(!user.get("stat").equals("0")||!user.get("stat").equals("1"))) {
            	userList.add(u);
            }
        }
		
		
		return respBodyWriter.toSuccess(userList);
	}
	
	/**
	 * 土豪总榜
	 *
	 * @return
	 */
	@RequestMapping("findRichRankingTotalList")
	@ResponseBody
    public RespBody findRichRankingTotalList() {
		
		String uid = this.request.getHeader("uid");
		
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		Set<String> userIds = null;
		userIds = getSortedSet(BicycleConstants.RICH_ALL_RANKING_ZSET);
		for (String userId : userIds) {
            Map<String, String> user = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
            Map<String, String> u = new HashMap<String, String>();
            u.put("id", user.get("id"));
            u.put("pic", user.get("pic"));
            u.put("name", user.get("name"));
            u.put("isAnchor", user.get("isAnchor"));
            u.put("vipStat", user.get("vipStat"));
            u.put("sex", user.get("sex"));
            
            u.put("isAttention", Integer.valueOf(attentionService.isAttention(Long.parseLong(uid), Long.valueOf(userId)) == 1 ?1 + attentionService.isAttention(Long.valueOf(userId), Long.parseLong(uid)) : 0).toString()   );
            
            Double points = jedisService.zscore(BicycleConstants.RICH_ALL_RANKING_ZSET, String.valueOf(userId));
            u.put("points",String.valueOf(points.longValue()) );
            if(user.get("stat")!=null&&(!user.get("stat").equals("0")||!user.get("stat").equals("1"))) {
            	userList.add(u);
            }
        }
		
		
		return respBodyWriter.toSuccess(userList);
	}
	
	/**
	 * 主播日榜
	 *
	 * @return
	 */
	@RequestMapping("findAnchorRankingDayList")
	@ResponseBody
    public RespBody findAnchorRankingDayList() {
		
		String uid = this.request.getHeader("uid");
		
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		Set<String> userIds = null;
		userIds = getSortedSet(BicycleConstants.ANCHOR_DAY_RANKING_ZSET);
		for (String userId : userIds) {
            Map<String, String> user = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
            Map<String, String> u = new HashMap<String, String>();
            u.put("id", user.get("id"));
            u.put("pic", user.get("pic"));
            u.put("name", user.get("name"));
            u.put("isAnchor", user.get("isAnchor"));
            u.put("vipStat", user.get("vipStat"));
            u.put("sex", user.get("sex"));
            
            u.put("isAttention", Integer.valueOf(attentionService.isAttention(Long.parseLong(uid), Long.valueOf(userId)) == 1 ?1 + attentionService.isAttention(Long.valueOf(userId), Long.parseLong(uid)) : 0 ).toString()  );
            
            Double points = jedisService.zscore(BicycleConstants.ANCHOR_DAY_RANKING_ZSET, String.valueOf(userId));
            u.put("points",String.valueOf(points.longValue()) );
            if(user.get("stat")!=null&&(!user.get("stat").equals("0")||!user.get("stat").equals("1"))) {
            	userList.add(u);
            }
        }
		
		
		return respBodyWriter.toSuccess(userList);
	}
	
	/**
	 * 主播周榜
	 *
	 * @return
	 */
	@RequestMapping("findAnchorRankingWeekList")
	@ResponseBody
    public RespBody findAnchorRankingWeekList() {
		
		String uid = this.request.getHeader("uid");
		
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		Set<String> userIds = null;
		userIds = getSortedSet(BicycleConstants.ANCHOR_WEEK_RANKING_ZSET);
		for (String userId : userIds) {
            Map<String, String> user = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
            Map<String, String> u = new HashMap<String, String>();
            u.put("id", user.get("id"));
            u.put("pic", user.get("pic"));
            u.put("name", user.get("name"));
            u.put("isAnchor", user.get("isAnchor"));
            u.put("vipStat", user.get("vipStat"));
            u.put("sex", user.get("sex"));
            
            u.put("isAttention", Integer.valueOf(attentionService.isAttention(Long.parseLong(uid), Long.valueOf(userId)) == 1 ?1 + attentionService.isAttention(Long.valueOf(userId), Long.parseLong(uid)) : 0 ).toString()  );
            
            Double points = jedisService.zscore(BicycleConstants.ANCHOR_WEEK_RANKING_ZSET, String.valueOf(userId));
            u.put("points",String.valueOf(points.longValue()) );
            if(user.get("stat")!=null&&(!user.get("stat").equals("0")||!user.get("stat").equals("1"))) {
            	userList.add(u);
            }
        }
		
		
		return respBodyWriter.toSuccess(userList);
	}
	
	/**
	 * 主播月榜
	 *
	 * @return
	 */
	@RequestMapping("findAnchorRankingMonthList")
	@ResponseBody
    public RespBody findAnchorRankingMonthList() {
		
		String uid = this.request.getHeader("uid");
		
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		Set<String> userIds = null;
		userIds = getSortedSet(BicycleConstants.ANCHOR_MONTH_RANKING_ZSET);
		for (String userId : userIds) {
            Map<String, String> user = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
            Map<String, String> u = new HashMap<String, String>();
            u.put("id", user.get("id"));
            u.put("pic", user.get("pic"));
            u.put("name", user.get("name"));
            u.put("isAnchor", user.get("isAnchor"));
            u.put("vipStat", user.get("vipStat"));
            u.put("sex", user.get("sex"));
            
            u.put("isAttention", Integer.valueOf(attentionService.isAttention(Long.parseLong(uid), Long.valueOf(userId)) == 1 ?1 + attentionService.isAttention(Long.valueOf(userId), Long.parseLong(uid)) : 0 ).toString()  );
            
            Double points = jedisService.zscore(BicycleConstants.ANCHOR_MONTH_RANKING_ZSET, String.valueOf(userId));
            u.put("points",String.valueOf(points.longValue()) );
            if(user.get("stat")!=null&&(!user.get("stat").equals("0")||!user.get("stat").equals("1"))) {
            	userList.add(u);
            }
        }
		
		
		return respBodyWriter.toSuccess(userList);
	}
	
	/**
	 * 主播总榜
	 *
	 * @return
	 */
	@RequestMapping("findAnchorRankingTotalList")
	@ResponseBody
    public RespBody findAnchorRankingTotalList() {
		
		String uid = this.request.getHeader("uid");
		
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		Set<String> userIds = null;
		userIds = getSortedSet(BicycleConstants.ANCHOR_ALL_RANKING_ZSET);
		for (String userId : userIds) {
            Map<String, String> user = jedisService.getMapByKey(BicycleConstants.USER_INFO + userId);
            Map<String, String> u = new HashMap<String, String>();
            u.put("id", user.get("id"));
            u.put("pic", user.get("pic"));
            u.put("name", user.get("name"));
            u.put("isAnchor", user.get("isAnchor"));
            u.put("vipStat", user.get("vipStat"));
            u.put("sex", user.get("sex"));
            
            u.put("isAttention", Integer.valueOf(attentionService.isAttention(Long.parseLong(uid), Long.parseLong(userId)) == 1 ?1 + attentionService.isAttention(Long.valueOf(userId), Long.parseLong(uid)) : 0).toString()  );
            
            Double points = jedisService.zscore(BicycleConstants.ANCHOR_ALL_RANKING_ZSET, String.valueOf(userId));
            u.put("points",String.valueOf(points.longValue()) );
            if(user.get("stat")!=null&&(!user.get("stat").equals("0")||!user.get("stat").equals("1"))) {
            	userList.add(u);
            }
        }
		
		
		return respBodyWriter.toSuccess(userList);
	}
	
	private Set<String> getSortedSet(String key) {
    	Set<String> set = jedisService.getSortedSetFromShardByDesc(key); //从缓存中取列表(admin定时任务刷缓存)
    	if (set == null) {
    		set = new HashSet<String>();
    	}
    	return set;
    }
	
	@RequestMapping("rankingListRules")
	public String rankingListRules() {
		return "html5/hitList";
	}


}
