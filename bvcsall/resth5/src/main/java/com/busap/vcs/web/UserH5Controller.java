package com.busap.vcs.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.VideoService;
import com.busap.vcs.service.impl.NotificationJPushUtil;

/**
 * h5页面有关用户信息的controller
 *
 */
@Controller
@RequestMapping("/user")
public class UserH5Controller extends BaseH5Controller {

	private Logger logger = LoggerFactory.getLogger(UserH5Controller.class);

	@Autowired
	NotificationJPushUtil util;

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "ruserService")
	private RuserService ruserService;

	@Resource(name = "videoService")
	private VideoService videoService;
	
	@Resource(name="attentionService")
	private AttentionService attentionService;

	/**
	 * h5个人中心
	 * @param uid
	 * @return
	 */
	@RequestMapping("/userCenter")
	public String userCenter(Long uid) {
		try {
			logger.info("H5,userCenter,uid={}",uid);
			String access_token = this.request.getParameter("access_token");
			logger.info("thirdpart callback to userDetail,access_token={}",access_token);
			//用户信息
			Ruser ruser = ruserService.find(uid);
			//用户视频信息
			this.request.setAttribute("ruser",ruser ); 
			
			this.request.setAttribute("access_token",access_token); 
			this.request.setAttribute("uid",uid); 
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/personalCenter/personalCenter";
	}
	
	/**
	 * h5个人主页
	 * @param uid
	 * @return
	 */
	@RequestMapping("/userDetail")
	public String userDetail(Long userId,Long uid) {
//		return "redirect:http://www.wopaitv.com";
		try {
			logger.info("H5,userDetail,userId={},uid={}",userId,uid);
			//用户信息
			Ruser ruser = ruserService.find(userId);
			//用户视频信息
			List<Video> videoList = videoService.findUserVideos(userId, null, 10, uid);
			ruser.setIsAttention(attentionService.isAttention(uid, userId));
			this.request.setAttribute("ruser",ruser ); 
			this.request.setAttribute("videoList",videoList ); 
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
		return "html5/personalCenter/personalDetail";
	}
	
	/**
	 * 获得推荐用户
	 * @param uid
	 * @return
	 */
	@RequestMapping("/getRecommendUser")
	public String getRecommendUser(Long uid) {
		try {
			logger.info("uid={}",uid);
			
			List<Ruser> ruserList = attentionService.getDynamicRecommend(uid,20);
			this.request.setAttribute("ruserList",ruserList ); 
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
		return "html5/log_reg/Recommend";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "html5/log_reg/login";
	}
	
	@RequestMapping("/register")
	public String register() {
		return "html5/log_reg/register";
	}
	
	@RequestMapping("/forgetPwd")
	public String forgetPwd() {
		return "html5/log_reg/forgetPwd";
	}
	
	
}
