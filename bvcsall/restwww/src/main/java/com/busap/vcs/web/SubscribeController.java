package com.busap.vcs.web;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.service.SubscribeService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller
@RequestMapping("/subscribe")
public class SubscribeController {

	private Logger logger = LoggerFactory.getLogger(SubscribeController.class);

	@Autowired
	protected HttpServletRequest request;
	
	@Resource(name="subscribeService")
	SubscribeService subscribeService;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Resource(name="videoService") 
    VideoService videoService; 
	
	//用户订阅
	@RequestMapping("subscribe")
	@ResponseBody
	public RespBody subscribe(Long activityId,Integer operate,String dateFrom){
		String uid =  this.request.getHeader("uid");
		logger.info("uid={},activityId={}", uid,activityId);
		
		if (operate == 1) { //订阅
			if (subscribeService.isSubscribed(Long.parseLong(uid),activityId) == 1) 
				return this.respBodyWriter.toError(ResponseCode.CODE_342.toString(), ResponseCode.CODE_342.toCode());
			return respBodyWriter.toSuccess(subscribeService.subscribe(Long.parseLong(uid),activityId,dateFrom));
		} else {  //取消订阅
			return respBodyWriter.toSuccess(subscribeService.cancelSubscribe(Long.parseLong(uid),activityId));
		}
			
	}
	
	//获得用户订阅活动列表
	@RequestMapping("getMyActivityList")
	@ResponseBody
	public RespBody getMyActivityList(){
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}", uid);
		return respBodyWriter.toSuccess(subscribeService.getMyActivityList(Long.parseLong(uid)));
	}
	
	//获得用户活动视频列表
	@RequestMapping("getMyActivityVideoList")
	@ResponseBody
	public RespBody getMyActivityVideoList(Long timestamp,Integer count){
		String uid =  this.request.getHeader("uid");
		logger.info("uid={},timestamp={},count={}", uid,timestamp,count);
		List<Video> list = subscribeService.getMyActivityVideoList(Long.parseLong(uid),timestamp,count);
		if (list == null || list.size() ==0)
			return respBodyWriter.toSuccess(videoService.findHotVideos((timestamp==null||timestamp.longValue()==0)?new Date():new Date(timestamp), count,uid));
		return respBodyWriter.toSuccess(list);
	}
}
