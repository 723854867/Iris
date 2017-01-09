package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.LiveNotice;

public interface LiveNoticeService extends BaseService<LiveNotice, Long> {
	
	public List<LiveNotice> findLiveNoticeByCreatorId(Long creatorId);
 
}
