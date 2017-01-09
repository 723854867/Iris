package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.AutoComment;
import com.busap.vcs.data.repository.AutoCommentRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.service.AutoCommentService;
import com.busap.vcs.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("autoCommentService")
public class AutoCommentServiceImpl extends BaseServiceImpl<AutoComment, Long> implements AutoCommentService {
	
    @Resource(name = "autoCommentRepository")
    private AutoCommentRepository autoCommentRepository;
     
    
    @Autowired
    JedisService jedisService;
    
    @Resource(name = "autoCommentRepository")
    @Override
    public void setBaseRepository(BaseRepository<AutoComment, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }  
    

}
