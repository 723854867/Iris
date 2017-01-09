package com.busap.vcs.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.VoiceList;
import com.busap.vcs.data.mapper.VoiceListDao;
import com.busap.vcs.data.repository.VoiceListRepository;
import com.busap.vcs.service.VoiceListService;

/**
 * Created by busap on 2016/8/2.
 */
@Service("voiceListService")
public class VoiceListServiceImpl implements VoiceListService {

    @Resource
    private VoiceListDao voiceListDao;
    
    @Resource
    private VoiceListRepository voiceListRepository;

    @Override
    public int insert(VoiceList voiceList) {
        return voiceListDao.insert(voiceList);
    }

    @Override
    public List<VoiceList> queryVoiceList(Map<String,Object> params){
        return voiceListDao.selectVoiceList(params);
    }

	@Override
	public VoiceList getRankConfig(Date currentTime, Integer type) {
		return voiceListRepository.getRankConfig(currentTime, type);
	}

    @Override
    public VoiceList queryVoiceList(Long id) {
        return voiceListDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(VoiceList voiceList) {
        return voiceListDao.updateByPrimaryKey(voiceList);
    }
}
