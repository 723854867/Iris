package org.Iris.app.jilu.service;

import org.Iris.core.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JiLu extends App {
	
	private static final Logger logger = LoggerFactory.getLogger(JiLu.class);
	
	public JiLu(String name) {
		super(name);
	}

	@Override
	public void bootstrap() {
		logger.info("App - {} startup!", name);
	}

	@Override
	public void stop() {
		logger.info("App - {} shutdown!", name);
	}
}
