package com.busap.vcs.data.mapper;

import java.util.List;

import com.busap.vcs.data.vo.LoadConfigUrlVO;

public interface LoadConfigUrlDAO {

	public List<LoadConfigUrlVO> findAllLoadConfigUrl();
	public List<LoadConfigUrlVO> findLoadConfigUrlByClientPf(String clientPf);
	
}
