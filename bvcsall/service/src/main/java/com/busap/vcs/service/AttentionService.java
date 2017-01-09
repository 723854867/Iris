package com.busap.vcs.service;

import java.util.Date;
import java.util.List;

import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.vo.AttentionRecordVO;
import com.busap.vcs.data.vo.AttentionVO;
import com.busap.vcs.data.vo.FansVO;

public interface AttentionService {
	
	public int deleteAttention(Long uid,Long attentionId,String dataFrom);
	
	public int getAttentionCount(Long uid);
	
	public int getFansCount(Long uid);
	
	public int isAttention(Long uid,Long otherUid);
	
	public int payAttention(Long uid,Long attentionId,String dataFrom);
	
	public List<AttentionVO> getAttentionList(Long uid,Date timestamp,int count);
	
	public List<AttentionVO> getAttentionListNew(Long uid,Long lastId,int count);
	
	public List<FansVO> getFansList(Long uid,Date timestamp,int count);
	
	public List<Long> selectAllAttentionId(Long uid);
	
	public List<Long> selectAllFansId(Long uid);
	
	public List<Long> selectAllFansIdWithoutMajia(Long uid);
	
	public List<AttentionVO> getOtherAttentionList(Long uid,Long otherUid,Date timestamp,int count);

	public List<AttentionVO> findAttentionByCreator(Long uid, Date date);
	
	public List<FansVO> getOtherFansList(Long uid,Long otherUid,Date timestamp,int count);

	public List<AttentionRecordVO> getAllRecords();

	public List<Ruser> getDynamicRecommend(Long uid,Integer count);

	public void deleteAttentionByIds(List<Long> ids);
	
	public void autoAttention();
	
}
