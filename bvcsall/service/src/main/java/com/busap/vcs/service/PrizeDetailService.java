package com.busap.vcs.service;

import com.busap.vcs.data.entity.Prize;
import com.busap.vcs.data.entity.PrizeDetail;
import com.busap.vcs.data.model.PrizeDetailDisplay;

import java.util.List;

/**
 * Created by
 * User: huoshanwei
 * Date: 15/10/26
 * Time: 15:52
 */
public interface PrizeDetailService extends BaseService<Prize, Long> {

    List<PrizeDetailDisplay> queryPrizeDetails(PrizeDetailDisplay prizeDetailDisplay);
    
    List<PrizeDetailDisplay> queryPrizeDetailsByPrizeId(Long prizeId,int prizeLevel,int page,int size);
    
    List<PrizeDetailDisplay> queryAllPrizeDetailsByPrizeId(Long prizeId,int prizeLevel);
    
    List<Integer> queryPrizeLevel(Long prizeId);

    int insertPrizeDetail(PrizeDetail prizeDetail);

    int updatePrizeDetail(PrizeDetail prizeDetail);

    int batchInsertPrizeDetail(List<PrizeDetail> list);

    PrizeDetail queryPrizeDetail(Long id);

    int deletePrizeDetailById(Long id);

}
