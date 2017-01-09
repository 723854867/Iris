package com.busap.vcs.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.NoRedis;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.SystemMess;
import com.busap.vcs.data.mapper.RuserDAO;
import com.busap.vcs.data.model.ExportWopaiNormalUser;
import com.busap.vcs.data.model.ExportWopaiUser;
import com.busap.vcs.data.model.RuserDisplay;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.RuserRepository;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.ReflectionService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.SystemMessService;
import com.busap.vcs.service.security.SubjectPermissionProvider;

@Service("ruserService")
public class RuserServiceImpl extends BaseServiceImpl<Ruser, Long> implements
		RuserService, SubjectPermissionProvider {

	private Logger logger = LoggerFactory.getLogger(RuserServiceImpl.class);
	
	@Resource(name="roomService")
	private RoomService roomService;
	
	@Resource(name = "ruserRepository")
	@Override
	public void setBaseRepository(BaseRepository<Ruser, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	}

	@Autowired
	private RuserRepository ruserRepository;

	@Autowired
	RuserDAO ruserDAO;

	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name = "systemMessService")
	private SystemMessService systemMessService;

	@Resource
	private SolrUserService solrUserService;

	// @Autowired
	// private PasswordHelper passwordHelper;

	@Override
	public Ruser createUser(Ruser user) {
		// 加密
		// passwordHelper.encryptPassword(user);
		throw new UnsupportedOperationException("不支持的操作");
		// return ruserRepository.save(user);
	}

	@Override
	public void changePassword(Long userId, String newPassword) {
		throw new UnsupportedOperationException("不支持的操作");
		// Ruser user = ruserRepository.findOne(userId);
		// user.setPassword(newPassword);
		// passwordHelper.encryptPassword(user);
		// ruserRepository.saveAndFlush(user);
	}

	@Override
	public Ruser findByUsername(String username) {
		return ruserRepository.findByUsername(username);
	}
	
	@Override
	public Ruser findByBandPhone(String bandPhone) {
		return ruserRepository.findByBandPhone(bandPhone);
	}

	@Override
	public List<Long> fuzzyQueryByName(String username) {
		return ruserRepository.findByNameLike(username);
	}

	@Override
	public List<Long> fuzzyQueryByPhone(String phone) {
		return ruserRepository.findByPhoneLike(phone);
	}

	@Override
	public Ruser findByWechatUnionid(String wechatUnionid) {
		return ruserRepository.findByWechatUnionid(wechatUnionid);
	}

	@Override
	public List findByType(String type) {
		return this.ruserRepository.findByType(type);
	}

	@Override
	public List<String> findPermissions() {
		return null;
	}

	@Override
	@Cacheable(value = "userRolePermission")
	public List<String> findPermissions(Long userId) {
		return null;
	}

	@Override
	public List<String> findPermissions(Subject subject) {
		return null;
	}

	@Override
	public List<String> findPermissions(Subject subject, String patten) {
		return null;
	}

	@Override
	public List<String> findPermissions(String patten) {
		return null;
	}

	@Override
	public List<String> findPermissionsByStartWith(Subject subject,
												   String startWith) {
		return null;
	}

	@Override
	public List<String> findPermissionsByStartWith(String startWith) {
		return null;
	}

	@Override
	public List<String> findPermissionsId(Long id) {
		return null;
	}

	public boolean existByName(String name, Long uid) {
		int c = ruserRepository.countByName(name, uid);
		if (c == 0)
			return false;
		else
			return true;
	}

	public boolean existByName(String name) {
		int c = ruserRepository.countByName(name);
		if (c == 0)
			return false;
		else
			return true;
	}

	public boolean countByThirdName(String thirdUsername, String thirdFrom) {
//		int c = ruserRepository.countByThirdName(thirdUsername, thirdFrom);
		int c = ruserRepository.countByUsername(thirdFrom+thirdUsername ); //改用索引字段查询
		if (c == 0)
			return false;
		return true;
	}
	
	public boolean countByWechatUnionid(String unionid, String thirdFrom) {
		int c = ruserRepository.countByWechatUnionid(unionid, thirdFrom);
		if (c == 0)
			return false;
		return true;
	}

	@Override
	public boolean isUserInfoCompletion(String username) {
		Ruser ruser = findByUsername(username);
		if (ruser == null)
			return false;
/*		if(StringUtils.isBlank(ruser.getEmail()))
			return false;*/
		if(StringUtils.isBlank(ruser.getName()) || StringUtils.isBlank(ruser.getPic()) || StringUtils.isBlank(ruser.getSex()))
			return false;
		//TODO huoshanwei 修改
		return true;
	}

	public Map<Long,Ruser> getRusers(List<Long> ids){
//		Date s = new Date();
		List<Ruser> ls = ruserDAO.getRusers(ids);
		Map<Long,Ruser> map = new HashMap<Long,Ruser>(20);
		for(int i=0;ls!=null&&i<ls.size();i++){
			if(ls.get(i)!=null)
				map.put(ls.get(i).getId(), ls.get(i));
		}
//		Date e = new Date();
//		logger.info("一次获取,ruser信息,耗时，time="+(e.getTime()-s.getTime())+"毫秒");
		return map;
	}

	@Override
	public Long registNum() {
		// TODO Auto-generated method stub
		return ruserRepository.countAll();
	}

	@Override
	public Page searchNormalUserList(Integer pageNo, Integer pageSize, Map<String, Object> params) {
		List<Ruser> list = ruserDAO.getNormalUser(params);
		Integer count = ruserDAO.getNormalUserCount(params);
		return  new PageImpl<Ruser>(list,new PageRequest(pageNo-1, pageSize, null),count);
	}

	@Override
	public List<Long> findIdsByVipState(int vipstate) {
		return this.ruserRepository.findIdsByVipStat(vipstate);
	}

	@Override
	public List<Long> findAllIds() {
		return this.ruserRepository.findAllIds();
	}

	@Override
	public List<Ruser> findUserByKeyWord(String uid,Date timestamp,String keyWord,int count) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("timestamp", timestamp);
		params.put("keyWord", keyWord);
		params.put("count", count);
		params.put("uid", uid);
		List<Ruser> list = ruserDAO.findUserByPhoneOrName(params);
		return list == null?new ArrayList<Ruser>():list;
	}

	@Override
	public List<Ruser> findUsersByIds(List<Long> ids) {
		return ruserRepository.findRUsersByIds(ids);
	}

	@Override
	public Map<String,String> getUserFromRedis(Long id) {
		Map<String,String> map = jedisService.getMapByKey(BicycleConstants.USER_INFO+id);
		//如果redis中取不到用户信息，去数据库查询
		if (map == null || map.size() == 0){
			Ruser user = find(id);
			if (user != null) {
				//将数据库查询出来的用户信息解析成map返回
				List<Field> fields = ReflectionService.getDeclaredFields(user);
				map = new HashMap<String, String>();
				for (Field field : fields) {
					NoRedis noRedis = field.getAnnotation(NoRedis.class);
					if (noRedis != null) {// 设置为不需要保存redis的字段直接跳过
						continue;
					} else {
						String fieldName = field.getName();
						Object o = ReflectionService.getFieldValue(user, fieldName);
						String value = "";
						if (o != null) {
							value = o.toString();
						}
						map.put(fieldName, value);
					}
				}

				//再重新将将用户信息放入到redis
				String key = BicycleConstants.USER_INFO+user.getId();
				jedisService.saveAsMap(key, user);
			}
		}
		if (map != null && map.size()>0)
			map.put("password", "");
		return map;
	}

	@Override
	public void dayUserPopularityToRedis() {
		List<Ruser> rusers = ruserDAO.findDayUserPopularityTop50(50);
		if(rusers!=null && rusers.size()>0){
			jedisService.setObject(BicycleConstants.DAY_USER_POPULARITY, rusers);
			jedisService.set(BicycleConstants.USER_POPULARITY_UPDATE_TIME, (new Date().getTime())+"");
			logger.info("======当日用户人气排行，前50======size:"+rusers.size());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ruser> findDayUserPopularity(Long uid,Integer start,Integer count) {
		List<Ruser> userRank = (List<Ruser>)jedisService.getObject(BicycleConstants.DAY_USER_POPULARITY);
		Ruser self = null;
		if(userRank != null && userRank.size()>0){
			if(uid != null && uid>0){
				self = this.find(uid);
				if(self != null){
					for(int i=0;i<userRank.size();i++){
						if(self.getId().equals(userRank.get(i).getId())){
							self.setHotRank(i+1);
							break;
						}
					}
				}
			}
			if(start == null || start.intValue()>=userRank.size()){
				start = 0;
			}
			if(count == null || count.intValue()>=userRank.size()){
				count = userRank.size() - start;
			}
			if((start + count)<=userRank.size()){
				userRank = userRank.subList(start, start + count);
			}
			if(self != null){
				userRank.add(self);
			}
		}

		return userRank;
	}

	@Override
	public List<Map<String, Object>> lastXDayUserNum(String date) {
		return this.ruserDAO.last5DayUserNum(date);
	}

	@Override
	public List<Ruser> findVipUsers() {

		return ruserRepository.findVipUser();
	}
	
	@Override
	public List<Ruser> findRecommondVipUsers(Collection ids,int count) {
		if (ids == null || ids.size() == 0){
			return ruserRepository.findRecommondVipUser( count);
		}
		return ruserRepository.findRecommondVipUser( count,ids);
	}

	@Override
	public void executeDayUserPopularity(Long uid) {

		Map<String,Object> params = new HashMap<String,Object>();
		String videoCount = jedisService.getValueFromMap(BicycleConstants.DAY_POPULARITY_WEIGHT, BicycleConstants.DAY_VIDEOCOUNT_WEIGHT);
		Double videoCountRate = 0.3;
		if(StringUtils.isNotBlank(videoCount)){
			videoCountRate = Double.parseDouble(videoCount);
		}
		String fansCount = jedisService.getValueFromMap(BicycleConstants.DAY_POPULARITY_WEIGHT, BicycleConstants.DAY_FANSCOUNT_WEIGHT);
		Double fansCountRate = 0.7;
		if(StringUtils.isNotBlank(fansCount)){
			fansCountRate = Double.parseDouble(fansCount);
		}

		Integer videos = ruserDAO.dayUserVideos(uid);

		Integer fans = ruserDAO.dayUserFans(uid);

		Double popularity = videos*videoCountRate +fans*fansCountRate;
		logger.info("execute day user popularity,uid:{},popularity:{}",uid,popularity);
		params.put("uid", uid);
		params.put("popularity", popularity);

		ruserDAO.execDayUserPopularityProc(params);

	}

	@Override
	public void calculatePopularity() {
		List<Long> uids = ruserDAO.findCalculateRankUsers();
		if(uids!=null && uids.size()>0){
			String videoCount = jedisService.getValueFromMap(BicycleConstants.DAY_POPULARITY_WEIGHT, BicycleConstants.DAY_VIDEOCOUNT_WEIGHT);
			Double videoCountRate = 0.3;
			if(StringUtils.isNotBlank(videoCount)){
				videoCountRate = Double.parseDouble(videoCount);
			}
			String fansCount = jedisService.getValueFromMap(BicycleConstants.DAY_POPULARITY_WEIGHT, BicycleConstants.DAY_FANSCOUNT_WEIGHT);
			Double fansCountRate = 0.7;
			if(StringUtils.isNotBlank(fansCount)){
				fansCountRate = Double.parseDouble(fansCount);
			}
			Map<String,Object> params = new HashMap<String,Object>();
			for(Long uid:uids){
				Integer videos = ruserDAO.dayUserVideos(uid);

				Integer fans = ruserDAO.dayUserFans(uid);

				Double popularity = videos*videoCountRate +fans*fansCountRate;
				logger.info("execute day user popularity,uid:{},popularity:{}",uid,popularity);
				params.put("uid", uid);
				params.put("popularity", popularity);

				ruserDAO.execDayUserPopularityProc(params);
			}
		}
	}

	@Override
	public List<Ruser> selectRusers(Map<String,Object> params){
		return ruserDAO.selectRusers(params);
	}

	@Override
	public List<RuserDisplay> queryWopaiRusers(Map<String,Object> params){
		return ruserDAO.selectWopaiRusers(params);
	}

	@Override
	public int selectRusersCount(Map<String,Object> params){
		return ruserDAO.selectRusersCount(params);
	}

	@Override
	public int deleteRuser(Long id){
		return ruserDAO.deleteRuser(id);
	}

	@Override
	public Ruser selectByPrimaryKey(Long id){
		return ruserDAO.selectByPrimaryKey(id);
	}

	@Override
	public int updateRuser(Ruser ruser){
		return ruserDAO.updateRuser(ruser);
	}

	@Override
	public int insertRuser(Ruser ruser){
		return ruserDAO.insertRuser(ruser);
	}

	@Override
	public int batchRankAbleToAllow(String[] ids){
		return ruserDAO.batchRankAbleToAllow(ids);
	}

	@Override
	public int batchRankAbleToBan(String[] ids){
		return ruserDAO.batchRankAbleToBan(ids);
	}

	/**
	 * 根据时间类型type获取用户人气排行榜
	 *
	 * @param type 时间类型 day 24小时 week 周 month 月 year 年
	 * @param uid 用户ID
	 * @param start 开始条数
	 * @param count 总数
	 * @return
	 */
	@Override
	public List<Ruser> queryUserPopularityList(String type, Long uid, Integer start, Integer count) {
		List<Ruser> userRank = null;
		if(uid != null){
			calculateUserPopularity(uid,type);//计算当前用户人气 24小时 周 月 年
		}

		if ("day".equals(type)) {
			userRank = (List<Ruser>) jedisService.getObject(BicycleConstants.TWENTY_FOUR_HOUR_USER_POPULARITY);
		} else if ("week".equals(type)) {
			userRank = (List<Ruser>) jedisService.getObject(BicycleConstants.WEEK_USER_POPULARITY);
		} else if ("month".equals(type)) {
			userRank = (List<Ruser>) jedisService.getObject(BicycleConstants.MONTH_USER_POPULARITY);
		} else if ("year".equals(type)) {
			userRank = (List<Ruser>) jedisService.getObject(BicycleConstants.YEAR_USER_POPULARITY);
		}
		Ruser self = null;
		if (userRank != null && userRank.size() > 0) {
			if (uid != null && uid > 0) {
				self = this.find(uid);
				if (self != null) {
					for (int i = 0; i < userRank.size(); i++) {
						if (self.getId().equals(userRank.get(i).getId())) {
							self.setHotRank(i + 1);
							break;
						}
					}
				}
			}
			if (start == null || start.intValue() >= userRank.size()) {
				start = 0;
			}
			if (count == null || count.intValue() >= userRank.size()) {
				count = userRank.size() - start;
			}
			if ((start + count) <= userRank.size()) {
				userRank = userRank.subList(start, start + count);
			}
			if (self != null) {
				userRank.add(self);
			}
		}

		return userRank;
	}

	/**
	 * 根据时间类型重新计算需要计算的用户人气信息
	 *
	 * @param type 时间类型 day 24小时 week 周 month 月 year 年
	 * @return
	 */
	@Override
	public void reCalculateUserPopularity(String type) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		logger.info("========重新计算用户人气{}开始==========,time{}", type,sdf.format(new Date()));
		Map<String, Object> userParams = new HashMap<String, Object>();
		userParams.put("type", type);
		List<Long> userList = getNeedReCalculateUsers(type);
		if (!userList.isEmpty()) {
			List<Long> uids = ruserDAO.selectNeedReCalculateUsers(userList);//查找需要重新计算人气的用户ID集合 24小时 周 月内有粉丝或者有发布过视频
			if (!uids.isEmpty()) {
				String videoCount = jedisService.getValueFromMap(BicycleConstants.DAY_POPULARITY_WEIGHT, BicycleConstants.DAY_VIDEOCOUNT_WEIGHT);
				Double videoCountRate = 0.3;
				if (StringUtils.isNotBlank(videoCount)) {
					videoCountRate = Double.parseDouble(videoCount);
				}
				String fansCount = jedisService.getValueFromMap(BicycleConstants.DAY_POPULARITY_WEIGHT, BicycleConstants.DAY_FANSCOUNT_WEIGHT);
				Double fansCountRate = 0.7;
				if (StringUtils.isNotBlank(fansCount)) {
					fansCountRate = Double.parseDouble(fansCount);
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("type", type);
				for (Long uid : uids) {
					params.put("uid", uid);
					Integer videos = ruserDAO.selectUserVideosByTime(params);//查找相应时间范围内用户视频数量
					Integer fans = ruserDAO.selectUserFansByTime(params);//查找相应时间范围内用户粉丝数量
					Double popularity = videos * videoCountRate + fans * fansCountRate;//计算人气值
					logger.info("execute {} user popularity,uid:{},popularity:{}", type, uid, popularity);
					params.put("popularity", popularity);
					ruserDAO.updateUserPopularity(params);
				}
				//System.out.println(uids.size());
				//批量更新其他用户人气值为0  取不包含在uids中的uid，24小时 周 月 未增加粉丝或者发布视频的用户 对应得人气字段清空为0
				if ("day".equals(type)) {
					ruserDAO.updateUserDayPopularityZero(uids);
				} else if ("week".equals(type)) {
					ruserDAO.updateUserWeekPopularityZero(uids);
				} else if ("month".equals(type)) {
					ruserDAO.updateUserMonthPopularityZero(uids);
				} else if ("year".equals(type)) {
					ruserDAO.updateUserYearPopularityZero(uids);
				}

			}
		}
		logger.info("========重新计算用户人气{}结束==========,time{}", type,sdf.format(new Date()));
	}

	/**
	 * 查询不同时间范围人气值后写入redis
	 *
	 * @param params type 时间类型 day 24小时 week 周 month 月 year 年
	 * @param params count
	 * @return
	 */
	@Override
	public void queryUserPopularityToRedis(Map<String, Object> params) {
		List<Ruser> rusers = ruserDAO.selectUserPopularityByTime(params);
		if (rusers != null && rusers.size() > 0) {
			if ("day".equals(params.get("type"))) {
				jedisService.setObject(BicycleConstants.TWENTY_FOUR_HOUR_USER_POPULARITY, rusers);
				jedisService.set(BicycleConstants.TWENTY_FOUR_HOUR_USER_POPULARITY_UPDATE_TIME, (new Date().getTime()) + "");
				logger.info("======24小时用户人气排行，前50======size:" + rusers.size());
			} else if ("week".equals(params.get("type"))) {
				jedisService.setObject(BicycleConstants.WEEK_USER_POPULARITY, rusers);
				jedisService.set(BicycleConstants.WEEK_USER_POPULARITY_UPDATE_TIME, (new Date().getTime()) + "");
				logger.info("======周用户人气排行，前50======size:" + rusers.size());
			} else if ("month".equals(params.get("type"))) {
				jedisService.setObject(BicycleConstants.MONTH_USER_POPULARITY, rusers);
				jedisService.set(BicycleConstants.MONTH_USER_POPULARITY_UPDATE_TIME, (new Date().getTime()) + "");
				logger.info("======月用户人气排行，前50======size:" + rusers.size());
			} else if ("year".equals(params.get("type"))) {
				jedisService.setObject(BicycleConstants.YEAR_USER_POPULARITY, rusers);
				jedisService.set(BicycleConstants.YEAR_USER_POPULARITY_UPDATE_TIME, (new Date().getTime()) + "");
				logger.info("======年用户人气排行，前50======size:" + rusers.size());
			}
		}
	}

	private List<Long> getNeedReCalculateUsers(String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		List<Long> attentionUser = ruserDAO.selectUserIdByAttentionTime(params);
		List<Long> videoUser = ruserDAO.selectUserIdByVideoTime(params);
		attentionUser.addAll(videoUser);
		return attentionUser;
	}

	//计算当前用户人气
	private void calculateUserPopularity(Long userId, String type) {
		String videoCount = jedisService.getValueFromMap(BicycleConstants.DAY_POPULARITY_WEIGHT, BicycleConstants.DAY_VIDEOCOUNT_WEIGHT);
		Double videoCountRate = 0.3;
		if (StringUtils.isNotBlank(videoCount)) {
			videoCountRate = Double.parseDouble(videoCount);
		}
		String fansCount = jedisService.getValueFromMap(BicycleConstants.DAY_POPULARITY_WEIGHT, BicycleConstants.DAY_FANSCOUNT_WEIGHT);
		Double fansCountRate = 0.7;
		if (StringUtils.isNotBlank(fansCount)) {
			fansCountRate = Double.parseDouble(fansCount);
		}

		//重新写入redis
		if (type.equals("day")) {
			Map<String, Object> dayParams = new HashMap<String, Object>();
			dayParams.put("type", "day");
			dayParams.put("uid", userId);
			Integer dayVideos = ruserDAO.selectUserVideosByTime(dayParams);//查找相应时间范围内用户视频数量
			Integer dayFans = ruserDAO.selectUserFansByTime(dayParams);//查找相应时间范围内用户粉丝数量
			Double dayPopularity = dayVideos * videoCountRate + dayFans * fansCountRate;//计算人气值
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("twentyFourHourPopularity", dayPopularity);
			params.put("id", userId);
			ruserDAO.updateRuserPopularity(params);
			Map<String, Object> upParams = new HashMap<String, Object>();
			upParams.put("type", type);
			upParams.put("count", 50);
			List<Ruser> rusers = ruserDAO.selectUserPopularityByTime(upParams);
			jedisService.setObject(BicycleConstants.TWENTY_FOUR_HOUR_USER_POPULARITY, rusers);
		} else if (type.equals("week")) {
			Map<String, Object> weekParams = new HashMap<String, Object>();
			weekParams.put("type", "week");
			weekParams.put("uid", userId);
			Integer weekVideos = ruserDAO.selectUserVideosByTime(weekParams);//查找相应时间范围内用户视频数量
			Integer weekFans = ruserDAO.selectUserFansByTime(weekParams);//查找相应时间范围内用户粉丝数量
			Double weekPopularity = weekVideos * videoCountRate + weekFans * fansCountRate;//计算人气值
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("weekPopularity", weekPopularity);
			params.put("id", userId);
			ruserDAO.updateRuserPopularity(params);
			Map<String, Object> upParams = new HashMap<String, Object>();
			upParams.put("type", type);
			upParams.put("count", 50);
			List<Ruser> rusers = ruserDAO.selectUserPopularityByTime(upParams);
			jedisService.setObject(BicycleConstants.WEEK_USER_POPULARITY, rusers);
		} else if (type.equals("month")) {
			Map<String, Object> monthParams = new HashMap<String, Object>();
			monthParams.put("type", "month");
			monthParams.put("uid", userId);
			Integer monthVideos = ruserDAO.selectUserVideosByTime(monthParams);//查找相应时间范围内用户视频数量
			Integer monthFans = ruserDAO.selectUserFansByTime(monthParams);//查找相应时间范围内用户粉丝数量
			Double monthPopularity = monthVideos * videoCountRate + monthFans * fansCountRate;//计算人气值
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("monthPopularity", monthPopularity);
			params.put("id", userId);
			ruserDAO.updateRuserPopularity(params);

			Map<String, Object> upParams = new HashMap<String, Object>();
			upParams.put("type", type);
			upParams.put("count", 50);
			List<Ruser> rusers = ruserDAO.selectUserPopularityByTime(upParams);
			jedisService.setObject(BicycleConstants.MONTH_USER_POPULARITY, rusers);
		} else if (type.equals("year")) {
			Map<String, Object> yearParams = new HashMap<String, Object>();
			yearParams.put("type", "year");
			yearParams.put("uid", userId);
			Integer yearVideos = ruserDAO.selectUserVideosByTime(yearParams);//查找相应时间范围内用户视频数量
			Integer yearFans = ruserDAO.selectUserFansByTime(yearParams);//查找相应时间范围内用户粉丝数量
			Double yearPopularity = yearVideos * videoCountRate + yearFans * fansCountRate;//计算人气值
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("yearPopularity", yearPopularity);
			params.put("id", userId);
			ruserDAO.updateRuserPopularity(params);
			Map<String, Object> upParams = new HashMap<String, Object>();
			upParams.put("type", type);
			upParams.put("count", 50);
			List<Ruser> rusers = ruserDAO.selectUserPopularityByTime(upParams);
			jedisService.setObject(BicycleConstants.YEAR_USER_POPULARITY, rusers);
		}

	}

	@Override
	public void updateLoginDate(String username) {
		ruserRepository.updateLoginDate(username);
	}
	
	@Override
	public void insertWechatUnionid(String username,String unionid) {
		ruserRepository.insertWechatUnionid(username,unionid);
	}

	@Override
	public List<ExportWopaiUser> queryWopaiUsers(Map<String,Object> params){
		return ruserDAO.selectWopaiUsers(params);
	}

	@Override
	public void allowEvaluation(int isAllow, Long uid) {
		ruserRepository.allowEvaluation(isAllow, uid);
	}

	@Override
	public Long queryRealFansCountById(Long id){
		return ruserDAO.selectRealFansCountById(id);
	}

	@Override
	public List<String> selectAllRegPlatform() {
		List<String> allRegPlatform = ruserRepository.selectAllRegPlatform();
		return allRegPlatform;
	}

	@Override
	public void changeInvalidatePic(Long uid, String type) {
		Ruser ruser = find(uid);
		if(ruser != null) {
			SystemMess mess = new SystemMess();

			if ("home".equals(type)) {
				ruser.setHomePic(jedisService.get(BicycleConstants.DEFAULT_HOME_PIC) + "?timestamp=" + System.currentTimeMillis());
				mess.setContent("您好，您设置的背景图违反了LIVE相关规定,请重新拍摄上传。");
				mess.setTitle("您设置的背景图审核未通过");
			} else if ("head".equals(type)) {
				if (jedisService.get(BicycleConstants.DEFAULT_HEAD_PIC) != null && !jedisService.get(BicycleConstants.DEFAULT_HEAD_PIC).equals("")) {
					ruser.setPic(jedisService.get(BicycleConstants.DEFAULT_HEAD_PIC) + "?timestamp=" + System.currentTimeMillis());
				}
				mess.setContent("您好，您设置的头像违反了LIVE相关规定,请重新拍摄上传。");
				mess.setTitle("您设置的头像审核未通过");
			}
			update(ruser);
			jedisService.saveAsMap(BicycleConstants.USER_INFO + ruser.getId(), ruser);
			if (jedisService.get(BicycleConstants.OFFICICAL_USER_ID) != null && !jedisService.get(BicycleConstants.OFFICICAL_USER_ID).equals("")) {
				mess.setCreatorId(Long.parseLong(jedisService.get(BicycleConstants.OFFICICAL_USER_ID)));
			}
			mess.setPlatform("all");
			mess.setDestUser(String.valueOf(uid));
			mess.setPublishTime(new Date());
			mess.setIsplan("0");
			mess.setCreateDate(new Date());
			mess.setOperation("picCheck");
			mess.setStat("0");
			mess.setTargetid(0l);
			mess.setImagePath("");
			systemMessService.saveSysmess(mess);
			systemMessService.sendMessage(mess);
			jedisService.deleteSortedSetItemFromShard(BicycleConstants.TO_BE_VERIFIED_PIC, uid + "|" + type);
		}
	}

	@Override
	public void verifyPic(Long uid, String type) {
		jedisService.deleteSortedSetItemFromShard(BicycleConstants.TO_BE_VERIFIED_PIC, uid+"|"+type);
	}

	/**
	 * 封号
	 * @param userId 用户ID
	 **/
	@Override
	public void closeAccount(Long userId){
		Ruser user = this.find(userId);
		user.setStat(2);
		user.setIsBlacklist(0);
		user.setModifyDate(new Date());
		//更新用户操作状态
		this.update(user);
		jedisService.saveAsMap(BicycleConstants.USER_INFO + user.getId(), user);
		//删除solr中用户信息
		try {
			solrUserService.deleteDocByid(userId);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String key = "FBU_"+userId.toString();
		jedisService.set(key, "2");
		//账户被禁用，删除redis中的登陆信息
		String userKey = "oltu_" + user.getUsername() + "_token";
		if (org.apache.commons.lang.StringUtils.isNotBlank(userKey)) {
			String access_token = jedisService.get(userKey);
			if (org.apache.commons.lang.StringUtils.isNotBlank(access_token)) {
				jedisService.delete(access_token);
			}
			jedisService.delete(userKey);
		}
	}

	@Override
	public List<ExportWopaiNormalUser> queryWopaiUserToExcel(Map<String, Object> params) {
		return ruserDAO.selectWopaiUserToExcel(params);
	}

	@Override
	public void updateBerryCount(Long uid) {
		ruserRepository.updateBerryCount(uid);
	}

	@Override
	public void updateDiamond(Long uid, Integer count) {
		ruserRepository.updateDiamond(uid, count);
		
	}

	@Override
	public void offlineLiveRoom(Long uid,Long adminId) {
		Room r = roomService.queryLivingRoomByUserId(uid);
		if(r != null){
			roomService.offlineRoom(r.getId(),adminId, "您已被管理员封号");
		}
	}

	@Override
	public int reduceDiamond(Long uid, Integer count) {
		return ruserRepository.reduceDiamond(uid, count);
	}
	
	public Long findCountByUserIdIfaNotNull(Long userId) {
		return ruserRepository.findCountByUserIdIfaNotNull(userId);
	}
	
}
