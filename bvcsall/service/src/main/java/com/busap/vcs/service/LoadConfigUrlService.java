package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.LoadConfigUrl;
import com.busap.vcs.data.vo.LoadConfigUrlVO;

public interface LoadConfigUrlService extends BaseService<LoadConfigUrl,Long> {
	/**
	 * load All LoadConfigUrl
	 */
	List<LoadConfigUrlVO> findAllLoadConfigUrl();
	
	/*
	 * 根据平台获取加载视频url
	 */
	List<LoadConfigUrlVO> findLoadConfigUrlByClientPf(String clientPf);
	
}
