package com.busap.vcs.data.repository;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.PromotionRecord;

/**
 * Created by zx.
 */
@Resource(name = "promotionRecordRepository")
public interface PromotionRecordRepository extends BaseRepository<PromotionRecord, Long> {
	
	
	@Query("select count(pr.id) from PromotionRecord pr  where ((pr.mac is not null) and (pr.mac!='') and pr.mac = ?1) or ((pr.ifa is not null) and  (pr.ifa!='') and pr.ifa =?2)")
	public Long findCountByMacOrIfa(String mac,String ifa);
	
	@Query("select count(pr.id) from PromotionRecord pr  where (((pr.mac is not null) and (pr.mac!='') and pr.mac = ?1) or ((pr.ifa is not null) and  (pr.ifa!='') and pr.ifa =?2)) and pr.createDate>=?3")
	public Long findCountByMacOrIfaAndValidTime(String mac,String ifa,Date validTime);
	
	@Query("select count(pr.id) from PromotionRecord pr  where pr.fromType=?4 and (((pr.mac is not null) and (pr.mac!='') and pr.mac = ?1) or ((pr.ifa is not null) and  (pr.ifa!='') and pr.ifa =?2)) and pr.createDate>=?3")
	public Long findCountByMacOrIfaAndValidTime(String mac,String ifa,Date validTime,String fromType);
	
	@Query(" from PromotionRecord pr  where (((pr.mac is not null) and (pr.mac!='') and pr.mac = ?1) or ((pr.ifa is not null) and  (pr.ifa!='') and pr.ifa =?2))  order by pr.createDate desc")
	public List findByMacOrIfa(String mac,String ifa);
	
	@Query(" from PromotionRecord pr  where (((pr.mac is not null) and (pr.mac!='') and pr.mac = ?1) or ((pr.ifa is not null) and  (pr.ifa!='') and pr.ifa =?2)) and pr.fromType=?3  order by pr.createDate desc")
	public List findByMacOrIfaAndFromType(String mac,String ifa,String fromType);
	
	
}
