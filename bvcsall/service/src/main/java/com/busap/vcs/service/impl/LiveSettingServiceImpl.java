package com.busap.vcs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.LiveSetting;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.mapper.LiveSettingDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.LievSettingRepository;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LiveSettingService;
import com.busap.vcs.service.RuserService;

@Service("liveSettingService")
public class LiveSettingServiceImpl extends BaseServiceImpl<LiveSetting, Long> implements LiveSettingService {

	@Resource(name="lievSettingRepository")
	private LievSettingRepository liveSettingRepository;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name="ruserService")
	private RuserService ruserService;
	
	@Autowired
	private LiveSettingDAO liveSettingDAO;
	
	@Resource(name="lievSettingRepository")
	@Override
	public void setBaseRepository(BaseRepository<LiveSetting, Long> baseRepository) {
		// TODO Auto-generated method stub
		super.setBaseRepository(baseRepository);
	}
	
	@Transactional
	@Override
	public void save(LiveSetting entity) {
		super.save(entity);
		if(entity.getId()!=null){
			jedisService.saveAsMap(BicycleConstants.LIVE_SETTING+entity.getId(), entity);
		}
	}

	@Transactional
	@Override
	public LiveSetting update(LiveSetting entity) {
		Long id = entity.getId();
		LiveSetting ls = this.find(id);
		if(ls!=null){
			if(entity.getStatus()!=ls.getStatus()){
				Map<String,Object> params = new HashMap<String,Object>();
		    	params.put("pageStart", 0);
		    	params.put("pageSize", 10000);
		    	params.put("settingId", id);
		    	
				if(entity.getStatus().intValue()==0){//改为生效
					List<Map<String, Object>> userList = liveSettingDAO.searchSettingUser(params);
			    	if(ls != null){
			    		for(Map<String,Object> map:userList){
			    			Long uid = (Long)map.get("id");
			    			if(uid!=null){
			    				jedisService.setValueToMap(BicycleConstants.USER_INFO+uid, "liveSetting", id.toString());
			    			}    			
			    		}
			    	}
				}else if(entity.getStatus().intValue()==1){
					List<Map<String, Object>> userList = liveSettingDAO.searchSettingUser(params);
			    	if(ls != null){
			    		for(Map<String,Object> map:userList){
			    			Long uid = (Long)map.get("id");
			    			if(uid!=null){
			    				jedisService.setValueToMap(BicycleConstants.USER_INFO+uid, "liveSetting", "-1");
			    			}    			
			    		}
			    	}
				}
			}
			ls.setMajiaCount(entity.getMajiaCount());
			ls.setMajiaPeriod(entity.getMajiaPeriod());
			ls.setMaxMajiaPeriod(entity.getMaxMajiaPeriod());
			ls.setTypeId(entity.getTypeId());
			ls.setName(entity.getName());
			ls.setStatus(entity.getStatus());
			
			entity = super.update(entity);
			jedisService.saveAsMap(BicycleConstants.LIVE_SETTING+entity.getId(), entity);
		} 
		
		return entity;
	}

	@Transactional
	@Override
	public void delete(Long id) {
		super.delete(id);
		jedisService.delete(BicycleConstants.LIVE_SETTING+id);
		Map<String,Object> params = new HashMap<String,Object>();
    	params.put("pageStart", 0);
    	params.put("pageSize", 10000);
    	params.put("settingId", id);
    	
    	List<Map<String, Object>> ls = liveSettingDAO.searchSettingUser(params);
    	if(ls != null){
    		for(Map<String,Object> map:ls){
    			Long uid = (Long)map.get("id");
    			if(uid!=null){
    				jedisService.setValueToMap(BicycleConstants.USER_INFO+uid, "liveSetting", "-1");
    			}    			
    		}
    	}
	}

	@Override
	public Page<LiveSetting> searchLiveSetting(Integer pageNo, Integer pageSize,Map<String, Object> params) {
		List<LiveSetting> ls = liveSettingDAO.searchLiveSetting(params);
		Integer count = liveSettingDAO.searchCount(params);
		
		Page<LiveSetting> pinfo = new PageImpl<LiveSetting>(ls,new PageRequest(pageNo-1, pageSize, null),count);
		return pinfo;
	}
	
	@Transactional
	@Override
	public void addUser(Long settingId, Long userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		Ruser user = ruserService.find(userId);
		if(user != null){
			params.put("settingId", settingId);
			params.put("userId", userId);
			liveSettingDAO.addUser(params);
			
			user.setLiveSetting(settingId);
			ruserService.update(user);
			jedisService.saveAsMap(BicycleConstants.USER_INFO+userId, user);
		}
	}

	@Override
	public Page<Map<String, Object>> searchSettingUser(Integer pageNo,Integer pageSize, Map<String, Object> params) {
		List<Map<String, Object>> ls = liveSettingDAO.searchSettingUser(params);
		Integer count = liveSettingDAO.searchSettingUserCount(params);
		
		Page<Map<String, Object>> pinfo = new PageImpl<Map<String,Object>>(ls,new PageRequest(pageNo-1, pageSize, null),count);
		return pinfo;
	}

	@Override
	public void removeUser(Long settingId, List<Long> userIds) {
		
		for(Long userId:userIds){
			Ruser user = ruserService.find(userId);
			if(user != null){
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("settingId", settingId);
				params.put("userId", userId);
				liveSettingDAO.removeUser(params);
				if(user.getLiveSetting().longValue()==settingId.longValue()){
					user.setLiveSetting(-1L);
					ruserService.update(user);
					jedisService.saveAsMap(BicycleConstants.USER_INFO+userId, user);
				}
			}
		}
	}

	
	
}
