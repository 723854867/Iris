package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Feedback;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "feedbackRepository")
public interface FeedbackRepository extends BaseRepository<Feedback, Long> {
	
}
