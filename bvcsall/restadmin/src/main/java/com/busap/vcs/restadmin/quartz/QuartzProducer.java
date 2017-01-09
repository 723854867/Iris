package com.busap.vcs.restadmin.quartz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzProducer {
	private static final Logger logger = LoggerFactory.getLogger(QuartzProducer.class);

	private static String location = "quartz.properties";

	private Scheduler scheduler = null;

	public void initScheduler() throws Exception {
		String dir = System.getProperty("user.dir");
		File fDir = new File(dir);
		String parentDir = fDir.getParent();
		String producerConfig = parentDir + File.separator + "quartz.properties";
		Properties properties = new Properties();
		try {
			InputStream is = new FileInputStream(producerConfig);
			logger.debug("------------------------quartz.properties address :"+producerConfig+"------------------------");
			System.out.println("*************************"+producerConfig+"***********************************");
			properties.load(is);
		} catch (FileNotFoundException e) {
			logger.error("=================quartz.properties is not found at {}===============",producerConfig);
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(location));
		}

		if (properties.isEmpty()) {
			logger.error("=================quartz config is empty=================");
			throw new RuntimeException("init quartz config error,check the config please");
		}
		StdSchedulerFactory factory = null;
		if (!properties.isEmpty()) {
			factory = new StdSchedulerFactory(properties);
		} else {
			factory = new StdSchedulerFactory();
		}
		scheduler = factory.getScheduler();
		logger.info("------------------------scheduler.start();------------------------");
		scheduler.start();
	}

	public void destoryScheduler() {
		try {
			scheduler.shutdown(true);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public Scheduler getScheduler() {
		return this.scheduler;
	}
}
