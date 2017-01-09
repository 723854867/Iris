package com.busap.vcs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.RuserLiveActivity;
import com.busap.vcs.data.mapper.RuserLiveActivityDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.RuserLiveActivityRepository;
import com.busap.vcs.service.RuserLiveActivityService;

@Service("ruserLiveActivityService")
public class RuserLiveActivityServiceImpl extends BaseServiceImpl<RuserLiveActivity, Long> implements RuserLiveActivityService {
	
    @Resource(name = "ruserLiveActivityRepository")
    private RuserLiveActivityRepository ruserliveActivityRepository;
    
    @Resource(name = "ruserLiveActivityRepository")
    @Override
    public void setBaseRepository(BaseRepository<RuserLiveActivity, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }
    
    @Autowired
    RuserLiveActivityDAO ruserLiveActivityDAO;


	@Override
	public Integer isJoin(Long uid, Long liveActivityId) {
		return ruserliveActivityRepository.isJoin(uid,liveActivityId);
	}


	@Override
	public List<Long> getLiveActivityIdByUid(Long uid) {
		return ruserliveActivityRepository.getLiveActivityIdByUid(uid);
	}

    @Override
    public Integer getCountByLiveActivityId(Long liveActivityId) {
        return ruserliveActivityRepository.getCountByLiveActivityId(liveActivityId);
    }
}
