package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

public interface SingerMapper {
	/**
	 * 按条件查询投诉信息
	 * @param params
	 * @return
	 */
	public List<Map> searchSinger(Map<String,Object> params); 
	
	public List<Map> searchRicher(Map<String,Object> params); 
	
}
