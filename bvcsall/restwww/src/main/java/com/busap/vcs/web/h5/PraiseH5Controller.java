package com.busap.vcs.web.h5;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * h5页面关注的controller
 *
 */
@Controller
@RequestMapping("/page/praise")
public class PraiseH5Controller extends BaseH5Controller {

	private Logger logger = LoggerFactory.getLogger(PraiseH5Controller.class);

	@Autowired
	protected HttpServletRequest request;

	@RequestMapping("/praiseList")
	public String praiseList(String videoId) {
		this.request.setAttribute("videoId", videoId);
    	return "html5/default/praiseList";
	}
}
