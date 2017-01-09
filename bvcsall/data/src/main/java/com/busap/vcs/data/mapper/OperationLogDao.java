package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.OperationLog;
import com.busap.vcs.data.entity.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2015/10/12.
 */
@Repository("operationLogDao")
public interface OperationLogDao {

    List<OperationLog> select(Map<String,Object> params);

    int insert(OperationLog operationLog);

/*    Integer selectCount(Map<String,Object> params);*/

    Permission selectPermissionByName(String name);

}
