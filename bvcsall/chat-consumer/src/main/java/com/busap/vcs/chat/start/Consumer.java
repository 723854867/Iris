package com.busap.vcs.chat.start;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.chat.srv.JedisService;
import com.busap.vcs.chat.srv.SendService;
import com.busap.vcs.chat.util.RoomUtil;
import com.busap.vcs.util.BeanUtils;


public class Consumer {
	private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
	
	@Value("#{configProperties['topic']}")
	private String topic;
	
	@Value("#{configProperties['partition_num']}")
	private int partitionsNum = 1;
	
	@Value("#{configProperties['client_id']}")
	private String clientId; 
	
	@Autowired
	private SendService sendService;
	
	@Autowired
	private Properties kafkaConfig;
	
	@Resource(name = "jedisService")
	private JedisService jedisService;
	
	private ConsumerConnector connector;

	public void setPartitionsNum(int num){
		this.partitionsNum = num;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Consumer() {

	}

	// init consumer,and start connection and listener
	
	public void init() throws Exception {
		logger.info("kafka consumer start.");
		RoomUtil.init(jedisService,clientId);
//		SendThread sendThreand = new SendThread();
//		new Thread(sendThreand).start();//开启发送线程
		
		ConsumerConfig config = new ConsumerConfig(kafkaConfig);
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
					WsMessage msg = (WsMessage) BeanUtils.byte2Obj(b);
					logger.info("consumer fatch message:{}",msg);
					if(msg != null){
						sendService.sendMessage(msg);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("consumer message error,error msg is {}",e.getMessage());
				}finally{
					connector.commitOffsets();
				}
			}
		}
	}

	public void close() {
		try {
			System.out.println("consumer shutdown.....................");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connector.shutdown();
		}
	}
	
	public SendService getSendService() {
		return sendService;
	}

	public void setSendService(SendService sendService) {
		this.sendService = sendService;
	}

}
