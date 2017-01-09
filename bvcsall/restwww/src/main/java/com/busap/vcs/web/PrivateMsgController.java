package com.busap.vcs.web;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.service.PrivateMsgService;

@Controller
@RequestMapping("privateChat")
public class PrivateMsgController {

	@Resource(name="privateMsgService")
	private PrivateMsgService privateMsgService;
	
	@RequestMapping("getUnread")
	@ResponseBody
	public Map<String,Object> getUnreadAll(HttpServletRequest request){
		String uid = request.getHeader("uid");
		if(StringUtils.isBlank(uid) || !StringUtils.isNumeric(uid)){
			return null;
		}
		return privateMsgService.searchUnreadMsg(uid);
	}
}
