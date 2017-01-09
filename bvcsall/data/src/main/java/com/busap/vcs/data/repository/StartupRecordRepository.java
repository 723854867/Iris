package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.ActivationRecord;

/**
 * Created by zx 
 */
@Resource(name = "startupRecordRepository")
public interface StartupRecordRepository extends BaseRepository<ActivationRecord, Long> {
	
	@Query("select count(sr.id) from StartupRecord sr  where  sr.ifa =?1")
	public Long findCountByIfa(String ifa);
	
}
