package com.busap.vcs.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import scala.actors.threadpool.Arrays;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.SingConstants;
import com.busap.vcs.data.entity.Gift;
import com.busap.vcs.data.entity.SingVote;
import com.busap.vcs.data.mapper.ConsumeRecordDAO;
import com.busap.vcs.data.mapper.GiftDao;
import com.busap.vcs.data.model.GiftDisplay;
import com.busap.vcs.data.repository.GiftRepository;
import com.busap.vcs.service.AnchorService;
import com.busap.vcs.service.GiftService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.SingVoteService;

/**
 * Created by busap on 2015/12/23.
 */
@Service("giftService")
public class GiftServiceImpl extends BaseServiceImpl<Gift, Long> implements GiftService {

	@Resource(name = "giftRepository")
	private GiftRepository giftRepository;

    @Resource
    private GiftDao giftDao;
    
    @Resource(name = "anchorService")
	private AnchorService anchorService;
    
    @Resource(name="singVoteService") 
    SingVoteService singVoteService; 
    
    @Resource
    private ConsumeRecordDAO consumeRecordDAO;
    
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name="roomService")
	private RoomService roomService;
	
    @Override
    public int deleteByPrimaryKey(Long id) {
        return giftDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Gift record) {
        return giftDao.insert(record);
    }

    @Override
    public Gift selectByPrimaryKey(Long id) {
        return giftDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Gift> selectAll(GiftDisplay giftDisplay) {
        return giftDao.selectAll(giftDisplay);
    }

    @Override
    public int updateByPrimaryKey(Gift record) {
        return giftDao.updateByPrimaryKey(record);
    }

	@Override
	public Map<String,Object> sendGift(Long id, Integer number, Long senderId,	Long recieverId,Long roomId,String appVersion,String platform,String channel) {
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String,String> roomInfo = jedisService.getMapByKey(BicycleConstants.ROOM_+roomId);
		if(roomInfo == null || roomInfo.isEmpty()){
			result.put("code", -9);
			return result;
		}
		
		String roomCreator = roomInfo.get("creatorId");
		
		if(!recieverId.toString().equals(roomCreator)){
			result.put("code", -9);
			return result;
		}
		
		Gift gift = new Gift();
				
		if(jedisService.ifKeyExists(BicycleConstants.GIFT+id)){
			Map<String,String> gMap = jedisService.getMapByKey(BicycleConstants.GIFT+id);
			try {
				BeanUtils.populate(gift, gMap);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(gift.getId() == null || gift.getState().intValue()!=1){
				result.put("code", -2);
				return result;
			}
		}else {//礼物不存在
			result.put("code", -2);
			return result;
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("giftId", id);
		params.put("number", number);
		params.put("senderId", senderId);
		params.put("recieverId", recieverId);
		params.put("roomId", roomId);
		params.put("appVersion", appVersion);
		params.put("platform", platform);
		params.put("channel", channel);
		

		if(gift.getIsFree().intValue()==1){//免费礼物
			Integer sentFreeCount = 1;
			if(jedisService.ifKeyExists(BicycleConstants.DAY_FREE_GIFT+senderId+"_"+id)){
				String sentFree = jedisService.get(BicycleConstants.DAY_FREE_GIFT+senderId+"_"+id);
				if(StringUtils.isNumeric(sentFree)){
					Integer sentCount = Integer.parseInt(sentFree);
					sentFreeCount = sentFreeCount.intValue() + sentCount.intValue();
					
					if(sentCount.intValue()<gift.getFreeCount().intValue()){
						jedisService.incr(BicycleConstants.DAY_FREE_GIFT+senderId+"_"+id);
					} else {
						result.put("code", -3);
						return result;//超过免费礼物每日送出上限
					}
				}
			} else {
				jedisService.set(BicycleConstants.DAY_FREE_GIFT+senderId+"_"+id,sentFreeCount.toString());
				jedisService.expire(BicycleConstants.DAY_FREE_GIFT+senderId+"_"+id, getExpireTime());
			}
			
			result.put("freeCount", (gift.getFreeCount().intValue()-sentFreeCount.intValue()));
		} 
		//添加分成比例参数
		double divide_rate = 0.04d;
		String rate = jedisService.get(BicycleConstants.DIVIDE_RATE);
		if(StringUtils.isNotBlank(rate)){
			divide_rate = Double.valueOf(rate);
		}
		params.put("divideRate", divide_rate);
		
		Integer errCode = giftDao.sendGift(params);
		
		if(errCode == null){
			result.put("code", -9);
			return result;
		}else{
			if(gift.getIsFree().intValue()==1){
				result.put("beans", 0);
			} else if (errCode >= 0) {
				Integer points = gift.getPoint() * number;
				Integer diamonds = gift.getPrice() * number; 
				result.put("beans", points);
				jedisService.zincrByScore(BicycleConstants.LIVE_CONTRIBUTION_LIST + recieverId, senderId+"", points.doubleValue());
				
//				if(gift.getLoopSupport()!=null && gift.getLoopSupport().intValue() == 1){//可连续赠送
					jedisService.zincrByScore(BicycleConstants.LOOP_GIFT_RECORD + roomId +"_"+ senderId, id + "", number.doubleValue());
//				}
				createExclusiveRank(id, roomId,number,points,diamonds,recieverId,senderId);
				createSingPopularity(id, roomId,number,points,diamonds,recieverId,senderId);
				createWangzongRank(id, points);
				try { //计算每场收到的礼物数量和金豆数量
					jedisService.incr(BicycleConstants.GIFT_NUMBER_BY_ROOM_+roomId, number.longValue());
					jedisService.incr(BicycleConstants.POINT_NUMBER_BY_ROOM_+roomId, gift.getPoint().longValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			result.put("code", errCode);
			if(errCode.intValue()>=0){
				result.put("remand", errCode);
			}
		}
		
		return result;
	}
	
	/**
	 * 生成好声音人气值
	 * @param id
	 * @param roomId
	 * @param number
	 * @param points
	 * @param diamonds
	 * @param recieverId
	 * @param senderId
	 */
	private void createSingPopularity(Long id, Long roomId, Integer number,
			Integer points, Integer diamonds, Long recieverId, Long senderId) {
		try {
			Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);//缓存中获得房间信息
			if (room != null) {
				String liveActivityId = room.get("liveActivityId"); //获得房间对应的直播活动id
				String singActivityId = jedisService.get(SingConstants.SING_ACTIVITY_ID);
				if (StringUtils.isNotBlank(liveActivityId) && liveActivityId.equals(singActivityId)) { //如果是新歌声活动，则进行人气榜数据生成
					SingVote sv = new SingVote();
					sv.setCreatorId(senderId); 
					sv.setDestId(recieverId);
					sv.setType(2);
					sv.setValue(points.longValue());
					Integer radio = Integer.parseInt(jedisService.get(SingConstants.GIFT_VOTE_RATIO)); //获得礼物投票系数
					if (jedisService.isSetMemberInShard(SingConstants.SING_MEMBER, String.valueOf(recieverId))) {  //判断是否是新歌声学员
						sv.setUserType(1);
					} else {
						sv.setUserType(2);
					}
					sv.setPopularity(radio*points.longValue());
					singVoteService.save(sv);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 直播活动：生成专属礼物排行榜
	 * @param giftId
	 * @param roomId
	 */
	private void createExclusiveRank(Long giftId,Long roomId,Integer number,Integer points,Integer diamonds,Long recieverId,Long senderId) {
		try {
			Map<String,String> room = jedisService.getMapByKey(BicycleConstants.ROOM_+ roomId);//缓存中获得房间信息
			if (room != null && room.get("liveActivityId") != null) {
				String liveActivityId = room.get("liveActivityId"); //获得房间对应的直播活动id
				if (liveActivityId != null && !"".equals(liveActivityId)) {
					Map<String,String> liveActivity = jedisService.getMapByKey(BicycleConstants.LIVE_ACTIVITY_+room.get("liveActivityId")); //获得直播活动信息
					String giftIds = liveActivity.get("giftIds"); //获得直播活动对应的专属礼物
					if (giftIds != null && !"".equals(giftIds)) {
						List list = Arrays.asList(giftIds.split(","));
						if (list.contains(String.valueOf(giftId))){
							jedisService.zincrByScore(BicycleConstants.EXCLUSIVE_GIFT_RECORD_ + liveActivityId, recieverId + "", points.doubleValue());  //计算主播收到专属礼物金豆排行
							jedisService.zincrByScore(BicycleConstants.EXCLUSIVE_GIFT_RECORD_DIAMOND_ + liveActivityId, senderId + "", diamonds.doubleValue()); //计算用户送出专属礼物金币排行
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成网综排行榜
	 * @param giftId
	 */
	private void createWangzongRank(Long giftId,Integer points) {
		try {
			Set<String> set = jedisService.getSetFromShard(SingConstants.WANGZONG_INFO);
			
			if (set != null && set.size() > 0) {
				for (String str : set) {  
				      String userId = str.split("\\|")[0]; //获得用户id
				      String[] giftIds = str.split("\\|")[1].split(","); //获得该用户对应的礼物id
				      for (int i=0;i<giftIds.length;i++) {
				    	  if (String.valueOf(giftId).trim().equals(giftIds[i].trim())) { 
				    		  jedisService.zincrByScore(SingConstants.WANGZONG_RANK, userId, points.doubleValue());  
				    		  return;
				    	  }
				      }
				}  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 获取今天剩余时间，用与用户当日免费礼物保留时间
	 * 
	 * @return
	 */
	public int getExpireTime() {
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat ss1 = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date(new Date().getTime() + 24 * 60 * 60 * 1000);
		String dt = ss.format(d);
		try {
			Date d1 = ss1.parse(dt);
			return Integer.parseInt(((d1.getTime() - System.currentTimeMillis()) / 1000) + "");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Long excuteAnchorTotalPoints(Long receiverId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("recieverId", receiverId);
		if(!jedisService.ifKeyExists(BicycleConstants.LIVE_CONTRIBUTION_LIST + receiverId)){
			List<Map<String,Object>> historyList = consumeRecordDAO.getContributionHistory(receiverId);
			if(historyList != null && historyList.size()>0){
				for(Map<String,Object> history:historyList){
					BigDecimal points = (BigDecimal)history.get("points");
					jedisService.zincrByScore(BicycleConstants.LIVE_CONTRIBUTION_LIST + receiverId, history.get("sender").toString(), points.doubleValue());
				}
			}
		}
//		return consumeRecordDAO.getAnchorTotalRecievePoints(params);
		return anchorService.getAnchorByUserid(receiverId).getTotalPointCount().longValue();
	}

	@Override
	public List<Gift> selectByIds(List<Long> ids) {
		return giftRepository.findGiftsByIds(ids);
	}

	@Override
	public List<Gift> findExclusiveGifts() {
		return giftRepository.findExclusiveGifts();
	}

	@Override
	public List<Gift> findExclusiveGiftsByState(Integer state){
		return giftRepository.findExclusiveGiftsByState(state);
	}
	
	@Override
	public List<Map<String,Object>> reCount(Long liveActivityId,List<Long> idList){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("liveActivityId", liveActivityId);
		params.put("ids", idList);
		return giftDao.reCount(params);
	}
}
