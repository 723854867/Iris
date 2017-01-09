package com.busap.vcs.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.ActivationRecord;
import com.busap.vcs.data.repository.ActivationRecordRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.service.ActivationRecordService;

@Service("activationRecordService")
public class ActivationRecordServiceImpl extends BaseServiceImpl<ActivationRecord, Long> implements ActivationRecordService {
	
    @Resource(name = "activationRecordRepository")
    private ActivationRecordRepository activationRecordRepository;
     
    
    @Resource(name = "activationRecordRepository")
    @Override
    public void setBaseRepository(BaseRepository<ActivationRecord, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }


	@Override
	public Long findCountByMacOrIfa(String mac, String ifa) {
		return activationRecordRepository.findCountByMacOrIfa(mac, ifa);
	}


	@Override
	public Long findCountByIfa(String ifa) {
		return activationRecordRepository.findCountByIfa(ifa);
	}  
    

}
