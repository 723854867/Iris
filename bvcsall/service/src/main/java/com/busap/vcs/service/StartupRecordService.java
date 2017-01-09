package com.busap.vcs.service;

import com.busap.vcs.data.entity.StartupRecord;

/**
 * Created by
 * User: zx
 */
public interface StartupRecordService extends BaseService<StartupRecord, Long> {
	

	public Long findCountByIfa(String ifa);

	
}
