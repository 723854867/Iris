package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.RecommandPosition;

public interface RecommandPositionService extends BaseService<RecommandPosition, Long> {
	/**
	 * 获取当前页推荐位列表
	 * @param page
	 * @return
	 */
	List<Map<String,Object>> findCurrentPagePositions(Integer page);
	/**
	 * 获取当前页之前的推荐位个数
	 * @param page
	 * @return
	 */
	Integer findPrePagePositionCount(Integer page);

}
