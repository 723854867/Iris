package org.Iris.app.jilu.service;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.service.realm.aliyun.AliyunService;
import org.Iris.app.jilu.service.realm.jms.JmsService;
import org.Iris.app.jilu.service.realm.pay.PayService;
import org.Iris.app.jilu.service.realm.wyyx.SmsService;
import org.Iris.app.jilu.service.realm.wyyx.WyyxService;
import org.Iris.app.jilu.web.filter.Constants;
import org.Iris.core.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JiLu extends App {
	
	private static final Logger logger = LoggerFactory.getLogger(JiLu.class);
	
	private JmsService jmsService;
	private AliyunService aliyunService;
	private PayService payService;
	private WyyxService wyyxService;
	private SmsService smsService;
	
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
		this.payService.init();
		this.smsService.init();
		Constants.init();
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

	public void setPayService(PayService payService) {
		this.payService = payService;
	}

	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}
	
}
