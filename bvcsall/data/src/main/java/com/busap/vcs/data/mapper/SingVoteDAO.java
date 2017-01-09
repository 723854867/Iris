package com.busap.vcs.data.mapper;

import com.busap.vcs.data.model.SingVoteDisplay;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SingVoteDAO {
	
	//学员榜
	List<Map<String,Object>> getMemberRank(Map<String,Object> params);
	
	//网综榜
	List<Map<String,Object>> getNormalRank(Map<String,Object> params);
	
	//主播榜
	List<Map<String,Object>> getAnchorRank(Map<String,Object> params);
	
	//贡献榜
	List<Map<String,Object>> getContributeRank(Map<String,Object> params);
	
	//贡献榜总榜
	List<Map<String,Object>> getContributeRankTotal(Map<String,Object> params);
	
	//获取投票信息
	List<SingVoteDisplay> selectSingVote(Map<String,Object> params);

}
