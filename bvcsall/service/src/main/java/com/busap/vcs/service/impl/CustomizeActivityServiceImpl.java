package com.busap.vcs.service.impl;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.busap.vcs.data.entity.CustomizeActivity;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.CustomizeActivityRepository;
import com.busap.vcs.service.CustomizeActivityService;
import com.busap.vcs.service.IntegralService;
import com.busap.vcs.service.JedisService;

@Service("customizeActivityService")
public class CustomizeActivityServiceImpl extends BaseServiceImpl<CustomizeActivity, Long> implements CustomizeActivityService {
	
    @Resource(name = "customizeActivityRepository")
    private CustomizeActivityRepository customizeActivityRepository;
     
    
    @Autowired
    JedisService jedisService;

	@Autowired
	IntegralService integralService;
    
    @Resource(name = "customizeActivityRepository")
    @Override
    public void setBaseRepository(BaseRepository<CustomizeActivity, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

	@Override
	public CustomizeActivity findCurrentCustomizeActivity() {
		// TODO Auto-generated method stub
		return null;
	}  
    


}
