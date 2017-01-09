package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.Prize;
import com.busap.vcs.data.mapper.PrizeDao;
import com.busap.vcs.data.model.ActivityVideoUser;
import com.busap.vcs.service.PrizeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("prizeService")
public class PrizeServiceImpl extends BaseServiceImpl<Prize, Long> implements PrizeService {


    @Resource
    private PrizeDao prizeDao;

    @Override
    public List<Prize> queryPrizes(Prize prize) {
        return prizeDao.select(prize);
    }
    
    @Override
    public List<Prize> queryAvaliblePrizes(Long activityId) {
    	return prizeDao.selectAvalible(activityId);
    }

    @Override
    public int insertPrize(Prize prize) {
        return prizeDao.insert(prize);
    }

    @Override
    public int updatePrize(Prize prize) {
        return prizeDao.update(prize);
    }

    @Override
    public Prize queryPrize(Long id) {
        return prizeDao.selectByPrimaryKey(id);
    }

    @Override
    public int deletePrizeById(Long id) {
        return prizeDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<ActivityVideoUser> queryActivityVideoUsers(Map<String,Object> params){
        return prizeDao.selectActivityVideoUsers(params);
    }

}
