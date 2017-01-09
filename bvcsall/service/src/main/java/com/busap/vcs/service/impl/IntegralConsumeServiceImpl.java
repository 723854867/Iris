package com.busap.vcs.service.impl;

import java.util.Date;
import java.util.List;

import com.busap.vcs.base.IntegralStatus;
import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.mapper.GeneralCustomDAO;
import com.busap.vcs.data.mapper.IntegralConsumeMapper;
import com.busap.vcs.data.mapper.IntegralDAO;
import com.busap.vcs.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 积分消费服务
 * Created by zx 
 */
@Service("integralConsumeService")
public class IntegralConsumeServiceImpl implements IntegralConsumeService {


    @Autowired
    private IntegralConsumeMapper integralConsumeMapper;

    @Autowired
    private IntegralDAO integralConsumeDAO;
    
    @Autowired
    private GeneralCustomDAO generalCustomDAO;


    /**
     * 添加积分消费记录
     * @return          积分消费对象
     */
    @Transactional
    public IntegralConsume insertIntegralConsume(IntegralConsume integralConsume) {
        
        integralConsumeMapper.insertSelective(integralConsume);
        integralConsume.setId(generalCustomDAO.findLastID());
        
//        IntegralConsumeExample example = new IntegralConsumeExample();
//        example.createCriteria().andUserIdEqualTo(integralConsume.getUserId())
//                .andCreateTimeEqualTo(integralConsume.getCreateTime())
//                .andConsumeIntegralEqualTo(integralConsume.getConsumeIntegral());
//        
//        List<IntegralConsume> icList=integralConsumeMapper.selectByExample(example);
//        IntegralConsume integralConsumeNew=null;
//        if(icList!=null&&icList.size()>0) {
//        	integralConsumeNew=icList.get(0);
//        }
        
        		
        return integralConsume;
    }
    
    /**
     * 根据主键获取积分记录
     * @return          积分消费对象
     */
    @Transactional
    public IntegralConsume getIntegralConsumeById(Long icId) {
    	
    	IntegralConsume integralConsume=integralConsumeMapper.selectByPrimaryKey(icId);
        
        return integralConsume;
    }
    
    @Transactional
    public IntegralConsume getIntegralConsumeByOrderNum(String orderNum) {
    	
      IntegralConsumeExample example = new IntegralConsumeExample();
      example.createCriteria().andOuderNumEqualTo(orderNum);
      
      List<IntegralConsume> icList=integralConsumeMapper.selectByExample(example);
      IntegralConsume integralConsumeNew=null;
      if(icList!=null&&icList.size()>0) {
      	integralConsumeNew=icList.get(0);
      }
        
        return integralConsumeNew;
    }
    
    @Transactional
    public IntegralConsume updateByPrimaryKeySelective(IntegralConsume integralConsume) {
        
        integralConsumeMapper.updateByPrimaryKeySelective(integralConsume);
        
        		
        return integralConsume;
    }

}
