package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.PrizeDetail;
import com.busap.vcs.data.model.PrizeDetailDisplay;

import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2015/10/26.
 */
public interface PrizeDetailDao {

    List<PrizeDetailDisplay> select(PrizeDetailDisplay prizeDetailDisplay);
    
    List<PrizeDetailDisplay> selectByPrizeId(Map<String,Object> params);
    
    List<PrizeDetailDisplay> selectAllByPrizeId(Map<String,Object> params);
    
    List<Integer> selectPrizeLevel(Long prizeId);

    int insert(PrizeDetail prizeDetail);

    int update(PrizeDetail prizeDetail);

    int batchInsertPrizeDetail(List<PrizeDetail> list);

    PrizeDetail selectByPrimaryKey(Long id);

    int deleteByPrimaryKey(Long id);
}
