package com.busap.vcs.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.mapper.DeviceInfoDAO;
import com.busap.vcs.service.DeviceInfoService;

@Service("deviceInfoService")
public class DeviceInfoServiceImpl implements DeviceInfoService {
	
	@Autowired
	DeviceInfoDAO deviceInfoDao;

	@Override
	public int insert(String uuid, String uniqueMark, String uid,
			String platform, String osVersion, String operator, String model,
			String factory, String imei, String mac, String iccid, String meid,
			String pseudoUniqeId, String androidId, String wlanMac,
			String btMac, String cpu, String memory, String screenResolution,
			String pixel) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uuid", uuid==null?"":uuid);
		params.put("uniqueMark", uniqueMark==null?"":uniqueMark);
		params.put("uid",uid);
		params.put("platform", platform==null?"":platform);
		params.put("osVersion", osVersion==null?"":osVersion);
		params.put("operator", operator==null?"":operator);
		params.put("model", model==null?"":model);
		params.put("factory", factory==null?"":factory);
		params.put("imei", imei==null?"":imei);
		params.put("mac", mac==null?"":mac);
		params.put("iccid", iccid==null?"":iccid);
		params.put("meid", meid==null?"":meid);
		params.put("pseudoUniqeId", pseudoUniqeId==null?"":pseudoUniqeId);
		params.put("androidId", androidId==null?"":androidId);
		params.put("wlanMac", wlanMac==null?"":wlanMac);
		params.put("btMac", btMac==null?"":btMac);
		params.put("cpu", cpu==null?"":cpu);
		params.put("memory", memory==null?"":memory);
		params.put("screenResolution", screenResolution==null?"":screenResolution);
		params.put("pixel", pixel==null?"":pixel);
	
		return deviceInfoDao.insert(params);
	}

	@Override
	public String selectUuidByUniqueMark(String uniqueMark) {
		return deviceInfoDao.selectUuidByUniqueMark(uniqueMark);
	}

	@Override
	public int bindUidAndUuid(Long uid, String uuid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("uuid", uuid);
		return deviceInfoDao.bindUidAndUuid(params);
	}

}
