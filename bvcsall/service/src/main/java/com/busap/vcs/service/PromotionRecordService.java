package com.busap.vcs.service;

import java.util.Date;
import java.util.List;

import com.busap.vcs.data.entity.PromotionRecord;

/**
 * Created by
 * User: zx
 */
public interface PromotionRecordService extends BaseService<PromotionRecord, Long> {


	public Long findCountByMacOrIfa(String mac,String ifa);
	
	public Long findCountByMacOrIfaAndValidTime(String mac,String ifa,Date validTime);
	
	public Long findCountByMacOrIfaAndValidTime(String mac,String ifa,Date validTime,String fromType);
	
	public List findByMacOrIfa(String mac,String ifa);
	
	public List findByMacOrIfaAndFromType(String mac,String ifa,String fromType);
	
}
