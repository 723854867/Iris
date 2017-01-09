package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.OperationLog;
import com.busap.vcs.data.entity.Tag;

/**
 * Created by
 * User: djyin
 * Date: 12/5/13
 * Time: 11:52 AM
 */
public interface OperationLogService extends BaseService<OperationLog, Long> {

    List<OperationLog> queryOperationLogs(Map<String,Object> params);

  /*  Integer queryOperationLogCount(Map<String,Object> params);*/

    int insertOperationLog(OperationLog operationLog);

}
