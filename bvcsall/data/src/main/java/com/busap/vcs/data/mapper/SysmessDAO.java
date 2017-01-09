package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.SystemMess;
import com.busap.vcs.data.vo.MessVO;
import com.busap.vcs.data.vo.SysmessVO;

public interface SysmessDAO {
	/**
	 * 按条件查询评论信息
	 * @param params key取值【pageStart,pageSize,creatorId,videoId,pid】等
	 * @return
	 */
	public List<SysmessVO> searchSysmess(Map<String,Object> params); 
	
	/**
	 * 按条件评论总条数
	 * @param params
	 * @return
	 */
	public Integer searchSysmessCount(Map<String,Object> params); 
	
	/**
	 * 获取尚未发布的系统消息
	 * @return
	 */
	public List<SystemMess> searchPlan();
	
	/**
	 * 获取推送给某个用户的有效系统
	 * @return
	 */
	public List<SysmessVO> searchAvailableSysmessByUid(Map<String,Object> params);
	
	/**
	 * 获取推送给某个用户的个人消息
	 * @return
	 */
	public List<MessVO> findUserMessByUid(Map<String,Object> params);
	
	/**
	 * 获得用户的赞消息
	 * @param params
	 * @return
	 */
	public List<MessVO> findUserPraiseMessByUid(Map<String,Object> params);
	
	/**
	 * 获得用户的评论消息
	 * @param params
	 * @return
	 */
	public List<MessVO> findUserEvaluationMessByUid(Map<String,Object> params);
	
	/**
	 * 获得用户的关注消息
	 * @param params
	 * @return
	 */
	public List<MessVO> findUserAttentionMessByUid(Map<String,Object> params);
	
	/**
	 * 获得用户的转发消息
	 * @param params
	 * @return
	 */
	public List<MessVO> findUserForwardMessByUid(Map<String,Object> params);
}
