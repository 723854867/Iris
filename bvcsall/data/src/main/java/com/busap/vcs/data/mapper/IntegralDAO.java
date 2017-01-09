package com.busap.vcs.data.mapper;


import com.busap.vcs.data.entity.IntegralStaDTO;
import com.busap.vcs.data.model.IntegralStaDisplay;
import com.busap.vcs.data.vo.IntegralDetailVO;

import java.util.List;
import java.util.Map;


/**
 * 积分自定义sql
 * Created by yangxinyu on 15/10/16.
 */
public interface IntegralDAO {

    public List<IntegralDetailVO> findUserIntegralDetailList(Map<String,Object> params);
    
    public List<IntegralStaDTO> integralMultiTypeStatistics();

    List<IntegralStaDisplay> selectIntegralMultiTypeStatistics();
}
