package com.busap.vcs.web.h5;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.VideoService;

/**
 * h5 页面有关主页信息的controller
 *
 */
@Controller
@RequestMapping("/page")
public class HomeH5Controller extends BaseH5Controller{

	private Logger logger = LoggerFactory.getLogger(HomeH5Controller.class);

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "videoService")
	private VideoService videoService;

    @Resource(name="activityService")
    ActivityService activityService; 
	
	/**
	 * h5首页
	 * @return
	 */
	@RequestMapping("/homePage")
	public String userHomePage(String uid) {
		try {
			logger.info("homePage,uid={}",uid);
			List<Activity> activityList=activityService.findAllByGroupType(0,"");  //获得首页分类信息
			
			List<Video> videoList = null;
			if (activityList != null && activityList.size() > 0){  //获得第一个分类下的视频列表
				Long activityId = activityList.get(0).getId();
				videoList = videoService.findActVideos(0l, activityId, uid, 20,1);
			}
			
			parseUserInfo(videoList);
			this.request.setAttribute("activityList",activityList ); 
	    	this.request.setAttribute("videoList",videoList); 
	    	this.request.setAttribute("uid",uid); 
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
    	return "html5/default/index";
	}
}
