package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.Tag;

/**
 * Created by
 * User: djyin
 * Date: 12/5/13
 * Time: 11:52 AM
 */
public interface TagService extends BaseService<Tag, Long> { 
	 
	public List findByStatus(String status);
	
	/**
	 * 	查找我的标签
	 * @param uid 用户ID
	 * @param communal 是否包括公共标签
	 * @return
	 */
	public List<Tag> findMyTags(Long uid,boolean communal);
}
