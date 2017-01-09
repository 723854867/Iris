package com.busap.vcs.service;

public interface DeviceInfoService {

	public int insert(String uuid, String uniqueMark, String uid,
			String platform, String osVersion, String operator, String model,
			String factory, String imei, String mac, String iccid, String meid,
			String pseudoUniqeId, String androidId, String wlanMac,
			String btMac, String cpu, String memory, String screenResolution,
			String pixel);

	public String selectUuidByUniqueMark(String uniqueMark);

	public int bindUidAndUuid(Long uid, String uuid);
}
