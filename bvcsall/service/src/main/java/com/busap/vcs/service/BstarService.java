package com.busap.vcs.service;

import com.busap.vcs.data.entity.Bstar;

public interface BstarService extends BaseService<Bstar, Long> {
	
	public boolean isUidExist (Long uid);
	
}
