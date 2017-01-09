package com.busap.vcs.service;

import com.busap.vcs.data.entity.Spread;

public interface SpreadService extends BaseService<Spread, Long> {
 
	public int uidExist(Long uid);
}
