package com.busap.vcs.data.mapper;

import java.util.Map;

/**
 * @author Administrator
 *
 */
public interface DeviceInfoDAO {

	/**
	 * 添加设备信息
	 * @param params
	 * @return
	 */
	public int insert(Map<String,Object> params);
	
	/**
	 * 根据设备唯一标识，查询我拍唯一标识
	 * @param params
	 * @return
	 */
	public String selectUuidByUniqueMark(String uniqueMark);
	
	/**
	 * 将用户id和我拍唯一标识绑定
	 * @param uid
	 * @param uuid
	 */
	public int bindUidAndUuid(Map<String,Object> params);
}
