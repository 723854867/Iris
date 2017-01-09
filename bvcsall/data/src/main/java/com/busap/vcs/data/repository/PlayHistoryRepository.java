package com.busap.vcs.data.repository;

import javax.annotation.Resource; 
import com.busap.vcs.data.entity.PlayHistory;

 
@Resource(name = "playHistoryRepository")
public interface PlayHistoryRepository extends BaseRepository<PlayHistory, Long> {
	
}
