package com.busap.vcs.service;

import com.busap.vcs.data.model.ExportFeedback;
import com.busap.vcs.data.model.FeedbackDisplay;
import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.Feedback;

import java.util.List;
import java.util.Map;

/**
 * Created by
 * User: djyin
 * Date: 12/5/13
 * Time: 11:52 AM
 */
public interface FeedbackService extends BaseService<Feedback, Long> {
	Page findFeedback(Integer pageStart,Integer pageSize,String content,String dataFrom,String startTime,String endTime);

	List<FeedbackDisplay> queryFeedbackInIds(String[] ids);

	int updateFeedbackInIds(List<FeedbackDisplay> list);

	List<FeedbackDisplay> queryFeedbackList(Map<String,Object> params);

	List<ExportFeedback> exportFeedBackList(Map<String,Object> params);

}