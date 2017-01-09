package com.busap.vcs.restadmin.task;

import org.springframework.stereotype.Component;

@Component
public class QueryVideoConvertResult{
	
//	private static final Logger logger = LoggerFactory.getLogger(QueryVideoConvertResult.class);
//	
//	@Resource(name="videoRepository") 
//	private VideoRepository videoRepository;
//	
//	@Resource(name="ruserRepository") 
//	private RuserRepository ruserRepository;
//	
//	@Resource(name="jedisService")
//	private JedisService jedisService;
//	 
//	
//	@Scheduled(fixedRate=1000*60*5)
//	public void autoPublish(){
//		long now = System.currentTimeMillis() - 1000*60*5;
//		long num = jedisService.zCount(Contant.VIDEO_CHECK, now, 0);
//		if(num > 0){
//			logger.debug("------有超过5分钟还未审核的视频,及时短信通知审核人员------");
//			List<String> phoneNos = videoRepository.findPhones();
//			if(phoneNos.size()>0)
//				Mass(phoneNos);
//		}
//	}
//	
//	private static final String sn = "SDK-BBX-010-18410";
//	private static final String pwd = "ea2da@66";
//	private static Client client;
//	static {
//		try {
//			client = new Client(sn, pwd);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//	
//	/**
//	 * 【群发短信告知审核专员有超过5分钟还未审核的视频】
//	 * @param strPhone
//	 */
//	private void Mass(List<String> strPhones){
//		String content = "有超过5分钟还未审核的视频，请及时审核!【巴士在线】" + new SimpleDateFormat("MM月dd日 HH:mm").format(new Date());
//		try {
//			content = URLEncoder.encode(content, "utf8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		if(strPhones.size() > 0)
//			logger.debug("------给手机号为：" + strPhones.toString() + "发送了5分钟未审核提醒------");
//		//send message for Phone
//		//群发
//		for (int i = 0; i < strPhones.size(); i++) {
//			client.mdSmsSend_u(strPhones.get(i), content, "", "", "");
//		}
//	}
}
