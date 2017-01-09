package com.busap.vcs.data.repository;

import javax.annotation.Resource;
import com.busap.vcs.data.entity.Task;

/**
 * Created by zx 
 */
@Resource(name = "taskRepository")
public interface TaskRepository extends BaseRepository<Task, Long> {
	
	
}
