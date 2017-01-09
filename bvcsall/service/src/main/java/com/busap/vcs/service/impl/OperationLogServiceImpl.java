package com.busap.vcs.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.mapper.OperationLogDao;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.OperationLog;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.OperationLogRepository;
import com.busap.vcs.service.OperationLogService;

@Service("operationLogService")
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLog, Long> implements
OperationLogService { 
	@Resource(name = "operationLogRepository")
	private OperationLogRepository operationLogRepository; 

	@Resource
	private OperationLogDao operationLogDao;

	@Resource(name = "operationLogRepository")
	@Override
	public void setBaseRepository(BaseRepository<OperationLog, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	}

	@Override
	public List<OperationLog> queryOperationLogs(Map<String,Object> params){
		return operationLogDao.select(params);
	}

	@Override
	public int insertOperationLog(OperationLog operationLog){
		return operationLogDao.insert(operationLog);
	}


	/*@Override
	public Integer queryOperationLogCount(Map<String,Object> params){
		return operationLogDao.selectCount(params);
	}*/
	
}
