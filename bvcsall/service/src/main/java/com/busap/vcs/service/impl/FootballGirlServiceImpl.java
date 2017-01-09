package com.busap.vcs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.FootballGirl;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.FootballGirlRepository;
import com.busap.vcs.service.FootballGirlService;

@Service("footballGirlService")
public class FootballGirlServiceImpl extends BaseServiceImpl<FootballGirl, Long> implements
FootballGirlService {
	
	private Logger logger = LoggerFactory.getLogger(FootballGirlServiceImpl.class);
	
	@Resource(name="footballGirlRepository")
	private FootballGirlRepository footballGirlRepository;
	
	
	@Resource(name="footballGirlRepository")
	@Override
	public void setBaseRepository(BaseRepository<FootballGirl, Long> baseRepository) {
		super.setBaseRepository(footballGirlRepository);
	}


	@Override
	public boolean isPhoneExist(String phone) {
		int count = footballGirlRepository.getCountByPhone(phone);
		return count > 0?true:false;
	}


	@Override
	public boolean isUidExist(Long uid) {
		int count = footballGirlRepository.getCountByUid(uid);
		return count > 0?true:false;
	}


	@Override
	public List<Long> findUids() {
		return footballGirlRepository.findUids();
	}

}
