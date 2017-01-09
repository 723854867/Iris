package com.busap.vcs.data.mapper;

import com.busap.vcs.data.model.ExportFeedback;
import com.busap.vcs.data.model.FeedbackDisplay;

import java.util.List;
import java.util.Map;

public interface FeedbackDAO {

	public List<Map<String,Object>> selectFeedback(Map<String,Object> params);
	
	public Integer selectFeedbackCount(Map<String,Object> params);

	List<FeedbackDisplay> selectInIds(String[] ids);

	int updateFeedbackInIds(List<FeedbackDisplay> list);

	List<FeedbackDisplay> selectFeedbackList(Map<String,Object> params);

	List<ExportFeedback> exportFeedBackList(Map<String,Object> params);
}
