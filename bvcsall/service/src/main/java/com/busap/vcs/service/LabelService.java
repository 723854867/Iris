package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Label;

public interface LabelService extends BaseService<Label, Long> {
	public List<Label> findMatchTag(String tag);

	/**
	 * 获取label信息列表
	 * @param params
	 * @return Label List
	 */
	List<Label> queryLabels(Map<String,Object> params);

	/**
	 * 获取label信息列表count
	 * @param params
	 * @return Label List
	 */
	Integer queryLabelsCount(Map<String,Object> params);

}
