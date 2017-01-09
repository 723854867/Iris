package com.busap.vcs.chat.service.kafka.producer;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.WsMessage;



@Service("chatProducer")
public class KafkaProducer {
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
	private Producer<WsMessage, WsMessage> producer;
	
	@Value("#{configProperties['topic']}")
	private String topic;
	
	@Autowired
	private Properties producerConfig;
	
	public KafkaProducer() {
	}
	
	@PostConstruct
	public void init() throws Exception {
		//ProducerConfig config = new ProducerConfig(kafkaConfig);
		ProducerConfig config = new ProducerConfig(producerConfig);
		producer = new Producer<WsMessage, WsMessage>(config);
		logger.info("kafka producer init complete.");
	}

	public void send(WsMessage message) {
		KeyedMessage<WsMessage, WsMessage> km = new KeyedMessage<WsMessage, WsMessage>(topic,message, message);
		
		producer.send(km);
//		ChatUtil.messageSender.getMessageQueue().add(message);
		logger.debug("kafka send message OK."+message.toString());
	}
	
	@PreDestroy
	public void close() {
		producer.close();
		logger.info("kafka producer closed.");
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
