package com.busap.vcs.data.repository;

import javax.annotation.Resource;
import org.springframework.data.jpa.repository.Query;
import com.busap.vcs.data.entity.ActivationRecord;

/**
 * Created by zx 
 */
@Resource(name = "activationRecordRepository")
public interface ActivationRecordRepository extends BaseRepository<ActivationRecord, Long> {
	
	@Query("select count(ar.id) from ActivationRecord ar  where ((ar.mac is not null) and (ar.mac!='') and ar.mac = ?1) or ((ar.ifa is not null) and  (ar.ifa!='') and ar.ifa =?2)")
	public Long findCountByMacOrIfa(String mac,String ifa);
	
	@Query("select count(ar.id) from ActivationRecord ar  where  ar.ifa =?1")
	public Long findCountByIfa(String ifa);
	
}
