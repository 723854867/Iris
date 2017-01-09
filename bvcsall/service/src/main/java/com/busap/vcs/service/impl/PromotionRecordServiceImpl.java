package com.busap.vcs.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.PromotionRecord;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.PromotionRecordRepository;
import com.busap.vcs.service.PromotionRecordService;

@Service("promotionRecordService")
public class PromotionRecordServiceImpl extends BaseServiceImpl<PromotionRecord, Long> implements PromotionRecordService {
	
    @Resource(name = "promotionRecordRepository")
    private PromotionRecordRepository promotionRecordRepository;
     
    
    @Resource(name = "promotionRecordRepository")
    @Override
    public void setBaseRepository(BaseRepository<PromotionRecord, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }  
    
	public Long findCountByMacOrIfa(String mac,String ifa) {
		return promotionRecordRepository.findCountByMacOrIfa(mac, ifa);
	};
	
	public Long findCountByMacOrIfaAndValidTime(String mac,String ifa,Date validTime) {
		return promotionRecordRepository.findCountByMacOrIfaAndValidTime(mac, ifa, validTime);
	};
	
	public Long findCountByMacOrIfaAndValidTime(String mac,String ifa,Date validTime,String fromType){
		return promotionRecordRepository.findCountByMacOrIfaAndValidTime(mac, ifa, validTime,fromType);
	}

	@Override
	public List findByMacOrIfa(String mac, String ifa) {
		return promotionRecordRepository.findByMacOrIfa(mac, ifa);
	}

	@Override
	public List findByMacOrIfaAndFromType(String mac, String ifa,String fromType) {
		return promotionRecordRepository.findByMacOrIfaAndFromType(mac, ifa, fromType);
	};
	
    

}
