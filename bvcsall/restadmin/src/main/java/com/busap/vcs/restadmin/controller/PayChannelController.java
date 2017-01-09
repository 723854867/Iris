package com.busap.vcs.restadmin.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.service.JedisService;

@Controller()
@RequestMapping("payChannel")
public class PayChannelController {

	@Autowired
	JedisService jedisService;

	@RequestMapping("index")
	public String index(String isUpdate,HttpServletRequest req) throws Exception {
		
		if (isUpdate != null && "1".equals(isUpdate)){
			String[] iosChannel = req.getParameterValues("iosChannel");
			String[] androidChannel = req.getParameterValues("androidChannel");
			
			jedisService.delete(BicycleConstants.PAY_CHANNEL_IOS);
			if (iosChannel != null && iosChannel.length > 0) {
				for (int i=0 ;i <iosChannel.length;i++) {
					jedisService.setValueToSetInShard(BicycleConstants.PAY_CHANNEL_IOS, iosChannel[i]);
				}
			}
			
			jedisService.delete(BicycleConstants.PAY_CHANNEL_ANDROID);
			if (androidChannel != null && androidChannel.length > 0) {
				for (int i=0 ;i <androidChannel.length;i++) {
					jedisService.setValueToSetInShard(BicycleConstants.PAY_CHANNEL_ANDROID, androidChannel[i]);
				}
			}
		} 
		
		String iosChannelString = "";
		String androidChannelString = "";
		Set<String> iosSet = jedisService.getSetFromShard(BicycleConstants.PAY_CHANNEL_IOS);
		Set<String> androidSet =jedisService.getSetFromShard(BicycleConstants.PAY_CHANNEL_ANDROID);
		for (String channel:iosSet) {
			iosChannelString += channel + ",";
		}
		for (String channel:androidSet) {
			androidChannelString += channel + ",";
		}
		req.setAttribute("iosChannelString", iosChannelString);
		req.setAttribute("androidChannelString", androidChannelString);
		
	
		return "payChannel/index";
	}

}
