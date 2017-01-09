package com.busap.vcs.service;

import com.busap.vcs.data.vo.CoverVO;

public interface CoverService {
	
	/**
	 * 随机获得有效的封面图片
	 * @return
	 */
	public CoverVO getRandomActiveCover();
	
}
