package com.busap.vcs.data.repository;


import javax.annotation.Resource;

import com.busap.vcs.data.entity.RequestLog;


@Resource(name = "requestLogRepository")
public interface RequestLogRepository extends BaseRepository<RequestLog, Long> {
	

}
