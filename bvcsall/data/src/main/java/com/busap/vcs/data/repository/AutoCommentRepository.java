package com.busap.vcs.data.repository;

import com.busap.vcs.data.entity.AutoComment;


import javax.annotation.Resource;

/**
 * 自动评论
 * Created by knight
 */
@Resource(name = "autoCommentRepository")
public interface AutoCommentRepository extends BaseRepository<AutoComment, Long> {
	
	
}
