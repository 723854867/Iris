package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.ShowVideo;
import com.busap.vcs.data.vo.ShowVO;

/**
 * @author Administrator
 *
 */
public interface ShowDAO {

	/**
	 * 根据视频id，查询我拍秀信息
	 * @return
	 */
	public ShowVideo getShowByVideoId(Long id);
	
	/**查询所有我拍秀信息
	 * @return
	 */
	public List<ShowVideo> getAllShow(Map<String,Object> params);
	
	/**
	 * 管理平台查询列表
	 * @param params
	 * @return
	 */
	public List<ShowVO> findAllShows(Map<String,Object> params);
	
	public Integer findAllShowsCount();
}
