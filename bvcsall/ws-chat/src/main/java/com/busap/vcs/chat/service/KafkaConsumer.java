package com.busap.vcs.chat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import org.apache.log4j.Logger;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.chat.bean.PubParameter;
import com.busap.vcs.chat.util.MessUtil;
import com.busap.vcs.chat.util.SerializeUtil;

public class KafkaConsumer {
	private static Logger logger = Logger.getLogger(KafkaConsumer.class);
	private String topic;
	private Properties consumerConfig;
	
	private int partitionsNum = 1;
	private ConsumerConnector connector;
	
	public KafkaConsumer(String topic,Properties consumerConfig){
		this.consumerConfig = consumerConfig;
		this.topic = topic;
	}
	/**
	 * 开启kafka消息接收
	 */
	public void init() {
		ConsumerConfig config = new ConsumerConfig(consumerConfig);

		connector = kafka.consumer.Consumer.createJavaConsumerConnector(config);
		Map<String, Integer> topics = new HashMap<String, Integer>();
		topics.put(topic, partitionsNum);
		Map<String, List<KafkaStream<byte[], byte[]>>> streams = connector.createMessageStreams(topics);
		
		List<KafkaStream<byte[], byte[]>> partitions = streams.get(topic);
		
		// start
		for (final KafkaStream<byte[], byte[]> partition : partitions) {
			//threadPool.execute(new MessageRunner(partition));
			ConsumerIterator<byte[], byte[]> it = partition.iterator();
			while (it.hasNext()) {
				MessageAndMetadata<byte[], byte[]> item = it.next();
				try {
					byte[] b = item.message();
					WsMessage msg = (WsMessage) SerializeUtil.ByteToObject(b);
					PubParameter.recieveLog.info("Kafka consumer fetch message."+msg.toString());
					MessUtil.recieveMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("consumer message error,error msg is {}",e);
				}finally{
					connector.commitOffsets();
				}
			}
		}
		logger.info("kafka exec end.");
	}
	/**
	 * 终结kafka消息
	 */
	public void stop(){
		this.connector.shutdown();
	}
}
