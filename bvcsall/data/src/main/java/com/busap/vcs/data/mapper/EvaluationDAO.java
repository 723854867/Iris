package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Evaluation;
import com.busap.vcs.data.model.EvaluationDisplay;
import com.busap.vcs.data.vo.EvaluationVO;

public interface EvaluationDAO {
	/**
	 * 按条件查询评论信息
	 * @param params key取值【pageStart,pageSize,creatorId,videoId,pid】等
	 * @return
	 */
	public List<EvaluationVO> searchEvaluation(Map<String,Object> params); 
	
	/**
	 * 按条件评论总条数
	 * @param params
	 * @return
	 */
	public Integer searchEvaluationCount(Map<String,Object> params);

	/**
	 * 根据ID删除转发
	 * @param idList forwardID
	 */
	public void deleteEvaluation(List<Long> idList);

	/**
	 * 查询评论信息列表
	 * @param params
	 */
	List<EvaluationDisplay> selectEvaluations(Map<String,Object> params);

	/*Integer selectEvaluationCount(Map<String,Object> params);*/
}
