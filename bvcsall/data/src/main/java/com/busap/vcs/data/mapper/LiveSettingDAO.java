package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.LiveSetting;

public interface LiveSettingDAO {
	/**
	 * 按条件查询
	 * @param params
	 * @return
	 */
	public List<LiveSetting> searchLiveSetting(Map<String,Object> params); 
	/**
	 * 按条件查询总条数
	 * @param params
	 * @return
	 */
	public Integer searchCount(Map<String,Object> params);
	/**
	 * 添加
	 * @param params
	 * @return
	 */
	public Integer addUser(Map<String,Object> params);
	
	/**
	 * 删除
	 * @param params
	 * @return
	 */
	public Integer removeUser(Map<String,Object> params);
	
	/**
	 * 查询用户列表
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> searchSettingUser(Map<String,Object> params);
	
	public Integer searchSettingUserCount(Map<String,Object> params);
}
