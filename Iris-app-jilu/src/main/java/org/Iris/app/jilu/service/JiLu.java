package org.Iris.app.jilu.service;

import org.Iris.app.jilu.common.JiLuConfig;
import org.Iris.app.jilu.service.jms.JmsService;
import org.Iris.core.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JiLu extends App {
	
	private static final Logger logger = LoggerFactory.getLogger(JiLu.class);
	
	private JmsService jmsService;
	
	public JiLu(String name) {
		super(name);
	}

	@Override
	public void bootstrap() {
		logger.info("App - {} startup!", name);
		
		// init jms service
		this.jmsService.init(JiLuConfig.getEnv());
	}

	@Override
	public void stop() {
		logger.info("App - {} shutdown!", name);
	}
	
	public void setJmsService(JmsService jmsService) {
		this.jmsService = jmsService;
	}
}
