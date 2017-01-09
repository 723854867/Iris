package com.busap.vcs.service.impl;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Tuple;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.mapper.AttentionDAO;
import com.busap.vcs.data.vo.AttentionRecordVO;
import com.busap.vcs.data.vo.AttentionVO;
import com.busap.vcs.data.vo.FansVO;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;

@Service("attentionService")
public class AttentionServiceImpl implements AttentionService {
	
	@Autowired
	AttentionDAO attention;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Override
	@Transactional
	public int deleteAttention(Long uid, Long attentionId, String dataFrom) {
		Ruser user = ruserService.find(uid); // 关注用户
		Ruser attentionUser = ruserService.find(attentionId);// 被关注用户
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("creatorId", uid);
		params.put("attentionId", attentionId);
		params.put("createDate", new Date());
		params.put("dataFrom", dataFrom);
		
		int result =  attention.delete(params);
		
		if (result > 0){
			// 更新用户表中的关注数和粉丝数
			user.setAttentionCount(attention.getAttentionCount(uid));
			attentionUser.setFansCount(attention.getFansCount(attentionId));
			ruserService.save(user);
			ruserService.save(attentionUser);
			//计算用户人气
			ruserService.executeDayUserPopularity(uid);
		}
		
		return result;
	}

	@Override
	public int getAttentionCount(Long uid) {
		return attention.getAttentionCount(uid);
	}

	@Override
	public int getFansCount(Long uid) {
		return attention.getFansCount(uid);
	}

	@Override
	public int isAttention(Long uid, Long otherUid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("otherUid", otherUid);
		return attention.isAttention(params);
	}

	@Override
	@Transactional
	public int payAttention(Long uid, Long attentionId, String dataFrom) {
		Ruser user = ruserService.find(uid); // 关注用户
		Ruser attentionUser = ruserService.find(attentionId);// 被关注用户
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("creatorId", uid);
		params.put("attentionId", attentionId);
		params.put("createDate", new Date());
		params.put("dataFrom", dataFrom);
		params.put("status", 0);

		int result = attention.insert(params);
		if (result > 0) {
			// 更新用户表中的关注数和粉丝数
			user.setAttentionCount(attention.getAttentionCount(uid));
			attentionUser.setFansCount(attention.getFansCount(attentionId));
			ruserService.save(user);
			ruserService.save(attentionUser);
			//计算用户人气
			ruserService.executeDayUserPopularity(uid);
		}
		
		return result;
	}

	@Override
	public List<AttentionVO> getAttentionList(Long uid, Date timestamp,
			int count) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("creatorId", uid);
		params.put("timestamp", timestamp);
		params.put("count", count);
		return attention.selectAllAttention(params);
	}
	
	@Override
	public List<AttentionVO> getAttentionListNew(Long uid, Long lastId,
			int count) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("creatorId", uid);
		if (lastId.intValue() == 0 ) {
			params.put("lastId", null);
		} else {
			params.put("lastId", lastId);
		}
		params.put("count", count);
		return attention.selectAllAttentionNew(params);
	}

	@Override
	public List<AttentionVO> findAttentionByCreator(Long uid, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date timestampStart = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date timestampEnd = calendar.getTime();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("creatorId", uid);
		params.put("timestampStart", timestampStart);
		params.put("timestampEnd", timestampEnd);

		return attention.selectAttentionByCreator(params);
	}



	@Override
	public List<FansVO> getFansList(Long uid, Date timestamp, int count) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("attentionId", uid);
		params.put("timestamp", timestamp);
		params.put("count", count);
		return attention.selectAllFans(params);
	}

	@Override
	public List<Long> selectAllAttentionId(Long uid) {
		return attention.selectAllAttentionId(uid);
	}
	
	@Override
	public List<Long> selectAllFansId(Long uid) {
		return attention.selectAllFansId(uid);
	}
	
	@Override
	public List<Long> selectAllFansIdWithoutMajia(Long uid) {
		return attention.selectAllFansIdWithoutMajia(uid);
	}

	@Override
	public List<AttentionVO> getOtherAttentionList(Long uid, Long otherUid,
			Date timestamp, int count) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("otherUid", otherUid);
		params.put("timestamp", timestamp);
		params.put("count", count);
		
		return attention.selectOtherAttention(params);
	}

	@Override
	public List<FansVO> getOtherFansList(Long uid, Long otherUid,
			Date timestamp, int count) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("otherUid", otherUid);
		params.put("timestamp", timestamp);
		params.put("count", count);
		
		return attention.selectOtherFans(params);
	}

	@Override
	public List<AttentionRecordVO> getAllRecords() {
		return attention.getAllRecords();
	}

	@Override
	public List<Ruser> getDynamicRecommend(Long uid,Integer count) {
		List<Long> ids = selectAllAttentionId(uid);
		List<Ruser> list = ruserService.findRecommondVipUsers(ids, count);
		
		//列表第一个随机
		Random random = new Random();
		int rad = random.nextInt(list.size());
		Ruser ruser = list.get(rad);
		list.remove(rad);
		
		List<Ruser> result = new ArrayList<Ruser>();
		result.add(ruser);
		result.addAll(list);
		return result;
	}

	@Override
	public void deleteAttentionByIds(List<Long> ids) {
		try {
			attention.deleteAttention(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void autoAttention() {
		//删除已经过期的被自动关注的用户id
		jedisService.deleteSortedSetItemByScore(BicycleConstants.AUTO_ATTENTION_UID, 0, Double.parseDouble(String.valueOf(System.currentTimeMillis())));
		//获得需要被自动关注的用户id
		Set<String> set = jedisService.getSortedSetByScore(BicycleConstants.AUTO_ATTENTION_UID,System.currentTimeMillis(), 2453089822018l);
		for (String attentionId:set) {
			try {
				Ruser attentionUser = ruserService.find(Long.parseLong(attentionId)); //获得被关注用户信息
				if (attentionUser != null && attentionUser.getStat() != 2){
					/*************获得关注操作执行的时间段-begin*************/
					//获得当前时间前30分钟
					Calendar calendar = Calendar.getInstance();
					calendar = Calendar.getInstance();
					calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 30);
					Date date = calendar.getTime();
					
					Long start = attentionUser.getCreateDate();
					if (date.getTime() > start) {
						start = date.getTime();
					}
					start = start+1000*60;
					Long end = System.currentTimeMillis();
					
					Long timeInterval = (end-start)<=0?0:(end-start);
					
					/*************获得关注操作执行的时间段-end*************/
					//获得自动关注执行次数
					int executeCount = getRandom(20, 30);
					
					//执行自动关注
					for (int i=0;i<executeCount;i++){
						try {
							Long attentionTime = start+getRandom(0, timeInterval.intValue());  //生成关注时间
							//获得马甲用户id
							String majiaUid = jedisService.getSrandmember(BicycleConstants.ATTENTION_MAJIA_ID_OF_+attentionId);
							
							if (majiaUid == null || "".equals(majiaUid)){  //如果马甲用完了 ，不执行关注
								break;
							}
							
							if(isAttention(Long.parseLong(majiaUid), Long.parseLong(attentionId)) == 0) {//没关注过，进行关注操作
								Ruser user = ruserService.find(Long.parseLong(majiaUid)); // 关注用户
								
								if (user != null) {
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("creatorId", majiaUid);
									params.put("attentionId", attentionId);
									params.put("createDate", new Date(attentionTime));
									params.put("dataFrom", "restadmin");
									params.put("status", 0);
									params.put("majiaAttention", 1);

									int result = attention.insert2(params);
									if (result > 0) {
										// 更新用户表中的关注数和粉丝数
										user.setAttentionCount(attention.getAttentionCount(Long.parseLong(majiaUid)));
										attentionUser.setFansCount(attention.getFansCount(Long.parseLong(attentionId)));
										ruserService.save(user);
										ruserService.save(attentionUser);
									}
								}
							}
							//删除此次随机的关注马甲id
							jedisService.deleteSetItemFromShard(BicycleConstants.ATTENTION_MAJIA_ID_OF_+attentionId, majiaUid);
							//将此次关注使用过的马甲id放入到该用户的马甲粉丝列表中
							jedisService.setValueToSetInShard(BicycleConstants.MAJIA_FANS_ID_OF_+attentionId, majiaUid);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private int getRandom(int min,int max){
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
	}
}
