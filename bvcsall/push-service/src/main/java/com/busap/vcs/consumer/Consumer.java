package com.busap.vcs.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.busap.vcs.srv.impl.*;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.busap.vcs.base.Message;
import com.busap.vcs.util.BeanUtils;
import com.busap.vcs.utils.mipush.IosMsgSender;


public class Consumer {
	private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
	//mipush api http://dev.xiaomi.com/doc/?p=533
	@Value("#{configProperties['group_id']}")
	private String groupid; // can be setting by spring
	
	@Value("#{configProperties['topic']}")
	private String topic;

	@Value("#{configProperties['ios_istest']}")
	public String IOS_ISTEST = "";
	
	@Autowired
	private PraiseService praiseService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private BnoticeService bnoticeService;
	@Autowired
	private FocusService focusService;
	@Autowired
	private ForwardService forwardService;
	@Autowired
	private LiveService liveService;
	@Autowired
	private LiveNoticeService liveNoticeService;
	@Autowired
	private BerryService berryService;
	
	
	@Autowired
	private Properties kafkaConfig;
	private int partitionsNum = 1;
	private ConsumerConnector connector;


	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}



	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setPartitionsNum(int partitionsNum) {
		this.partitionsNum = partitionsNum;
	}

	public Consumer() {

	}

	// init consumer,and start connection and listener
	
	public void init() throws Exception {
		IosMsgSender.init(IOS_ISTEST);
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
					Message msg = (Message) BeanUtils.byte2Obj(b);
					switch (msg.getModule()) {
					case PRAISE:
						praiseService.dealMsg(msg);
						break;
					case COMMENT:
						commentService.dealMsg(msg);
						break;
					case BNOTICE:
						bnoticeService.dealMsg(msg);
						break;
					case FOCUS:
						focusService.dealMsg(msg);
						break;
					case LIVE:
						liveService.dealMsg(msg);
						break;
					case FORWARD:
						forwardService.dealMsg(msg);
					case LIVE_NOTICE:
						liveNoticeService.dealMsg(msg);
						break;
					case BERRY:
						berryService.dealMsg(msg);
						break;
					default:
						break;
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
}
