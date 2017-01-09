package com.busap.vcs.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.SingVote;
import com.busap.vcs.data.model.SingVoteDisplay;

public interface SingVoteService extends BaseService<SingVote, Long> {
	
	//获得榜单信息
	public List<Map<String,Object>> getRank(Integer type,Integer count,Date startTime,Date endTime,Long uid);
	
	//人工干预投票
	public void manualVote(Date createTime,Long destId,Long popularity,Long operationId);
	
	
	public Long getPopularity(Long destId);
	
	
	public Long getContributePopularity(Long creatorId);
	
	public Double getUserPopularity(Long uid,Integer type);
	
	public void createRank(Integer type);

	//获取投票信息
	List<SingVoteDisplay> querySingVote(Map<String,Object> params);
	
}
