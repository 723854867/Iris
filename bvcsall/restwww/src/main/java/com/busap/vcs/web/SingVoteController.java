package com.busap.vcs.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Tuple;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.SingConstants;
import com.busap.vcs.data.entity.ConsumeRecord;
import com.busap.vcs.data.entity.SingVote;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.ConsumeRecordService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserLiveActivityService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.SingVoteService;
import com.busap.vcs.service.kafka.producer.WsMessageProducer;
import com.busap.vcs.util.DateUtils;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

 
@Controller
@RequestMapping("/singVote")
public class SingVoteController extends CRUDController<SingVote, Long> {

    private Logger logger = LoggerFactory.getLogger(SingVoteController.class); 

    @Resource(name="singVoteService") 
    SingVoteService singVoteService; 
    
    @Resource(name = "ruserService")
	private RuserService ruserService;
    
    @Resource(name = "ruserLiveActivityService")
    private RuserLiveActivityService ruserLiveActivityService;
    
    @Resource(name = "consumeRecordService")
	private ConsumeRecordService consumeRecordService;
    
    @Autowired
    JedisService jedisService;
    
    @Resource(name="wsMessageProducer")
	private WsMessageProducer wsMessageProducer;
    
    
    @Resource(name="singVoteService")
    @Override
    public void setBaseService(BaseService<SingVote, Long> baseService) {
        this.baseService = baseService;
    } 
    
    //带CD时间的投票
    @RequestMapping("/cdVote")
    @ResponseBody
    public RespBody cdVote(Long roomId){ 
    	String uid = request.getHeader("uid");
    	logger.info("cdVote,uid={},roomId={}",uid,roomId);
    	
    	Map<String,String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_+roomId);
		if(roomInfo == null || roomInfo.isEmpty() || StringUtils.isBlank(roomInfo.get("creatorId"))){
			return respBodyWriter.toSuccess(); 
		}
		
		Long destId = Long.parseLong(roomInfo.get("creatorId")); //获得房间的主播id
    	
    	int count = jedisService.incr(SingConstants.CD_VOTE_FLAG_ + uid); //使用redis自身方法自增，避免脏读
    	if (count == 1) { //cd时间内没有投过票，进行投票处理
    		jedisService.expire(SingConstants.CD_VOTE_FLAG_ + uid, Integer.parseInt(jedisService.get(SingConstants.CD_VOTE_INTERVAL)));
    		SingVote sv = new SingVote();
    		sv.setCreatorId(Long.parseLong(uid));
    		sv.setDestId(destId);
    		sv.setType(1);
    		sv.setValue(1l);
    		Integer radio = Integer.parseInt(jedisService.get(SingConstants.CD_VOTE_RATIO)); //获得cd投票系数
    		if (jedisService.isSetMemberInShard(SingConstants.SING_MEMBER, String.valueOf(destId))) {  //判断是否是新歌声学员
    			sv.setUserType(1);
    		} else {
    			sv.setUserType(2);
    		}
    		sv.setPopularity(1l*radio);
    		singVoteService.save(sv);
    		
    		String senderName = "";
    		try {
    			senderName = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "name");
    			//房间内提示投票信息
        		WsMessage wsMessage = new WsMessage();
    			wsMessage.setChildCode("0006");
    			wsMessage.setCode("000");
    			wsMessage.setRoomId(roomId+"");
    			wsMessage.setContent(senderName+" 为心爱的主播投了一票");
    			
    			wsMessageProducer.send("chat_topic_", wsMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    	}
    	
    	return respBodyWriter.toSuccess(); 
    }  
    
    //付费投票
    @Transactional
    @RequestMapping("/expenseVote")
    @ResponseBody
    public RespBody expenseVote(Long destId){ 
    	String uid = request.getHeader("uid");
    	logger.info("expenseVote,uid={},destId={}",uid,destId);
    	
		if (!jedisService.isSetMemberInShard(SingConstants.SING_MEMBER, String.valueOf(destId))) {  //判断是否是新歌声学员
			return respBodyWriter.toSuccess(); 
		}
    	String ymdStr = DateUtils.getNowYMDAsStr();
    	int count = jedisService.incr(SingConstants.EXPENSE_VOTE_FREE_TIMES_ +ymdStr+"_"+ uid); 
    	if (count == 1) {  //每天的第一次免费
    		SingVote sv = new SingVote();
    		sv.setCreatorId(Long.parseLong(uid));
    		sv.setDestId(destId);
    		sv.setType(3);
    		sv.setValue(1l);
    		sv.setUserType(1);
    		Integer radio = Integer.parseInt(jedisService.get(SingConstants.EXPENSE_VOTE_RATIO)); //获得付费投票系数
    		sv.setPopularity(1l*radio);
    		singVoteService.save(sv);
    		
    		jedisService.expire(SingConstants.EXPENSE_VOTE_FREE_TIMES_ +ymdStr+"_"+ uid, 60*60*24);
    	} else {
    		Integer diamondCount = Integer.parseInt(jedisService.get(SingConstants.EXPENSE_VOTE_DIAMOND_COUNT));
    		
    		//扣减用户金币
			int result = ruserService.reduceDiamond(Long.parseLong(uid), diamondCount);
    		
			if (result > 0) {
				//增加消费记录
				ConsumeRecord record = new ConsumeRecord();
				record.setCreateDate(new Date());
				record.setCreatorId(Long.parseLong(uid));
				record.setDiamondCount(diamondCount);
				record.setGiftName("付费投票");
				record.setRoomId(0l);
				consumeRecordService.save(record);
				
				if (record.getId() != null && record.getId().intValue()>0) {
					//增加投票记录
					SingVote sv = new SingVote();
					sv.setCreatorId(Long.parseLong(uid));
					sv.setDestId(destId);
					sv.setType(3);
					sv.setValue(1l);
					sv.setUserType(1);
					Integer radio = Integer.parseInt(jedisService.get(SingConstants.EXPENSE_VOTE_RATIO)); //获得付费投票系数
					sv.setPopularity(1l*radio);
					singVoteService.save(sv);
				}
			} else {
				return respBodyWriter.toError("余额不足", -1);
			}
    	}
    	
    	return respBodyWriter.toSuccess(); 
    }  
    
    /**
     * 获得新歌声排行榜
     * @param type  1：学员榜 2：网综榜 3：主播榜 4：贡献榜 5:贡献榜总榜
     * @return
     */
    @RequestMapping("/getSingRank")
    @ResponseBody
    public RespBody getSingRank(Integer type,Integer page,Integer size){ 
    	String uid = request.getHeader("uid");
    	logger.info("getSingRank,type={},uid={}",type,uid);
    	if (StringUtils.isBlank(uid)) {
    		uid = "-1";
    	}
    	String rankKey = "";
    	Long dateline = 0l;
    	String url = "";
    	if (type == 1) {
    		rankKey = SingConstants.MEMBER_RANK;
    		dateline = Long.parseLong(jedisService.get(SingConstants.MEMBER_DATELINE));
    	} else if(type == 2) {
    		rankKey = SingConstants.NORMAL_RANK;
    		dateline = Long.parseLong(jedisService.get(SingConstants.NORMAL_DATELINE));
    	} else if(type == 3) {
    		rankKey = SingConstants.ANCHOR_RANK;
    		dateline = Long.parseLong(jedisService.get(SingConstants.ANCHOR_DATELINE));
    		url = jedisService.get(SingConstants.ANCHOR_RANK_URL);
    	} else if(type == 4) {
    		rankKey = SingConstants.CONTRIBUTE_RANK;
    		dateline = Long.parseLong(jedisService.get(SingConstants.CONTRIBUTE_DATELINE));
    		url = jedisService.get(SingConstants.CONTRIBUTE_RANK_URL);
    	} else if(type == 5) {
    		rankKey = SingConstants.CONTRIBUTE_RANK_TOTAL;
    		dateline = Long.parseLong(jedisService.get(SingConstants.CONTRIBUTE_DATELINE_TOTAL));
    	} 
    	
    	DecimalFormat df = new DecimalFormat("########.0");
    	Set<Tuple> set = jedisService.zrevrangeWithScores(rankKey, (page-1)*size.longValue(), page*size.longValue()-1);
    	List<Map<String,Object>> rankList = new ArrayList<Map<String,Object>>();
		for (Tuple tuple:set) {
			Map<String,Object> rankInfo = new HashMap<String,Object>();
			String id = tuple.getElement();
			String popularity = tuple.getScore()>0?df.format(tuple.getScore()):"0";
			rankInfo.put("id", id);
			rankInfo.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+id, "name"));
			rankInfo.put("vipStat", jedisService.getValueFromMap(BicycleConstants.USER_INFO+id, "vipStat"));
			rankInfo.put("sex", jedisService.getValueFromMap(BicycleConstants.USER_INFO+id, "sex"));
			rankInfo.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+id, "pic"));
			rankInfo.put("corps", jedisService.getValueFromMap(BicycleConstants.USER_INFO+id, "corps"));
			rankInfo.put("popularity", popularity.replace(".0", ""));
			
			rankList.add(rankInfo);
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("rankList", rankList);
		
		//计算自己在榜单中的位置
		Long selfPosition = jedisService.zrevRank(rankKey, uid);
		if (selfPosition == null) { //榜外显示位置为0
			selfPosition = 0l;
		} else {
			selfPosition++;
		}
		
		//计算自己的人气值()
		Double selfPopularity = 0d;
		selfPopularity = singVoteService.getUserPopularity(Long.parseLong(uid),type);
		
		result.put("costDiamond", Integer.parseInt(jedisService.get(SingConstants.EXPENSE_VOTE_DIAMOND_COUNT))); //投票消耗的金币数
		result.put("dateline", dateline); //距离结束剩余时间，毫秒
		result.put("url", url); //跳转url
		result.put("selfPosition", selfPosition); //自己在榜单中的位置
		result.put("selfPopularity", selfPopularity>0?df.format(selfPopularity).replace(".0", ""):"0"); //自己人气值
		result.put("isJoin", ruserLiveActivityService.isJoin(Long.parseLong(uid), Long.parseLong(jedisService.get(SingConstants.SING_ACTIVITY_ID))));//是否参加了好声音活动
		result.put("rankList", rankList);
		
    	return respBodyWriter.toSuccess(result); 
    }  
    
    /**
     * 获得房间内新歌声排行榜
     * @param anchorId  主播id
     * @return
     */
    @RequestMapping("/getRoomSingRank")
    @ResponseBody
    public RespBody getRoomSingRank(Long anchorId,Integer page,Integer size){ 
    	String uid = request.getHeader("uid");
    	logger.info("getSingRank,anchorId={},uid={}",anchorId,uid);
    	
    	if (StringUtils.isBlank(uid)) {
    		uid = "-1";
    	}
    	
    	Integer type = 0;
    	
    	String rankKey = "";
    	Long dateline = 0l;
    	String url = "";
    	
    	Map<String,Object> result = new HashMap<String,Object>();
    	if (jedisService.isSetMemberInShard(SingConstants.SING_MEMBER, String.valueOf(anchorId))) {   //判断是否是新歌声学员，如果是就是学员榜，不是就是主播榜
			result.put("rankType", 1);
			rankKey = SingConstants.MEMBER_RANK;
    		dateline = Long.parseLong(jedisService.get(SingConstants.MEMBER_DATELINE));
    		type = 1;
		} else {
			result.put("rankType", 3);
			rankKey = SingConstants.ANCHOR_RANK;
    		dateline = Long.parseLong(jedisService.get(SingConstants.ANCHOR_DATELINE));
    		url = jedisService.get(SingConstants.ANCHOR_RANK_URL);
    		type = 3;
		}
		
    	DecimalFormat df = new DecimalFormat("########.0");
		//获得测试排行榜数据
    	Set<Tuple> set = jedisService.zrevrangeWithScores(rankKey, (page-1)*size.longValue(), page*size.longValue()-1);
    	List<Map<String,Object>> rankList = new ArrayList<Map<String,Object>>();
		for (Tuple tuple:set) {
			Map<String,Object> rankInfo = new HashMap<String,Object>();
			String id = tuple.getElement();
			String popularity = tuple.getScore()>0?df.format(tuple.getScore()):"0";
			rankInfo.put("id", id);
			rankInfo.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+id, "name"));
			rankInfo.put("vipStat", jedisService.getValueFromMap(BicycleConstants.USER_INFO+id, "vipStat"));
			rankInfo.put("sex", jedisService.getValueFromMap(BicycleConstants.USER_INFO+id, "sex"));
			rankInfo.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+id, "pic"));
			rankInfo.put("corps", jedisService.getValueFromMap(BicycleConstants.USER_INFO+id, "corps"));
			rankInfo.put("popularity", popularity.replace(".0", ""));
			
			rankList.add(rankInfo);
		}
		result.put("rankList", rankList);
		
		//计算自己在榜单中的位置
		Long selfPosition = jedisService.zrevRank(rankKey, uid);
		if (selfPosition == null) { //榜外显示位置为0
			selfPosition = 0l;
		} else {
			selfPosition++;
		}
		
		//计算自己的人气值()
		Double selfPopularity = 0d;
		selfPopularity = singVoteService.getUserPopularity(Long.parseLong(uid),type);
		
		result.put("costDiamond", Integer.parseInt(jedisService.get(SingConstants.EXPENSE_VOTE_DIAMOND_COUNT))); //投票消耗的金币数
		result.put("dateline", dateline); //距离结束剩余时间，毫秒
		result.put("url", url); //跳转url
		result.put("selfPosition", selfPosition); //自己在榜单中的位置
		result.put("selfPopularity", selfPopularity>0?df.format(selfPopularity).replace(".0", ""):"0"); //自己人气值
		result.put("isJoin", ruserLiveActivityService.isJoin(Long.parseLong(uid), Long.parseLong(jedisService.get(SingConstants.SING_ACTIVITY_ID))));//是否参加了好声音活动
		result.put("rankList", rankList);
    	return respBodyWriter.toSuccess(result); 
    }  
    
    @RequestMapping("/createRank")
    @ResponseBody
    public RespBody createRank(Integer type){ 
    	singVoteService.createRank(type);
    	return respBodyWriter.toSuccess(); 
    }  
    
    
    //房间内互动榜
  	@RequestMapping("/interactionRank")
  	@ResponseBody
  	public RespBody interactionRank() {
  		List<Map<String,Object>> ranklist = new ArrayList<Map<String,Object>>();
  		logger.info("interactionRank");
  		Set<Tuple> set = jedisService.zrevrangeWithScores(SingConstants.WANGZONG_RANK, 0l, -1l);
  		for (Tuple tuple:set) {
  			String receiverId = tuple.getElement();//获得接受者id
  			String giftNumber = String.valueOf(tuple.getScore());//获得收到专属礼物金豆数
  			if (giftNumber != null && giftNumber.contains(".")) {
  				giftNumber = giftNumber.substring(0,giftNumber.indexOf("."));
  			}
  			Map<String,Object> map = new HashMap<String,Object>();
  			map.put("uid", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "id"));
  			map.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "name"));
  			map.put("signature", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "signature"));
  			map.put("vipStat", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "vipStat"));
  			map.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "pic"));
  			map.put("sex", jedisService.getValueFromMap(BicycleConstants.USER_INFO+receiverId, "sex"));
  			map.put("bean", giftNumber);
  			ranklist.add(map);
  		}
  		return respBodyWriter.toSuccess(ranklist);
  	}
  	
    public static void main(String[] args) {
		Double a = 1d;
		System.out.println(a>0);
		DecimalFormat df = new DecimalFormat("########.0");
		System.out.println(df.format(a));
	}
    
    
}
