package com.busap.vcs.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * h5 页面有关主页信息的controller
 *
 */
@Controller
public class RankH5Controller extends BaseH5Controller{

	private Logger logger = LoggerFactory.getLogger(RankH5Controller.class);

	/**
	 * h5首页
	 * @return
	 */
	@RequestMapping("/rank")
	public String rank() {
    	return "html5/default/rangking";
	}
}
