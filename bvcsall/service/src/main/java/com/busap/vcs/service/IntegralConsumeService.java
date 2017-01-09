package com.busap.vcs.service;


import com.busap.vcs.data.entity.IntegralConsume;

/**
 * 积分消费service
 * Created by zx 
 */
public interface IntegralConsumeService {

    public IntegralConsume insertIntegralConsume(IntegralConsume integralConsume);
    
    public IntegralConsume getIntegralConsumeById(Long icId);
    
    public IntegralConsume getIntegralConsumeByOrderNum(String orderNum);
    
    public IntegralConsume updateByPrimaryKeySelective(IntegralConsume integralConsume);

}
