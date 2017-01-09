package com.busap.vcs.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.ShowVideo;

public interface ShowVideoService extends BaseService<ShowVideo, Long> {
	
	//获得我拍秀列表
	public List<ShowVideo> getShowList(String uid,Long timestamp,Integer count);
	
	
	//通过视频id获得我拍秀
	public ShowVideo getShowByVideoId(Long videoId);
	
	/**
	 * 后台分页查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page findShowVideos(Integer pageNo,Integer pageSize);
}
