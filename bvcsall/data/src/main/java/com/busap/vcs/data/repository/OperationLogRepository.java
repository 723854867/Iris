package com.busap.vcs.data.repository;

import java.util.List;

import com.busap.vcs.data.entity.Favorite;
import com.busap.vcs.data.entity.OperationLog;

import javax.annotation.Resource;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "operationLogRepository")
public interface OperationLogRepository extends BaseRepository<OperationLog, Long> {
	
}
