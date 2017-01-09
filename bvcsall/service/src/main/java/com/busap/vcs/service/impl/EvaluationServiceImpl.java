package com.busap.vcs.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.model.EvaluationDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Evaluation;
import com.busap.vcs.data.entity.Video;
import com.busap.vcs.data.mapper.EvaluationDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.EvaluationRepository;
import com.busap.vcs.data.repository.VideoRepository;
import com.busap.vcs.data.vo.EvaluationVO;
import com.busap.vcs.service.EvaluationService;
import com.busap.vcs.service.VideoService;

@Service("evaluationService")
public class EvaluationServiceImpl extends BaseServiceImpl<Evaluation, Long> implements
EvaluationService {
 
	 @Autowired
	 private EvaluationDAO evaluationDao;
	 
	@Resource(name = "evaluationRepository")
	private EvaluationRepository evaluationRepository;
	 
	@Resource(name="videoRepository") 
	private VideoRepository videoRepository;
	
	@Resource(name="videoService") 
	private VideoService videoService;

	@Resource(name = "evaluationRepository")
	@Override
	public void setBaseRepository(BaseRepository<Evaluation, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	}   
	
	public void saveEvaluation(Evaluation f){
		this.save(f);
		if(f.getVideoId()!=null) {
			videoRepository.incEvaluationCount(f.getVideoId());
			//评论后重新计算视频热度值
			videoService.updateHotPointByVideoId(f.getVideoId());
			//重新计算当日视频热度
			videoService.excuteVideoDayHotValue(f.getVideoId());
		}
	}

	public void deleteEvaluation(Long id){
		Evaluation e = evaluationRepository.findOne(id); 
		if(e!=null){
			this.delete(id);
			if(e.getVideoId()!=null){
				int count = videoRepository.decEvaluationCount(e.getVideoId());
				Video v = videoRepository.findOne(e.getVideoId());
				v.setEvaluationCount(count);
				videoRepository.save(v);
				//删除评论后重新计算视频热度值
				videoService.updateHotPointByVideoId(e.getVideoId());
				//重新计算当日视频热度
				videoService.excuteVideoDayHotValue(e.getVideoId());
			}
		}
	}

	@Override
	public Page searchEvlauation(Integer pageNo, Integer pageSize,
			Map<String, Object> params) {
		List<EvaluationVO> evls = evaluationDao.searchEvaluation(params);
		Integer count = evaluationDao.searchEvaluationCount(params);
		
		Page<EvaluationVO> pinfo = new PageImpl<EvaluationVO>(evls,new PageRequest(pageNo-1, pageSize, null),count);
		return pinfo;
	}

	@Override
	public void deleteEvaluationByIds(List<Long> ids) {
		try {
			evaluationDao.deleteEvaluation(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteEvaluation(List<Long> ids) {
		if(ids!=null){
			for(Long id:ids){
				this.deleteEvaluation(id);
			}
		}
		
	}

	/**
	 * 查询评论信息列表
	 * @param params
	 */
	@Override
	public List<EvaluationDisplay> selectEvaluations(Map<String,Object> params){
		return evaluationDao.selectEvaluations(params);
	}

	/**
	 * 获取评论信息列表数量
	 * @param params
	 */
/*	@Override
	public Integer selectEvaluationCount(Map<String,Object> params){
		return evaluationDao.selectEvaluationCount(params);
	}*/

}
