package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.FootballGirl;

public interface FootballGirlService extends BaseService<FootballGirl, Long> {
	
	public boolean isPhoneExist (String phone);
	
	public boolean isUidExist (Long uid);
	
	public List<Long> findUids();
	
}
