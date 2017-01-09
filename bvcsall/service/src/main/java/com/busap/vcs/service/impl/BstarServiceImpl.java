package com.busap.vcs.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Bstar;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.BstarRepository;
import com.busap.vcs.service.BstarService;

@Service("bstarService")
public class BstarServiceImpl extends BaseServiceImpl<Bstar, Long> implements
BstarService {
	
	private Logger logger = LoggerFactory.getLogger(BstarServiceImpl.class);
	
	@Resource(name="bstarRepository")
	private BstarRepository bstarRepository;
	
	
	@Resource(name="bstarRepository")
	@Override
	public void setBaseRepository(BaseRepository<Bstar, Long> baseRepository) {
		super.setBaseRepository(bstarRepository);
	}


	@Override
	public boolean isUidExist(Long uid) {
		int count = bstarRepository.getCountByUid(uid);
		return count > 0?true:false;
	}
}
