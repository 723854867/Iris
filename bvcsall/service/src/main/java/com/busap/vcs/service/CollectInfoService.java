package com.busap.vcs.service;

/**
 * interface
 * Created by Knight on 16/5/5.
 */
public interface CollectInfoService {

    public boolean saveInfo(Long userId, Long roomId, Long firstDelayTime, Integer blockTimes, String type,String localOutIP, String publishHost, String piliHost,String clientType,String errorCode);
}
