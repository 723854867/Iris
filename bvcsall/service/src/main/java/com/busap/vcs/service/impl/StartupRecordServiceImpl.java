package com.busap.vcs.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.StartupRecord;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.StartupRecordRepository;
import com.busap.vcs.service.StartupRecordService;

@Service("startupRecordService")
public class StartupRecordServiceImpl extends BaseServiceImpl<StartupRecord, Long> implements StartupRecordService {
	
    @Resource(name = "startupRecordRepository")
    private StartupRecordRepository startupRecordRepository;
     
    
    @Resource(name = "startupRecordRepository")
    @Override
    public void setBaseRepository(BaseRepository<StartupRecord, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }
    
    public Long findCountByIfa(String ifa) {
    	return startupRecordRepository.findCountByIfa(ifa);
    }


}
