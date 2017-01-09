package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.busap.vcs.service.*;
import com.busap.vcs.webcomn.controller.CRUDController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.base.Action;
import com.busap.vcs.base.Message;
import com.busap.vcs.base.Module;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.vo.AttentionVO;
import com.busap.vcs.data.vo.FansVO;
import com.busap.vcs.service.impl.NotificationJPushUtil;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
//import com.busap.vcs.data.mapper.AttentionDAO;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller
@RequestMapping("/attention")
public class AttentionController {

	private Logger logger = LoggerFactory.getLogger(AttentionController.class);

	@Autowired
	NotificationJPushUtil util;

	@Autowired
	protected HttpServletRequest request;

	@Resource(name = "ruserService")
	private RuserService ruserService;

	@Resource(name = "videoService")
	private VideoService videoService;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name="attentionService")
	private AttentionService attentionService;

	@Resource(name = "integralService")
	private IntegralService integralService;

	@Autowired
    KafkaProducer kafkaProducer;

	// 关注
	@RequestMapping("/payAttention")
	@ResponseBody
	@Transactional
	public RespBody payAttention(Long attentionId, String dataFrom,
			int isAttention) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={},attentionId={},dataFrom={}", uid, attentionId,dataFrom);
		
		if (Long.parseLong(uid) == attentionId)  //判断是否关注自己 
			return this.respBodyWriter.toError(ResponseCode.CODE_341.toString(), ResponseCode.CODE_341.toCode());

		if (isAttention >= 1) {
			// 取消关注
			attentionService.deleteAttention(Long.parseLong(uid), attentionId, dataFrom); 
			jedisService.deleteSetItemFromShard(BicycleConstants.ATTENTION_ID_OF+uid, String.valueOf(attentionId));
		} else {

			// 判断用户是否被加入黑名单，如加入，不能关注
			if (jedisService.isSetMemberInShard(BicycleConstants.BLACK_LIST_USER_ID + attentionId, uid)) {
				return this.respBodyWriter.toError(ResponseCode.CODE_613.toString(), ResponseCode.CODE_613.toCode());
			}

			// 添加关注
			if (attentionService.isAttention(Long.parseLong(uid), attentionId) == 1)
				return this.respBodyWriter.toError(ResponseCode.CODE_311.toString(), ResponseCode.CODE_311.toCode());
			
			int result = attentionService.payAttention(Long.parseLong(uid), attentionId, dataFrom);
			if (result == 1) {
				//添加关注成功，加入消息队列，并放到redis里
				Message msg = new Message();
				msg.setModule(Module.FOCUS);
				msg.setAction(Action.INSERT);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uid", uid);
				map.put("pid", String.valueOf(attentionId));
				msg.setDataMap(map);
		    	kafkaProducer.send("push-msg-topic", msg);
		    	
		    	jedisService.setValueToSetInShard(BicycleConstants.ATTENTION_ID_OF+uid, String.valueOf(attentionId));

				try {
					// 判断粉丝数是否达标，加积分
					integralService.getIntegralFromFanNumber(attentionId);
				} catch (Exception e) {
					logger.info("getIntegralFromFanNumber error: exception=" + e.getMessage());
					e.printStackTrace();
				}
				try {
					// 判断关注的人是否包含VIP
					integralService.checkAttentedUserIsVIP(Long.parseLong(uid), attentionId);
				} catch (Exception e) {
					logger.info("checkAttentedUserIsVIP error: exception=" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return respBodyWriter.toSuccess(attentionService.isAttention(Long.parseLong(uid), attentionId) == 1 ?
					1 + attentionService.isAttention(attentionId, Long.parseLong(uid)) : 0);

	}
	
	// 批量添加关注
	@RequestMapping("/multiPayAttention")
	@ResponseBody
	@Transactional
	public RespBody multiPayAttention(String attentionIds, String dataFrom) {
		final String uid =  this.request.getHeader("uid");
		logger.info("uid={},dataFrom={},attentionIds={}", uid, dataFrom, attentionIds);
		if (attentionIds == null || "".equals(attentionIds)) {
			return respBodyWriter.toError(ResponseCode.CODE_312.toString(), ResponseCode.CODE_312.toCode());
		}
		final String data = dataFrom;
		final String idArray = attentionIds;
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				logger.info("new thread to multiPayAttention! uid=" + uid + " & idArray=" + idArray);
				String[] ids = idArray.trim().split(",");
				List<Long> list = attentionService.selectAllAttentionId(Long.parseLong(uid));
				if (ids.length >0 ){
					for (String id : ids) {
						Long attentionId = Long.parseLong(id);
						//如果已关注某人，或者关注人是自己，continue
						if (list.contains(attentionId) || uid.equals(id))
							continue;
						int result = attentionService.payAttention(Long.parseLong(uid), attentionId, data);
						if (result == 1) {
							//添加关注成功，加入消息队列，并放到redis里
							jedisService.setValueToSetInShard(BicycleConstants.ATTENTION_ID_OF + uid, String.valueOf(attentionId));
							try {
								// 判断粉丝数是否达标，加积分
								integralService.getIntegralFromFanNumber(attentionId);
							} catch (Exception e) {
								logger.info("getIntegralFromFanNumber error: exception=" + e.getMessage());
								e.printStackTrace();
							}
							try {
								// 判断关注的人是否包含VIP
								integralService.checkAttentedUserIsVIP(Long.parseLong(uid), attentionId);
							} catch (Exception e) {
								logger.info("checkAttentedUserIsVIP error: exception=" + e.getMessage());
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		t.start();

		return respBodyWriter.toSuccess();
	}

	// 获得关注列表
	@RequestMapping("/getAttentionList")
	@ResponseBody
	public RespBody getAttentionList(Long timestamp,Integer count) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}", uid);
		List<AttentionVO> list = attentionService.getAttentionList(Long.parseLong(uid), (timestamp==null||timestamp.longValue()==0)?null:new Date(timestamp), count);
		List<AttentionVO> result = getAttentionEachOther(list, uid);
		return respBodyWriter.toSuccess(result);
	}
	
	// 获得关注列表(新接口，用id翻页)
	@RequestMapping("/getAttentionListNew")
	@ResponseBody
	public RespBody getAttentionListNew(Long lastId,Integer count) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}", uid);
		List<AttentionVO> list = attentionService.getAttentionListNew(Long.parseLong(uid), lastId == null?0l:lastId, count);
		return respBodyWriter.toSuccess(list);
	}

	// 获得粉丝列表
	@RequestMapping("/getFansList")
	@ResponseBody
	public RespBody getFansList(Long timestamp,Integer count) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}", uid);
		List<FansVO> list = attentionService.getFansList(Long.parseLong(uid), (timestamp==null||timestamp.longValue()==0)?null:new Date(timestamp), count);
		List<FansVO> result = getOtherFansAttentionEachOther(list, uid);
		return respBodyWriter.toSuccess(result);
	}

	// 获得关注人视频列表(动态)
	@RequestMapping("/getAttentionVideoList")
	@ResponseBody
	public RespBody getAttentionVideoList(Long timestamp, Integer count) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}", uid);
		List<Long> ids = attentionService.selectAllAttentionId(Long.parseLong(uid));
		ids.add(Long.parseLong(uid));
		if (ids == null || ids.size() == 0){
			return respBodyWriter.toSuccess(new ArrayList());
		}
		return respBodyWriter.toSuccess(videoService.findAttentionNewVideos(Long.parseLong(uid),
				ids, (timestamp==null||timestamp.longValue()==0)?null:new Date(timestamp), count));
	}
	
	// 获得关注人及自己视频，直播预告，图片，回放列表(动态)
	@RequestMapping("/getAttentionInfoList")
	@ResponseBody
	public RespBody getAttentionInfoList(Long timestamp, Integer count, Integer type) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}", uid);
		List<Long> ids = attentionService.selectAllAttentionId(Long.parseLong(uid));
		ids.add(Long.parseLong(uid));
		if (ids == null || ids.size() == 0){
			return respBodyWriter.toSuccess(new ArrayList());
		}
		return respBodyWriter.toSuccess(videoService.getAttentionInfoList(Long.parseLong(uid),ids, (timestamp==null||timestamp.longValue()==0)?null:new Date(timestamp), count,type));
	}
	
	// 获得自己视频，直播预告，图片，回放列表(动态)
	@RequestMapping("/getSelfAttentionInfoList")
	@ResponseBody
	public RespBody getSelfAttentionInfoList(Long timestamp, Integer count, Integer type) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}", uid);
		List<Long> ids = new ArrayList<Long>();
		ids.add(Long.parseLong(uid));
		if (ids == null || ids.size() == 0){
			return respBodyWriter.toSuccess(new ArrayList());
		}
		return respBodyWriter.toSuccess(videoService.getAttentionInfoList(Long.parseLong(uid),
				ids, (timestamp==null||timestamp.longValue()==0)?null:new Date(timestamp), count,type));
	}
	
	// 获取固定用户视频，直播预告，图片，回放列表(动态)
	@RequestMapping("/getVideoInfoListByUserId")
	@ResponseBody
	public RespBody getVideoInfoListByUserId(Long timestamp, Integer count, Integer type,Long userId) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(userId);
		if (ids == null || ids.size() == 0){
			return respBodyWriter.toSuccess(new ArrayList());
		}
		return respBodyWriter.toSuccess(videoService.getAttentionInfoList(userId,
				ids, (timestamp==null||timestamp.longValue()==0)?null:new Date(timestamp), count,type));
	}
	
	// 获得关注人及自己直播列表(动态)
	@RequestMapping("/getAttentionLiveInfoList")
	@ResponseBody
	public RespBody getAttentionLiveInfoList(Integer count, Integer start) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}", uid);
		List<Long> ids = attentionService.selectAllAttentionId(Long.parseLong(uid));
//		ids.add(Long.parseLong(uid));
		if (ids == null || ids.size() == 0){
			return respBodyWriter.toSuccess(new ArrayList());
		}
		if(count==null) {
			count=10;
		}
		if(start==null) {
			start=0;
		}else {
			start=(start-1)*count;
		}
		return respBodyWriter.toSuccess(videoService.getAttentionLiveInfoList(ids,  count,start));
	}
	
	// 根据用户id获取直播
	@RequestMapping("/getLiveInfoByUserId")
	@ResponseBody
	public RespBody getLiveInfoByUserId(Long userId) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}", uid);
		List<Long> ids = new ArrayList();;
		ids.add(userId);
		if (ids == null || ids.size() == 0){
			return respBodyWriter.toSuccess(new ArrayList());
		}
		return respBodyWriter.toSuccess(videoService.getAttentionLiveInfoList(ids,  1,0));
	}
	
	
	
	// 获得他人的关注人视频列表(动态)
	@RequestMapping("/getOtherAttentionVideoList")
	@ResponseBody
	public RespBody getOtherAttentionVideoList(Long otherUid,Long timestamp, Integer count) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={},otherUid={}", uid,otherUid);
		List<Long> ids = attentionService.selectAllAttentionId(otherUid);
		//ids.add(Long.parseLong(uid));
		if (ids == null || ids.size() == 0){
			return respBodyWriter.toSuccess(new ArrayList());
		}
		return respBodyWriter.toSuccess(videoService.findOtherAttentionNewVideos((uid==null||"".equals(uid))?null:Long.parseLong(uid),
				ids, (timestamp==null||timestamp.longValue()==0)?null:new Date(timestamp), count));
	}
	
	// 获得他人关注列表
	@RequestMapping("/getOtherAttentionList")
	@ResponseBody
	public RespBody getOtherAttentionList(Long otherUid,Long timestamp,Integer count) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={},otherUid={}",uid, otherUid);
		List<AttentionVO> list = attentionService.getOtherAttentionList(Long.parseLong(uid), otherUid, (timestamp == null || timestamp.longValue() == 0) ? null : new Date(timestamp), count);
		return respBodyWriter.toSuccess(list);
	}

	// 获得他人粉丝列表
	@RequestMapping("/getOtherFansList")
	@ResponseBody
	public RespBody getOtherFansList(Long otherUid,Long timestamp,Integer count) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={},otherUid={}",uid, otherUid);
		List<FansVO> list = attentionService.getOtherFansList(Long.parseLong(uid),otherUid, (timestamp==null||timestamp.longValue()==0)?null:new Date(timestamp), count);
		List<FansVO> result = getOtherFansAttentionEachOther(list, uid);
		return respBodyWriter.toSuccess(result);
	}
	
	//判断是否关注某人
	@RequestMapping("/isAttention")
	@ResponseBody
	public RespBody isAttention(Long otherUid) {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={},otherUid={}",uid,otherUid);
		int attention = 0;
		if (attentionService.isAttention(Long.parseLong(uid), otherUid) == 1) {
			attention = 1 + attentionService.isAttention(otherUid, Long.parseLong(uid));
		}
		return respBodyWriter.toSuccess(attention);
	}

	//获得推荐马甲用户
	@RequestMapping("/getRecommendUsers")
	@ResponseBody
	public RespBody getRecommendUsers() {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}",uid);
		Set<String> uidSet = new HashSet<String>();
		while (uidSet.size()<6 && uidSet.size()<jedisService.getSetSizeFromShard(BicycleConstants.MAJIA_UID)) {
			uidSet.add(jedisService.getSrandmember(BicycleConstants.MAJIA_UID));
		}
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Iterator<String> it = uidSet.iterator();
		while (it.hasNext()) {
		  String str = it.next();
		  list.add(jedisService.getMapByKey(BicycleConstants.MAJIA+str));
		}
		
		return respBodyWriter.toSuccess(list);
	}
	
	//获得推荐用户（蓝v，黄v，新注册用户）
	@RequestMapping("/getDynamicRecommend")
	@ResponseBody
	public RespBody getDynamicRecommend() {
		String uid =  this.request.getHeader("uid");
		logger.info("uid={}",uid);
		
		List<Ruser> list = attentionService.getDynamicRecommend(Long.parseLong(uid),20);
		
		return respBodyWriter.toSuccess(list);
	}
	
	@RequestMapping("/getSinger")
	@ResponseBody
	public RespBody getSinger() {
		String uid =  this.request.getHeader("uid");
		String[] team1= {"3294413|会表演会耍帅的新加坡小帅哥","3294531|钟爱中式乐器，精通各种乐器","3315887|像孩子一样，率真十足","3348093|铁肺”少女，拥有彪悍的高音","3348145|有着硬汉的外表却唱着沧桑的情歌","3382921|外表看似粗狂，歌声却独有一番风味","3382911|“绿衣胖胖”带来的视听盛宴","3444167|一个姓张一个姓杨 做出来的歌就那么张扬","3444305|满身的花儿随着《那些花儿》绽放","3444365|做音乐一定要天马行空","3444381|一把吉他一首民谣 找回心里那片净土"};
		String[] team2= {"3294439|生活中的氧气女孩","3294577|爱抒情民谣的安静美男子","3315841|热爱舞台的活力少女","3315901|内心深处深爱摇滚的力量与执着","3315971|典型暖男，带着干净温暖的声音，融化你的心","3348111|向前跑，迎着冷眼和嘲笑","3348119|混血大男孩，独有的音乐风格","3444213|嘻哈少女带你一起追寻“爱是什么”","3486235|坚持自己唱歌的初衷","3486283|狂野简单摇滚热血的少年"};
		String[] team3= {"3294551|舞台会成为国际歌星梦想的起点","3294589|直播界名副其实的小鲜肉","3315859|内向爱笑的大男孩","3315869|用自己的音乐，演绎自己的人生","3348097|热爱音乐的态度始终如一","3382951|用歌声寻找最初的心境","3382939|用歌声让你重新认识RAP","3444199|“金华龙猫”周旸用声音演绎什么是天然","3444349|把大家带进温暖的音乐世界","3497762|长相帅气的国防生"};
		String[] team4= {"3315853|耿直，幽默，还带着嘻哈范儿","3348139|萌妹子姚希，俏皮短发＋可爱眼镜","3348157|人来疯的小熙，引爆你内心的小宇宙","3382863|用\"歌声\"和\"气场\"杀出一条自己的未来之路","3382881|带你一起聆听少女的心声。","3382897|拥有超强的爆发力，你期待吗？","3444327|决定把自己归零 她只有57岁而已","3444353|像是从迪士尼漫画里走出来一样","3497713|拥有独特的声音和音乐气质","3497612|帅气的外表搭配独特的嗓音"};
		String[] team5= {"3382979|来自南京的雅痞男生，散发忧郁气质"};
//		String[] team1= {"119143|会表演会耍帅的新加坡小帅哥","119143|钟爱中式乐器，精通各种乐器","119143|像孩子一样，率真十足","3348093|铁肺”少女，拥有彪悍的高音","119143|有着硬汉的外表却唱着沧桑的情歌","119143|外表看似粗狂，歌声却独有一番风味","119143|“绿衣胖胖”带来的视听盛宴","119143|一个姓张一个姓杨 做出来的歌就那么张扬","119143|满身的花儿随着《那些花儿》绽放","119143|做音乐一定要天马行空","119143|一把吉他一首民谣 找回心里那片净土"};
//		String[] team2= {"119143|生活中的氧气女孩","119143|爱抒情民谣的安静美男子","119143|热爱舞台的活力少女","3315901|内心深处深爱摇滚的力量与执着","119143|典型暖男，带着干净温暖的声音，融化你的心","119143|向前跑，迎着冷眼和嘲笑","119143|混血大男孩，独有的音乐风格","119143|嘻哈少女带你一起追寻“爱是什么”","119143|坚持自己唱歌的初衷","119143|狂野简单摇滚热血的少年"};
//		String[] team3= {"119143|舞台会成为国际歌星梦想的起点","119143|直播界名副其实的小鲜肉","119143|内向爱笑的大男孩","3315869|用自己的音乐，演绎自己的人生","119143|热爱音乐的态度始终如一","119143|用歌声寻找最初的心境","119143|用歌声让你重新认识RAP","119143|“金华龙猫”周旸用声音演绎什么是天然","119143|把大家带进温暖的音乐世界","119143|长相帅气的国防生"};
//		String[] team4= {"119143|耿直，幽默，还带着嘻哈范儿","119143|萌妹子姚希，俏皮短发＋可爱眼镜","119143|人来疯的小熙，引爆你内心的小宇宙","119143|用\"歌声\"和\"气场\"杀出一条自己的未来之路","119143|带你一起聆听少女的心声。","119143|拥有超强的爆发力，你期待吗？","119143|决定把自己归零 她只有57岁而已","119143|像是从迪士尼漫画里走出来一样","119143|拥有独特的声音和音乐气质","119143|帅气的外表搭配独特的嗓音"};
//		String[] team5= {"119143|来自南京的雅痞男生，散发忧郁气质"};
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (int i=0;i<team1.length;i++) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", team1[i].split("\\|")[0]);
			map.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team1[i].split("\\|")[0], "name"));
			map.put("vipStat", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team1[i].split("\\|")[0], "vipStat"));
			map.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team1[i].split("\\|")[0], "pic"));
			map.put("description", team1[i].split("\\|")[1]);
			map.put("corps", 1);//战队标识，1：周杰伦 2：汪峰 3：那英 4：庾澄庆
			list.add(map);
		}
		for (int i=0;i<team2.length;i++) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", team2[i].split("\\|")[0]);
			map.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team2[i].split("\\|")[0], "name"));
			map.put("vipStat", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team2[i].split("\\|")[0], "vipStat"));
			map.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team2[i].split("\\|")[0], "pic"));
			map.put("description", team2[i].split("\\|")[1]);
			map.put("corps", 2);//战队标识，1：周杰伦 2：汪峰 3：那英 4：庾澄庆
			list.add(map);
		}
		for (int i=0;i<team3.length;i++) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", team3[i].split("\\|")[0]);
			map.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team3[i].split("\\|")[0], "name"));
			map.put("vipStat", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team3[i].split("\\|")[0], "vipStat"));
			map.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team3[i].split("\\|")[0], "pic"));
			map.put("description", team3[i].split("\\|")[1]);
			map.put("corps", 3);//战队标识，1：周杰伦 2：汪峰 3：那英 4：庾澄庆
			list.add(map);
		}
		for (int i=0;i<team4.length;i++) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", team4[i].split("\\|")[0]);
			map.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team4[i].split("\\|")[0], "name"));
			map.put("vipStat", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team4[i].split("\\|")[0], "vipStat"));
			map.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team4[i].split("\\|")[0], "pic"));
			map.put("description", team4[i].split("\\|")[1]);
			map.put("corps", 4);//战队标识，1：周杰伦 2：汪峰 3：那英 4：庾澄庆
			list.add(map);
		}
		for (int i=0;i<team5.length;i++) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", team5[i].split("\\|")[0]);
			map.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team5[i].split("\\|")[0], "name"));
			map.put("vipStat", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team5[i].split("\\|")[0], "vipStat"));
			map.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+team5[i].split("\\|")[0], "pic"));
			map.put("description", team5[i].split("\\|")[1]);
			map.put("corps", 5);//战队标识，1：周杰伦 2：汪峰 3：那英 4：庾澄庆
			list.add(map);
		}
		
		return respBodyWriter.toSuccess(list);
	}
	
	//将马甲用户放到redis中
	@RequestMapping("/setMajiaToRedis")
	@ResponseBody
	public RespBody setMajiaToRedis() {
		List<Ruser> list = ruserService.findByType("majia");
		for (Ruser ruser : list) {
			String uidStr = String.valueOf(ruser.getId());
			//将用户的uid放到set中
			jedisService.setValueToSetInShard(BicycleConstants.MAJIA_UID, uidStr);
			//将用户信息放到map中
			jedisService.setValueToMap(BicycleConstants.MAJIA+uidStr, "uid", uidStr);
			jedisService.setValueToMap(BicycleConstants.MAJIA+uidStr, "name", ruser.getName()==null?"":ruser.getName());
			jedisService.setValueToMap(BicycleConstants.MAJIA+uidStr, "pic", ruser.getPic()==null?"":ruser.getPic());
			jedisService.setValueToMap(BicycleConstants.MAJIA+uidStr, "vstat", String.valueOf(ruser.getVipStat()));
		}
		return respBodyWriter.toSuccess();
	}

	private List<AttentionVO> getAttentionEachOther(List<AttentionVO> attList, String uid) {
		List<AttentionVO> result = new ArrayList<AttentionVO>();
		for (AttentionVO attentionVO : attList) {
			if (attentionVO == null) {
				continue;
			}
			Set<String> attentionSet = jedisService.getSetFromShard(BicycleConstants.ATTENTION_ID_OF + attentionVO.getAttentionId());
			if (attentionSet.contains(uid)) {
				attentionVO.setIsAttention(2);
			} else {
				attentionVO.setIsAttention(1);
			}
			result.add(attentionVO);
		}
		return result;
	}

	private List<FansVO> getOtherFansAttentionEachOther(List<FansVO> fansList, String uid) {
		List<FansVO> result = new ArrayList<FansVO>();
		for (FansVO fansVO : fansList) {
			if (fansVO == null) {
				continue;
			}
			if (attentionService.isAttention(Long.parseLong(uid), fansVO.getFansId()) == 1) {
				fansVO.setIsAttention(1 + attentionService.isAttention(fansVO.getFansId(), Long.parseLong(uid)));
			} else {
				fansVO.setIsAttention(0);
			}
			result.add(fansVO);
		}
		return result;
	}
}
