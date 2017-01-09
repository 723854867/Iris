package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.vo.ComplainVO;

public interface ComplainDAO {
	/**
	 * 按条件查询投诉信息
	 * @param params
	 * @return
	 */
	public List<ComplainVO> searchComplain(Map<String,Object> params); 
	/**
	 * 按条件查询投诉信息总条数
	 * @param params
	 * @return
	 */
	public Integer searchComplainCount(Map<String,Object> params);
}
