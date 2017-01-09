package com.busap.vcs.data.mapper;

import java.util.List;

import com.busap.vcs.data.vo.CoverVO;

/**
 * @author Administrator
 *
 */
public interface CoverDAO {

	/**
	 * 获得有效的封面图片列表
	 * @return
	 */
	public List<CoverVO> selectActiveCovers();
}
