package org.Iris.app.jilu.service;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.service.realm.aliyun.AliyunService;
import org.Iris.app.jilu.service.realm.jms.JmsService;
import org.Iris.app.jilu.service.realm.weixin.WeiXinService;
import org.Iris.app.jilu.service.realm.wyyx.WyyxService;
import org.Iris.core.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JiLu extends App {
	
	private static final Logger logger = LoggerFactory.getLogger(JiLu.class);
	
	private JmsService jmsService;
	private AliyunService aliyunService;
	private WyyxService wyyxService;
	private WeiXinService weiXinService;
	
	public JiLu(String name) {
		super(name);
	}

	@Override
	public void bootstrap() {
		logger.info("App - {} startup!", name);
		
		// init jms service
		this.jmsService.init(AppConfig.getEnv());
		// init aliyun service
		this.aliyunService.init();
		// init wyyx service
		this.wyyxService.init();
		// init weixin service
		this.weiXinService.init();
	}

	@Override
	public void stop() {
		logger.info("App - {} shutdown!", name);
	}
	
	public void setJmsService(JmsService jmsService) {
		this.jmsService = jmsService;
	}
	
	public void setAliyunService(AliyunService aliyunService) {
		this.aliyunService = aliyunService;
	}

	public void setWyyxService(WyyxService wyyxService) {
		this.wyyxService = wyyxService;
	}

	public void setWeiXinService(WeiXinService weiXinService) {
		this.weiXinService = weiXinService;
	}
	
}
