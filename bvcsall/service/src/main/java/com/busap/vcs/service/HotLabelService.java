package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.HotLabel;

public interface HotLabelService extends BaseService<HotLabel, Long> {

	public List<HotLabel> findAllOrderByDisplayorder();
	public List<HotLabel> find4ByShowOrder();

/*	Integer queryHotLabelsCount(Map<String,Object> params);*/

	List<HotLabel> queryHotLabels(Map<String,Object> params);

	int insert(HotLabel hotLabel);

}
