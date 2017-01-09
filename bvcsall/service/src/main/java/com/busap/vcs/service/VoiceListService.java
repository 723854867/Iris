package com.busap.vcs.service;

import com.busap.vcs.data.entity.VoiceList;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2016/8/2.
 */
public interface VoiceListService {

    int insert(VoiceList voiceList);

    List<VoiceList> queryVoiceList(Map<String,Object> params);
    
    public VoiceList getRankConfig(Date currentTime,Integer type);

    VoiceList queryVoiceList(Long id);

    int updateByPrimaryKey(VoiceList voiceList);

}
