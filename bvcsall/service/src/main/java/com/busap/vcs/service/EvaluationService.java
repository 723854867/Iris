package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.model.EvaluationDisplay;
import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.Evaluation;

public interface EvaluationService extends BaseService<Evaluation, Long>{

	public void saveEvaluation(Evaluation f);
	
	public void deleteEvaluation(Long id); 
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deleteEvaluation(List<Long> ids); 
	/**
	 * 查询某一视频对应的评论，dmsong add 20150126
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public Page searchEvlauation(Integer pageNo,Integer pageSize,Map<String,Object> params);

	public void deleteEvaluationByIds(List<Long> ids);

	/**
	 * 查询评论信息列表
	 * @param params
	 */
	List<EvaluationDisplay> selectEvaluations(Map<String,Object> params);

/*	Integer selectEvaluationCount(Map<String,Object> params);*/
}
