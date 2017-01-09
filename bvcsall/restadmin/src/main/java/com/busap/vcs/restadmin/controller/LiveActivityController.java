package com.busap.vcs.restadmin.controller;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Gift;
import com.busap.vcs.data.entity.LiveActivity;
import com.busap.vcs.data.model.ExportWopaiNormalUser;
import com.busap.vcs.data.model.LiveActivityDisplay;
import com.busap.vcs.restadmin.utils.CommonUtils;
import com.busap.vcs.service.*;
import com.busap.vcs.service.utils.CSVUtils;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;
import com.busap.vcs.webcomn.U;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.util.*;

@Controller()
@RequestMapping("liveActivity")
public class LiveActivityController {

//	private Logger logger = LoggerFactory.getLogger(LiveActivityController.class);

	@Autowired
	protected HttpServletRequest request;


	@Resource(name = "liveActivityService")
	private LiveActivityService liveActivityService;

	@Resource(name = "giftService")
	private GiftService giftService;

	@Resource
	private RoomService roomService;

	@Resource
	private RuserLiveActivityService ruserLiveActivityService;

	@Resource
	private JedisService jedisService;

	@Value("${files.localpath}")
	private String basePath;

	@RequestMapping("list")
	public String getList() throws Exception {
		return "liveActivity/list";
	}

	@RequestMapping(value = {"save"})
	public String save(HttpServletRequest req, Long id, String title, String description, Integer status, Integer type,
					   String cover, int showCountOfTop, String startTime, String endTime, Integer orderNum) {
		String[] gifts = req.getParameterValues("gifts");
		Long uid = U.getUid();
		liveActivityService.save(gifts, id, title, description, status, type, cover, showCountOfTop, startTime, endTime, uid, orderNum);
		return "redirect:list";
	}

	@RequestMapping(value = {"updateStatus"})
	@ResponseBody
	public String save(@RequestParam(value = "ids", required = true)  String ids,
					   @RequestParam(value = "status", required = true) Integer status) {
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			LiveActivity activity = liveActivityService.find(Long.parseLong(id));
			activity.setStatus(status);
			liveActivityService.update(activity);
		}
		return "ok";
	}


	@RequestMapping("edit")
	public String editActivity(HttpServletRequest req,@RequestParam(value = "activityId", required = false)  Long activityId) throws Exception {
		if (activityId != null) {
			List<Gift> gifts = giftService.findExclusiveGifts();
			LiveActivity activity = this.liveActivityService.find(activityId);
			req.setAttribute("activity", activity);
			if (activity.getGiftIds() != null) {
				String giftIds = activity.getGiftIds();
				List<String> ids = Arrays.asList(giftIds.split(","));
				for (Gift gift : gifts) {
					if (ids.contains(String.valueOf(gift.getId()))) {
						gift.setState(1);
					} else {
						gift.setState(0);
					}
				}
				req.setAttribute("gifts", gifts);
			}
		}else{
			List<Gift> gifts = giftService.findExclusiveGiftsByState(1);
			for (Gift gift : gifts) {
				gift.setState(0);
			}
			req.setAttribute("gifts", gifts);
		}
		return "liveActivity/edit";
	}

	@RequestMapping("deleteLiveActivity")
	public String deleteLiveActivity(@RequestParam(value = "activityId", required = true)  Long activityId) throws Exception {
		liveActivityService.deleteById(activityId);
		return "redirect:list";
	}
	/**
	 * 直播活动管理
	 * @param queryPage
	 * @param title
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping("queryLiveActivityList")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryLiveActivityList(@ModelAttribute("queryPage") JQueryPage queryPage,
													@RequestParam(value = "title", required = false)  String title,
													@RequestParam(value = "status", required = false) Integer status,
													@RequestParam(value = "startTime", required = false) String startTime,
													@RequestParam(value = "endTime", required = false) String endTime){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("status",status);
		params.put("title",title);
		params.put("startTime",startTime);
		params.put("endTime",endTime);
		List<LiveActivityDisplay> list = liveActivityService.queryLiveActivities(params);
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("total",page.getTotalResult());
		map.put("rows",list);
		return map;
	}

	@RequestMapping("forwardLiveActivityDetail")
	public ModelAndView forwardLiveActivityDetail(Long liveActivityId){
		ModelAndView modelAndView = new ModelAndView();
		LiveActivity liveActivity = liveActivityService.queryLiveActivityById(liveActivityId);
		Map<String,String> liveData = roomService.queryLiveDataByLiveActivityId(liveActivityId);
		Integer signCount = ruserLiveActivityService.getCountByLiveActivityId(liveActivityId);
		liveData.put("signCount", String.valueOf(signCount));
		liveData.put("topCount", String.valueOf(jedisService.getSortedSetSizeFromShard(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+liveActivityId)));
		Set<Tuple> set = jedisService.zrevrangeWithScores(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+liveActivityId, 0L, -1L);
		Double tPoint = Double.valueOf(0);
		for (Tuple tuple:set) {
			tPoint += tuple.getScore();//获得收到专属礼物金豆数
		}
		String totalPoint = String.valueOf(tPoint);
		if (totalPoint != null && totalPoint.contains(".")) {
				totalPoint = totalPoint.substring(0,totalPoint.indexOf("."));
		}
		liveData.put("totalPoint", String.valueOf(totalPoint));
		
		
		Map sCountMap=new HashMap();
		sCountMap.put("liveActivityId", liveActivityId);
		Long senderCount=roomService.findSenderCount(sCountMap);
		liveData.put("senderCount", senderCount!=null?senderCount.toString():"0");
		
		sCountMap.put("is_exclusive", 1);
		Long eSenderCount=roomService.findSenderCount(sCountMap);
		liveData.put("eSenderCount", eSenderCount!=null?eSenderCount.toString():"0");
		
		
		Map sDCMap=new HashMap();
		sDCMap.put("liveActivityId", liveActivityId);
		Long sDC=roomService.findSumDPByLiveActivityId(sDCMap);
		liveData.put("sDC", sDC!=null?sDC.toString():"0");
		Long distinctLiveNum = roomService.queryDistinctLiveNumByLiveActivityId(liveActivityId);
		liveData.put("distinctLiveNum", String.valueOf(distinctLiveNum));
		modelAndView.addObject("liveData",liveData);
		modelAndView.addObject("liveActivity",liveActivity);
		modelAndView.setViewName("liveActivity/detail");
		return modelAndView;
	}

	@RequestMapping("queryRuserLiveActivityDetailRecord")
	@ResponseBody
	public Map<String,Object> queryRuserLiveActivityDetailRecord(Long liveActivityId,@ModelAttribute("queryPage") JQueryPage queryPage){
		Map<String,Object> map = new HashMap<String, Object>();
		Set<Tuple> set = jedisService.zrevrangeWithScores(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+liveActivityId, (queryPage.getPage()-1)*queryPage.getRows().longValue(), queryPage.getPage()*queryPage.getRows().longValue()-1);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Tuple tuple:set) {
			Map<String,Object> rankInfoMap = new HashMap<String,Object>();
			String receiverId = tuple.getElement();//获得接受者id
			String giftNumber = String.valueOf(tuple.getScore());//获得收到专属礼物金豆数
			if (giftNumber != null && giftNumber.contains(".")) {
				giftNumber = giftNumber.substring(0,giftNumber.indexOf("."));
			}
			Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+receiverId);
			rankInfoMap.put("userId", userInfo.get("id"));
			rankInfoMap.put("name",userInfo.get("name"));
			rankInfoMap.put("giftNumber", (giftNumber==null || "".equals(giftNumber) || "null".equals(giftNumber))?0:giftNumber);
			
			Map params=new HashMap();
			params.put("liveActivityId", liveActivityId);
			params.put("userId", userInfo.get("id"));
			Map<String,String> liveData = roomService.selectLiveDataByLiveActivityIdAndUserId(params);
			
			rankInfoMap.put("maxAccessNumber", liveData.get("maxAccessNumber"));
			
			list.add(rankInfoMap);
		}
		map.put("total",list.size());
		map.put("rows",list);
		return map;
	}

	@RequestMapping("exportRuserLiveActivityDetailRecord")
	@ResponseBody
	public void exportRuserLiveActivityDetailRecord(HttpServletResponse response,Long liveActivityId){
		Set<Tuple> set = jedisService.zrevrangeWithScores(BicycleConstants.EXCLUSIVE_GIFT_RECORD_+liveActivityId, 0L, -1L);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> params = new HashMap<String, Object>(2);
		for (Tuple tuple:set) {
			Map rankInfoMap = new LinkedHashMap<String, String>();
			String receiverId = tuple.getElement();//获得接受者id
			String giftNumber = String.valueOf(tuple.getScore());//获得收到专属礼物金豆数
			if (giftNumber != null && giftNumber.contains(".")) {
				giftNumber = giftNumber.substring(0,giftNumber.indexOf("."));
			}
			Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+receiverId);
			rankInfoMap.put("1", userInfo.get("id"));
			rankInfoMap.put("2",userInfo.get("name"));
			rankInfoMap.put("3", (giftNumber==null || "".equals(giftNumber) || "null".equals(giftNumber))?0:giftNumber);
			params.put("liveActivityId", liveActivityId);
			params.put("userId", userInfo.get("id"));
			Map<String,String> liveData = roomService.selectLiveDataByLiveActivityIdAndUserId(params);
			rankInfoMap.put("4", String.valueOf(liveData.get("maxAccessNumber")));
			list.add(rankInfoMap);
		}
		LinkedHashMap<String,String> headers = new LinkedHashMap<String, String>(4);
		headers.put("1", "用户ID");
		headers.put("2", "用户昵称");
		headers.put("3", "金豆数");
		headers.put("4", "访问数");
		File file = CSVUtils.createCSVFile(list, headers, basePath+"/exportExcel/", "直播活动排行榜");
		CommonUtils.download(file.getPath(),response);
	}



}
