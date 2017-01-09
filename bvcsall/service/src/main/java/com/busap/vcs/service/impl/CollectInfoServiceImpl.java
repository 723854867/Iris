package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.CollectInfo;
import com.busap.vcs.data.mapper.CollectInfoMapper;
import com.busap.vcs.service.CollectInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by Knight on 16/5/5.
 */
@Service("collectInfoService")
public class CollectInfoServiceImpl implements CollectInfoService {

    @Autowired
    private CollectInfoMapper collectInfoMapper;

    @Override
    public boolean saveInfo(Long userId, Long roomId, Long firstDelayTime, Integer blockTimes, String type,String localOutIP, String publishHost, String piliHost,String clientType,String errorCode) {
        if (userId != null && roomId != null && firstDelayTime != null && blockTimes != null) {
            long time = System.currentTimeMillis() / 1000l;
            CollectInfo collectInfo = new CollectInfo();
            collectInfo.setUserId(userId);
            collectInfo.setCreate(time);
            collectInfo.setRoomId(roomId);
            collectInfo.setFirstDelayTime(firstDelayTime);
            collectInfo.setBlockTimes(blockTimes);
            collectInfo.setType(type);
            collectInfo.setLocalOutIP(localOutIP);
            collectInfo.setPublishHost(publishHost);
            collectInfo.setPiliHost(piliHost);
            collectInfo.setClientType(clientType);
            collectInfo.setErrorCode(errorCode);
            return collectInfoMapper.insertSelective(collectInfo) > 0;
        }
        return false;
    }
}
