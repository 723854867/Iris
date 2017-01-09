package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.Prize;
import com.busap.vcs.data.entity.PrizeDetail;
import com.busap.vcs.data.mapper.PrizeDao;
import com.busap.vcs.data.mapper.PrizeDetailDao;
import com.busap.vcs.data.model.PrizeDetailDisplay;
import com.busap.vcs.service.PrizeDetailService;
import com.busap.vcs.service.PrizeService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("prizeDetailService")
public class PrizeDetailServiceImpl extends BaseServiceImpl<Prize, Long> implements PrizeDetailService {


    @Resource
    private PrizeDetailDao prizeDetailDao;

    @Override
    public List<PrizeDetailDisplay> queryPrizeDetails(PrizeDetailDisplay prizeDetailDisplay) {
        return prizeDetailDao.select(prizeDetailDisplay);
    }
    
    @Override
    public List<PrizeDetailDisplay> queryPrizeDetailsByPrizeId(Long prizeId,int prizeLevel,int page,int size) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("prizeId", prizeId);
    	params.put("prizeLevel", prizeLevel);
    	params.put("pageStart", (page-1)*size);
    	params.put("pageSize", size);
    	return prizeDetailDao.selectByPrizeId(params);
    }
    
    @Override
	public List<PrizeDetailDisplay> queryAllPrizeDetailsByPrizeId(Long prizeId,int prizeLevel) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("prizeId", prizeId);
    	params.put("prizeLevel", prizeLevel);
    	return prizeDetailDao.selectAllByPrizeId(params);
	}
    
    @Override
    public List<Integer> queryPrizeLevel(Long prizeId) {
        return prizeDetailDao.selectPrizeLevel(prizeId);
    }

    @Override
    public int insertPrizeDetail(PrizeDetail prizeDetail) {
        return prizeDetailDao.insert(prizeDetail);
    }

    @Override
    public int updatePrizeDetail(PrizeDetail prizeDetail) {
        return prizeDetailDao.update(prizeDetail);
    }

    @Override
    public int batchInsertPrizeDetail(List<PrizeDetail> list) {
        return prizeDetailDao.batchInsertPrizeDetail(list);
    }

    @Override
    public PrizeDetail queryPrizeDetail(Long id) {
        return prizeDetailDao.selectByPrimaryKey(id);
    }

    @Override
    public int deletePrizeDetailById(Long id) {
        return prizeDetailDao.deleteByPrimaryKey(id);
    }


}
