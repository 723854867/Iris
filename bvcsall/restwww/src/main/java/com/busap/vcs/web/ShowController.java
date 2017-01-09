package com.busap.vcs.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.service.ShowVideoService;
//import com.busap.vcs.data.mapper.AttentionDAO;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller
@RequestMapping("/show")
public class ShowController {

	private Logger logger = LoggerFactory.getLogger(ShowController.class);

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Autowired
	protected HttpServletRequest request;
	
	@Resource(name = "showVideoService")
	private ShowVideoService showVideoService;
	
	// 获得我拍秀列表
	@RequestMapping("/getShowList")
	@ResponseBody
	@Transactional
	public RespBody getShowList(Long timestamp,Integer count) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={},getShowList", uid);
		return respBodyWriter.toSuccess(showVideoService.getShowList(uid,timestamp, count));
	}
}
