package com.busap.vcs.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.service.CoverService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller
@RequestMapping("/cover")
public class CoverController {

	private Logger logger = LoggerFactory.getLogger(CoverController.class);

	@Resource(name="coverService")
	private CoverService coverService;
	
	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();

	//获得封面图片
	@RequestMapping("/getCover")
	@ResponseBody
	@Transactional
	public RespBody getCover() {
		return respBodyWriter.toSuccess(coverService.getRandomActiveCover());
	}
}
