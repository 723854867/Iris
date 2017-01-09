package com.busap.vcs.data.repository;


import javax.annotation.Resource;

import com.busap.vcs.data.entity.LiveCheckLog;

@Resource(name = "liveCheckLogRepository")
public interface LiveCheckLogRepository extends BaseRepository<LiveCheckLog, Long> {
	
}
