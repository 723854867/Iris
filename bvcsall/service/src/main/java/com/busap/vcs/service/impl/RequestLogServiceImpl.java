package com.busap.vcs.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.RequestLog;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.RequestLogRepository;
import com.busap.vcs.service.RequestLogService;

@Service("requestLogService")
public class RequestLogServiceImpl extends BaseServiceImpl<RequestLog, Long> implements
RequestLogService {
	
	private Logger logger = LoggerFactory.getLogger(RequestLogServiceImpl.class);
	
	@Resource(name="requestLogRepository")
	private RequestLogRepository requestLogRepository;
	
	
	@Resource(name="bstarRepository")
	@Override
	public void setBaseRepository(BaseRepository<RequestLog, Long> baseRepository) {
		super.setBaseRepository(requestLogRepository);
	}

}
