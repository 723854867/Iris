package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import scala.actors.threadpool.Arrays;

import com.busap.vcs.base.WsMessage;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Gift;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.GiftService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.kafka.producer.WsMessageProducer;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller
@RequestMapping("gift")
public class GiftController  extends CRUDController<Gift, Long>{
	private Logger logger = LoggerFactory.getLogger(GiftController.class);
	
	@Resource(name="giftService")
	private GiftService giftService;
	
	@Resource(name="wsMessageProducer")
	private WsMessageProducer wsMessageProducer;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name="giftService")
    @Override
    public void setBaseService(BaseService<Gift, Long> baseService) {
        this.baseService = baseService;
    }
	
	@RequestMapping("sendgift")
	@ResponseBody
	public RespBody sendGift(Long id,Integer number,Long recieverId,Long roomId,String appVersion,String osVersion){
		String uid = (String)this.request.getHeader("uid"); 
		logger.info("sendgift start,giftId{},number{},sender{},reciever{},live room{},appVersion:{},osVersion:{}",id,number,uid,recieverId,roomId,appVersion,osVersion);
		if(number<=0){
			return new RespBody(false,"不合法的礼物数量！",null,-9);
		}
		if(jedisService.isSetMemberInShard(BicycleConstants.BLACK_LIST_USER_ID+recieverId, uid)){
			return new RespBody(false,"已被拉入黑名单，赠送失败",null,-4);
		}
		
		if (isExpired(id, roomId)){ //专属礼物过期，不让发送
			return new RespBody(false,"活动已结束，专属礼物停止使用",null,-5);
		}
		String platform = "android";
		if (osVersion != null && osVersion.contains("ios")){
			platform = "ios";
		}
		String channel = jedisService.getValueFromMap(BicycleConstants.USER_INFO+uid, "regPlatform");
		if (channel == null) {
			channel = "";
		}
		Map<String,Object> result = giftService.sendGift(id, number, Long.parseLong(uid), recieverId, roomId,appVersion,platform,channel);
		Integer code = (Integer)result.get("code");
		if (code == null) {
			return new RespBody(false,"服务器错误，赠送失败",null,-9);
		}
		if(code.intValue()>=0){
			Double score = jedisService.zscore(BicycleConstants.LOOP_GIFT_RECORD + roomId +"_"+ uid,id+"");
			if(score != null && score>0){
				number = score.intValue();
			}
			String screenshotNumber = jedisService.getValueFromMap(BicycleConstants.GIFT+id, "screenshotNumber");
			String isExclusive = jedisService.getValueFromMap(BicycleConstants.GIFT+id, "isExclusive");//是否是专属礼物
			if (isExclusive == null || "".equals(isExclusive)){
				isExclusive = "0";
			}
			Integer beans = (Integer)result.get("beans");
			WsMessage message = buildMessage(uid,id,number,recieverId,roomId,beans,Integer.parseInt(isExclusive));
			//判断本次发送礼物是否需要截屏
			if (screenshotNumber != null && !"".equals(screenshotNumber)) {
				if (Integer.parseInt(screenshotNumber) !=0 && number == Integer.parseInt(screenshotNumber)) {
					Map<String,Object> extra = message.getExtra();
					extra.put("needScreenshot", "1");
				}
			}
						
			wsMessageProducer.send("chat_topic_", message);
			logger.info("sendgift OK,message:{}",message);
			return this.respBodyWriter.toSuccess(result);
		}else{
			switch(code){
				case -1:
					return new RespBody(false,"余额不足，请及时充值",null,-1);
				case -2:
					return new RespBody(false,"所购物品不存在或已下架",null,-2);
				case -3:
					return new RespBody(false,"已达到今日免费礼物赠送上限",null,-3);
				default:
					return new RespBody(false,"服务器错误，赠送失败",null,-9);
			}
		}
	}
	
	//判断活动结束，停止专属礼物的发送
	private boolean isExpired(Long giftId,Long roomId) {
		try {
			Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);//缓存中获得房间信息
			if (room != null && StringUtils.isNotBlank(room.get("liveActivityId")) ) {
				String liveActivityId = room.get("liveActivityId"); //获得房间对应的直播活动id
				if (liveActivityId != null && !"".equals(liveActivityId)) {
					Map<String,String> liveActivity = jedisService.getMapByKey(BicycleConstants.LIVE_ACTIVITY_+room.get("liveActivityId")); //获得直播活动信息
					String giftIds = liveActivity.get("giftIds"); //获得直播活动对应的专属礼物
					if (giftIds != null && !"".equals(giftIds)) {
						List list = Arrays.asList(giftIds.split(","));
						if (list.contains(String.valueOf(giftId))){ //是直播活动的专属礼物，判断活动是否过期
							String endTime = liveActivity.get("endTime");
							if (System.currentTimeMillis() >= Long.parseLong(endTime)){
								return true;
							}
							return false;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			} else { //不是活动直播，不判断礼物
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private WsMessage buildMessage(String senderId, Long id, Integer number, Long recieverId, Long roomId,Integer beans,Integer isExcluesive) {
		WsMessage mess = new WsMessage();
		mess.setCode("500");
		mess.setChildCode("5001");
		mess.setSenderId(senderId);
		mess.setRecieverId(String.valueOf(recieverId));
		mess.setRoomId(String.valueOf(roomId));
		mess.getExtra().put("giftId", id);
		mess.getExtra().put("number", number);
		Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO+senderId);
		mess.setSenderName(userInfo.get("name"));
		mess.getExtra().put("userid", senderId);
		mess.getExtra().put("name", userInfo.get("name"));
		mess.getExtra().put("username", userInfo.get("username"));
		mess.getExtra().put("signature", userInfo.get("signature"));
		mess.getExtra().put("vipStat", userInfo.get("vipStat"));
		mess.getExtra().put("pic", userInfo.get("pic"));
		mess.getExtra().put("sex", userInfo.get("sex"));
		mess.getExtra().put("loginTime", userInfo.get("loginTime"));
		mess.getExtra().put("beans", beans);
		mess.getExtra().put("isExcluesive", isExcluesive);
		
		return mess;
	}
	
	
	//重新计算某个活动的金豆榜
	@RequestMapping("reCount")
	@ResponseBody
	public RespBody reCount(Long liveActivityId,String ids){
		logger.info("reCount,liveActivityId="+liveActivityId);
		String[] ss = ids.split(",");
		List<Long> idList = new ArrayList<Long>();
		for (int i=0;i<ss.length;i++) {
			idList.add(Long.parseLong(ss[i]));
		}
		List<Map<String,Object>> list = giftService.reCount(liveActivityId, idList);
		for(int i=0;i<list.size();i++) {
			Map<String,Object> map = list.get(i);
			jedisService.zincrByScore(BicycleConstants.EXCLUSIVE_GIFT_RECORD_ + liveActivityId, String.valueOf(map.get("reciever")) , Double.parseDouble(String.valueOf(map.get("dc"))));  //计算主播收到专属礼物金豆排行
		}
		return this.respBodyWriter.toSuccess();
	}
}
