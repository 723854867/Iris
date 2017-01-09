package com.busap.vcs.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import com.busap.vcs.base.Message;
import com.busap.vcs.bean.VideoTaskItem;
import com.busap.vcs.srv.JedisService;
import com.busap.vcs.srv.impl.MessageService;
import com.busap.vcs.srv.impl.UserFocusService;
import com.busap.vcs.srv.impl.VideoService;
import com.busap.vcs.util.BeanUtils;


public class Consumer {
	private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
	@Value("#{configProperties['group_id']}")
	private String groupid; // can be setting by spring
	public static List<VideoTaskItem> focuslist = new ArrayList<VideoTaskItem>();
	public static List<VideoTaskItem> praiselist = new ArrayList<VideoTaskItem>();
	@Value("#{configProperties['topic']}")
	private String topic;
	@Value("#{configProperties['userfocus_intervaltime']}")
	private String userfocusIntervaltime;
	@Value("#{configProperties['userprise_intervaltime']}")
	private String userpriseIntervaltime;
	@Autowired
	VideoService videoService;
	@Autowired
	MessageService messageService;
	@Autowired
	UserFocusTask userFocusTask;
	@Autowired
	MajiaPraiseTask majiaPraiseTask;
	@Autowired
	private Properties kafkaConfig;
	@Autowired
	JedisService jedisService;
	@Autowired
	UserFocusService userFocusService;
	private int partitionsNum = 1;
	private ConsumerConnector connector;
	private Thread thread;
	private Timer timer;

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
		timer = new Timer();
		thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					doConsume();	
				}catch(Exception ex){
					logger.error("Consumer error,detail:",ex);
				}
			}
			
		});
	}

	// init consumer,and start connection and listener
	public void doConsume(){
		ConsumerConfig config = new ConsumerConfig(kafkaConfig);
		connector = kafka.consumer.Consumer.createJavaConsumerConnector(config);
		Map<String, Integer> topics = new HashMap<String, Integer>();
		topics.put(topic, partitionsNum);
		Map<String, List<KafkaStream<byte[], byte[]>>> streams = connector.createMessageStreams(topics);
		List<KafkaStream<byte[], byte[]>> partitions = streams.get(topic);
		try {
//		// start
			for (final KafkaStream<byte[], byte[]> partition : partitions) {
				//threadPool.execute(new MessageRunner(partition));
				ConsumerIterator<byte[], byte[]> it = partition.iterator();
				while (it.hasNext()) {
					MessageAndMetadata<byte[], byte[]> item = it.next();
					try {
						byte[] b = item.message();
						Message msg = (Message) BeanUtils.byte2Obj(b); 
						switch (msg.getModule()) {
						case VIDEO:
							logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++>"+msg.getDataMap());
							messageService.dealMsg(msg);
							break;
						default:
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("consumer message error,error msg is {},{}",e.getMessage(),e.toString());
					} finally {
						connector.commitOffsets();
					}
				}
			}
		} catch (Exception e) {
			logger.error("error msg is {},{}",e.getMessage(),e.toString());
		}
	}
	
	public void init() throws Exception {
		thread.setDaemon(true);
		thread.start();
		timer.schedule(userFocusTask,  0, Long.valueOf(userfocusIntervaltime));
		timer.schedule(majiaPraiseTask,  0, Long.valueOf(userpriseIntervaltime));
		userFocusService.initMajia();
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
	public String getUserfocusIntervaltime() {
		return userfocusIntervaltime;
	}



	public void setUserfocusIntervaltime(String userfocusIntervaltime) {
		this.userfocusIntervaltime = userfocusIntervaltime;
	}



	public String getUserpriseIntervaltime() {
		return userpriseIntervaltime;
	}



	public void setUserpriseIntervaltime(String userpriseIntervaltime) {
		this.userpriseIntervaltime = userpriseIntervaltime;
	}

}
