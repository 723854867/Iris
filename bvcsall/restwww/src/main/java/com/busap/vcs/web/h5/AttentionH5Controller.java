package com.busap.vcs.web.h5;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.service.AttentionService;

/**
 * h5页面关注的controller
 *
 */
@Controller
@RequestMapping("/page/attention")
public class AttentionH5Controller extends BaseH5Controller {

	private Logger logger = LoggerFactory.getLogger(AttentionH5Controller.class);

	@Autowired
	protected HttpServletRequest request;

	@Resource(name="attentionService")
	private AttentionService attentionService;

	/**
	 * 关注人的视频动态页面
	 * @param videoId
	 * @return
	 */
	@RequestMapping("/attentionVideoList")
	public String attentionVideoList(String uid) {
		try {
			logger.info("H5,attentionVideoList");
			logger.info("uid={}",uid);
			
			List<Ruser> ruserList = attentionService.getDynamicRecommend(Long.parseLong(uid),1);
			this.request.setAttribute("ruserList",ruserList ); 
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/default/attention";
	}
	
	/**
	 * 动态推荐人页面
	 * @param uid
	 * @return
	 */
	@RequestMapping("/dynamicRecommend")
	public String dynamicRecommend(String uid) {
		try {
			logger.info("H5,dynamicRecommend");
			logger.info("uid={}",uid);
			
			List<Ruser> ruserList = attentionService.getDynamicRecommend(Long.parseLong(uid),20);
			this.request.setAttribute("ruserList",ruserList ); 
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/default/interest";
	}
	
	/**
	 * 我的关注人页面
	 * @param uid
	 * @return
	 */
	@RequestMapping("/getAttention")
	public String getAttention() {
		try {
			logger.info("H5,getAttention");
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
    	return "html5/personalCenter/myAttention";
	}
	
	/**
	 * 我的粉丝页面
	 * @param uid
	 * @return
	 */
	@RequestMapping("/getFans")
	public String getFans() {
		try {
			logger.info("H5,getFans");
		} catch (Exception e) {
			return "html5/default/errorpage";
		}
		
		return "html5/personalCenter/myFans";
	}
	
}
