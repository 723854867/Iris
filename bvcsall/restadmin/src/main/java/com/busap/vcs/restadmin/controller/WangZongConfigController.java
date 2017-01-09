package com.busap.vcs.restadmin.controller;

import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.service.JedisService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller()
@RequestMapping("wangzongConfig")
public class WangZongConfigController{

	private static final Logger logger = LoggerFactory.getLogger(WangZongConfigController.class);
	
	@Resource(name="jedisService")
	private JedisService jedisService;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@RequestMapping("info")
	public String info(HttpServletRequest req) {
		Set<String> set = jedisService.getSetFromShard("wangzong_info");
		req.setAttribute("info", set);
		return "wangzong/info";
	}
	
	@RequestMapping("clearRank")
	@ResponseBody
	public RespBody clearRank(HttpServletRequest req) {
		jedisService.delete("wangzong_rank");
		return this.respBodyWriter.toSuccess(1); 
	}
	
	@RequestMapping("deleteInfo")
	public String deleteInfo(String member,HttpServletRequest req) {
		jedisService.deleteSetItemFromShard("wangzong_info",member);
		return "redirect:info";
	}
	
	
	@RequestMapping("addInfo")
	public String addInfo(String uid,String giftIds,HttpServletRequest req){
		giftIds = giftIds.replace("，", ",");
		if (giftIds.endsWith(",")){
			giftIds.substring(0, giftIds.length()-1);
		}
		jedisService.setValueToSetInShard("wangzong_info", uid+"|"+giftIds);
		return "redirect:info";
	}
	
}
