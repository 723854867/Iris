package com.busap.vcs.data.repository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.busap.vcs.data.entity.Evaluation;

@Resource(name = "evaluationRepository")
public interface EvaluationRepository extends BaseRepository<Evaluation, Long>{
	
	public void deleteByCreatorIdAndId(Long creatorId,Long id);
	/**
	 * 后台查看评论列表
	 * add by dmsong
	 * add date 20150114
	 * @param start
	 * @param length
	 * @return
	 */
	@Query(nativeQuery=true,value="select e.*,ru.name as creatorName from evaluation e,ruser ru where e.creator_id=ru.id order by e.create_at desc limit ?1,?2")
	public List<Evaluation> evaluationList(Integer start,Integer length);
	
	@Modifying
	@Transactional
	@Query("delete from Evaluation a where a.id in(?1)")
	public void deleteEvaluations(List<Long> ids); 
	
}
