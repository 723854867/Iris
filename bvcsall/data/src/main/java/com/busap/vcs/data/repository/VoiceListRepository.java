package com.busap.vcs.data.repository;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.VoiceList;


@Resource(name = "voiceListRepository")
public interface VoiceListRepository extends BaseRepository<VoiceList, Long> {

	//获得榜单配置
	@Query(nativeQuery=true,value="select * from voice_list where start_time<=?1 and type=?2 and state=1 order by start_time desc limit 1")
	public VoiceList getRankConfig(Date currentTime,Integer type);
	
}
