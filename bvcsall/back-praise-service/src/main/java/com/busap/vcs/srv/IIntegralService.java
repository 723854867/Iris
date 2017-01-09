package com.busap.vcs.srv;

/**
 * 积分 service
 * Created by Knight on 15/10/27.
 */
public interface IIntegralService {

    public void getIntegralFromFanNumber(Long userId);

    public void getIntegralFromActivity(Long userId, Long videoId);

}
