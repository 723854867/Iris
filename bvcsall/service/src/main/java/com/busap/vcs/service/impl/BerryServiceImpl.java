package com.busap.vcs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Berry;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.BerryRepository;
import com.busap.vcs.service.BerryService;

@Service("berryService")
public class BerryServiceImpl extends BaseServiceImpl<Berry, Long> implements
BerryService {
	
	private Logger logger = LoggerFactory.getLogger(BerryServiceImpl.class);
	
	@Resource(name="berryRepository")
	private BerryRepository berryRepository;
	
	
	@Resource(name="berryRepository")
	@Override
	public void setBaseRepository(BaseRepository<Berry, Long> baseRepository) {
		super.setBaseRepository(berryRepository);
	}


	@Override
	public List<Berry> getPlantBerryList(Long userId, Integer page, Integer size) {
		return berryRepository.getPlantBerryList(userId, (page-1)*size, size);
	}


}
