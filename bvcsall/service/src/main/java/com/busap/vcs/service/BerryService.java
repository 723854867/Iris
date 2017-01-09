package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.Berry;

public interface BerryService extends BaseService<Berry, Long> {
	
	public List<Berry> getPlantBerryList(Long userId,Integer page,Integer count); 
	
}
