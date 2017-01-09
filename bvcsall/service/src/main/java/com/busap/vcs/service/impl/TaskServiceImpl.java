package com.busap.vcs.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Task;
import com.busap.vcs.data.mapper.TaskDAO;
import com.busap.vcs.data.repository.ActivityRepository;
import com.busap.vcs.data.repository.ActivityVideoRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.TaskRepository;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.TaskService;

@Service("taskService")
public class TaskServiceImpl extends BaseServiceImpl<Task, Long> implements TaskService {
	
    @Resource(name = "taskRepository")
    private TaskRepository taskRepository;
     
    
    @Autowired
    TaskDAO taskDAO;
    
    @Autowired
    JedisService jedisService;
    
    @Resource(name = "taskRepository")
    @Override
    public void setBaseRepository(BaseRepository<Task, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }  
    

}
