package com.busap.vcs.listener;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.service.JedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounter implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event) {
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		//监听session是否销毁 如销毁清除redis中审核用户信息
		String uname = (String)se.getSession().getAttribute("uname");
		if(StringUtils.isNotBlank(uname)) {
			ApplicationContext applicationContext =WebApplicationContextUtils.getWebApplicationContext(se.getSession().getServletContext());
			JedisService jedisService =(JedisService)applicationContext.getBean("jedisService");
			jedisService.deleteSortedSetItemFromShard(BicycleConstants.CHECK_GROUP, uname);
		}

	}
}