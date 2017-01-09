package com.busap.vcs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.model.ExportFeedback;
import com.busap.vcs.data.model.FeedbackDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Feedback;
import com.busap.vcs.data.mapper.FeedbackDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.FeedbackRepository;
import com.busap.vcs.service.FeedbackService;

@Service("feedbackService")
public class FeedbackServiceImpl extends BaseServiceImpl<Feedback, Long> implements FeedbackService {
	@Resource(name = "feedbackRepository")
    private FeedbackRepository feedbackRepository;
	
	@Autowired
	private FeedbackDAO feedbackDAO;
     
    @Resource(name = "feedbackRepository")
    @Override
    public void setBaseRepository(BaseRepository<Feedback, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

	@Override
	public Page findFeedback(Integer pageNo, Integer pageSize,String content,String dataFrom,String startTime,String endTime) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pageSize", pageSize);
		params.put("pageStart", (pageNo-1)*pageSize);
		params.put("content", content);
		params.put("dataFrom", dataFrom);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		List<Map<String,Object>> fbList = feedbackDAO.selectFeedback(params);
		Map<String,Object> pageParams = new HashMap<String,Object>();
		pageParams.put("content", content);
		pageParams.put("dataFrom", dataFrom);
		pageParams.put("startTime", startTime);
		pageParams.put("endTime", endTime);
		Integer totalCount = feedbackDAO.selectFeedbackCount(pageParams);
		Page pinfo = new PageImpl(fbList,new PageRequest(pageNo-1, pageSize, null),totalCount);
		return pinfo;
	}

	@Override
	public List<FeedbackDisplay> queryFeedbackInIds(String[] ids){
		return feedbackDAO.selectInIds(ids);
	}

	@Override
	public int updateFeedbackInIds(List<FeedbackDisplay> list){
		return feedbackDAO.updateFeedbackInIds(list);
	}

	@Override
	public List<FeedbackDisplay> queryFeedbackList(Map<String,Object> params){
		return feedbackDAO.selectFeedbackList(params);
	}

	@Override
	public List<ExportFeedback> exportFeedBackList(Map<String,Object> params){
		return feedbackDAO.exportFeedBackList(params);
	}
}
