package com.busap.vcs.web.h5;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * h5页面有关用户信息的controller
 *
 */
@Controller
@RequestMapping("/page/feedback")
public class FeedbackH5Controller extends BaseH5Controller {

	private Logger logger = LoggerFactory.getLogger(FeedbackH5Controller.class);

	@Autowired
	protected HttpServletRequest request;
	
	/**
	 * 添加反馈
	 * @return
	 */
	@RequestMapping("/addFeedback")
	public String addFeedback() {
		return "html5/personalCenter/feedBack";
	}
}
