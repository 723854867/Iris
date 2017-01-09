package com.busap.vcs.service;

import com.busap.vcs.data.entity.LiveCheckLog;


public interface LiveCheckLogService extends BaseService<LiveCheckLog, Long> {
   
	public void check(Long operatorId,String operate,String reason,Long roomId,Long userId,String type);
}