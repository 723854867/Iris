package com.busap.vcs.restadmin.controller;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import com.busap.vcs.webcomn.U;

/**
 * 菜单暂时是写死的
 * @author meizhiwen
 *
 */
@Controller
@RequestMapping("index")
public class IndexController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Value("${ws_server_url}")
	private String ws_url;
	
	@Resource(name = "respBodyBuilder")
    protected RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@RequestMapping("welcome")
	public String index(){
		return "index/welcome";
	}
	
	@RequestMapping("wsurl")
	@ResponseBody
	public RespBody findWsUrl(){
		return this.respBodyWriter.toSuccess(ws_url+"?uid="+U.getUid());
	}
}
