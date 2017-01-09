package com.busap.vcs.consumer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xbean.spring.context.ResourceXmlApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class Start {
	private static String DEFAULT_SPRING_FILE = "classpath:spring.xml";
	private static String DEFAULT_CONSUMER_FILE = "classpath:consumer.properties";
	private static String DEFAULT_CONFIG_FILE = "classpath:config.properties";
	private static final Logger logger = LoggerFactory.getLogger(Start.class);
	//java -jar -Dconfig.path=config.properties -Dconsumer.path=consumer.properties push-consumer.jar
	public static void main(String[] args) {
//		ApplicationContext context = new ClassPathXmlApplicationContext(DEFAULT_SPRING_FILE);
		ResourceLoader loader = new DefaultResourceLoader();
		Resource rs = loader.getResource(DEFAULT_SPRING_FILE);
		try {
			String xml = IOUtils.toString(rs.getInputStream(), "UTF-8");
			// 通用配置文件
			String configPath = System.getProperty("config.path");
//			System.out.println("============================>config.path:"+configPath);
			if (StringUtils.isBlank(configPath)) {
				logger.error("==============config file is not found in system property,use classpath config file");
			} else {
				File configFile = new File(configPath);
				if (!configFile.exists()) {
					throw new IllegalArgumentException("================the specify config file does not exist ...");
				}
				xml = StringUtils.replace(xml, DEFAULT_CONFIG_FILE, "file:" + configPath);
			}

			// kafka消费者配置文件
			String consumerPath = System.getProperty("consumer.path");
//			System.out.println("============================>consumer.path:"+consumerPath);
			if (StringUtils.isBlank(consumerPath)) {
				logger.error("==============the consumer config file is not found in system property,use classpath consumer config file");
			} else {
				File consumerFile = new File(consumerPath);
				if (!consumerFile.exists()) {
					throw new IllegalArgumentException("================the specify consumer config file does not exist ...");
				}
				xml = StringUtils.replace(xml, DEFAULT_CONSUMER_FILE, "file:" + consumerPath);
			}

//			System.out.println("============================>xml:"+xml);
			Resource byteResource = new ByteArrayResource(xml.getBytes());

			final ApplicationContext ctx = new ResourceXmlApplicationContext(byteResource);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
