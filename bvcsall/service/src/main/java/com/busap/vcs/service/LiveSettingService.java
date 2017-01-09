package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.LiveSetting;

public interface LiveSettingService extends BaseService<LiveSetting, Long> {

	public Page<LiveSetting> searchLiveSetting(Integer pageNo, Integer pageSize,	Map<String, Object> params);
	
	public void addUser(Long settingId,Long userId);
	
	public void removeUser(Long settingId,List<Long> userId);
	
	public Page<Map<String,Object>> searchSettingUser(Integer pageNo,Integer pageSize,Map<String,Object> params);
}
