package com.busap.vcs.service.kafka.producer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.WsMessage;



@Service
public class WsMessageProducer {
	private static final Logger logger = LoggerFactory.getLogger(WsMessageProducer.class);
	private Producer<WsMessage, WsMessage> producer;
	
	private static String location = "producer_ws.properties";


	public WsMessageProducer() {
	}
	
	@PostConstruct
	public void init() throws Exception {
		String dir = System.getProperty("user.dir");
		File fDir = new File(dir);
		String parentDir = fDir.getParent();
		String producerConfig = parentDir+File.separator+"producer_ws.properties";
		Properties properties = new Properties();
		try {
			InputStream is = new FileInputStream(producerConfig);
			properties.load(is);
		} catch (FileNotFoundException e) {
			logger.error("=================websockt send message producer file is not found at {}===============",producerConfig);
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(location));
		}
		
		if (properties.isEmpty()) {
			throw new RuntimeException("init kafka websocket send producer error,check the config please");
		}
		//ProducerConfig config = new ProducerConfig(kafkaConfig);
		ProducerConfig config = new ProducerConfig(properties);
		producer = new Producer<WsMessage, WsMessage>(config);
	}

	public void send(String topicName, WsMessage message) {
		if (topicName == null || message == null) {
			return;
		}
		KeyedMessage<WsMessage, WsMessage> km = new KeyedMessage<WsMessage, WsMessage>(topicName,message, message);
		
		producer.send(km);
	}
	
	@PreDestroy
	public void close() {
		producer.close();
	}

	public static void main(String[] args) {
		/*KafkaProducer producer = null;
		try {
			producer = new KafkaProducer();
			producer.init();
			int i = 0;
			while (true) {
				Message msg = new Message();
				msg.setModule(Module.M2);
				msg.setAction(Action.UPDATE);
				producer.send("test-topic1",msg);
				//i++;
				//System.out.println("cleint send ="+i);
				//Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (producer != null) {
				producer.close();
			}
		}*/

	}
}
