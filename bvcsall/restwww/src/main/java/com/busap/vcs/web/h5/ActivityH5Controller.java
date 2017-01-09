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
import com.busap.vcs.data.entity.Banner;
import com.busap.vcs.data.entity.HotLabel;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.BannerService;
import com.busap.vcs.service.HotLabelService;
import com.busap.vcs.service.impl.NotificationJPushUtil;

/**
 * h5页面有关用户信息的controller
 *
 */
@Controller
@RequestMapping("/page/activity")
public class ActivityH5Controller extends BaseH5Controller {

	private Logger logger = LoggerFactory.getLogger(ActivityH5Controller.class);

	@Autowired
	NotificationJPushUtil util;

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "bannerService")
	private BannerService bannerService;
	
	@Resource(name="hotLabelService")
    HotLabelService hotLabelService;
	
	@Resource(name="activityService")
	ActivityService activityService; 
	
	/**
	 * 活动(发现页)首页
	 * @param uid
	 * @return
	 */
	@RequestMapping("/home")
	public String home() {
		try {
			List<Banner> bannerList =  bannerService.findAllBanner(0);
			List<HotLabel> hotLabelList = hotLabelService.find4ByShowOrder();
			
			List<Activity> activityList= activityService.findAllByGroupType(2,"");
			if(activityList!=null && activityList.size()>4) {
				activityList = activityList.subList(0, 4);
			}
			this.request.setAttribute("bannerList",bannerList ); 
			this.request.setAttribute("hotLabelList",hotLabelList ); 
			this.request.setAttribute("activityList",activityList ); 
			return "html5/activities/activities";
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
	}
	
	/**
	 * 活动详情页面
	 * @param activityId
	 * @return
	 */
	@RequestMapping("/activityIndex")
	public String activityIndex(Long activityId) {
		try {
			logger.info("H5,activityIndex ,activityId={}",activityId);
			Activity activity = activityService.find(activityId);
			
			this.request.setAttribute("activity",activity ); 
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
    	return "html5/activities/active_index";
	}
	
	/**活动列表页面
	 * @return
	 */
	@RequestMapping("/activityList")
	public String activityList() {
    	return "html5/activities/active_list";
	}
	
	/**活动列表页面（标签）
	 * @return
	 */
	@RequestMapping("/activityTag")
	public String activityTag(String tag) {
		logger.info("H5,activityTag ,tag={}",tag);
		this.request.setAttribute("tag",tag ); 
		return "html5/activities/active_tag";
	}
}
