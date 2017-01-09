package com.busap.vcs.service;

import com.busap.vcs.data.entity.ActivationRecord;

/**
 * Created by
 * User: zx
 */
public interface ActivationRecordService extends BaseService<ActivationRecord, Long> {
	
	public Long findCountByMacOrIfa(String mac,String ifa);
	
	public Long findCountByIfa(String ifa);


	
}
