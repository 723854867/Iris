package com.busap.vcs.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Spread;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.SpreadRepository;
import com.busap.vcs.service.SpreadService;

@Service("spreadService")
public class SpreadServiceImpl extends BaseServiceImpl<Spread, Long> implements SpreadService {
	
    @Resource(name = "spreadRepository")
    private SpreadRepository spreadRepository;
    
     
    @Resource(name = "spreadRepository")
    @Override
    public void setBaseRepository(BaseRepository<Spread, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }


	@Override
	public int uidExist(Long uid) {
		return spreadRepository.uidExist(uid);
	}
    

}
