package com.busap.vcs.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.Favorite;
import com.busap.vcs.data.entity.Video;

public interface FavoriteService extends BaseService<Favorite, Long>{ 
	
	public Page<Video> findFavoriteList(Integer page, Integer size, String uid);
	public List<Video> findFavoriteListByLastId(Date date, Integer count,String uid);
	
	public void saveFavorite(Favorite f);
	
	public void deleteFavorite(Long videoId,Long creator);
	
	/**
	 * 条件查询收藏视频
	 * @param page
	 * @param size
	 * @param uid
	 * @param filters
	 * @return
	 */
	public Page<Video> findFavoriteList(Integer page, Integer size,Long uid,List<Filter> filters);
	
	
	/**
	 * 删除某用户收藏的视频列表
	 * @param creator
	 */
	public void deleteFavorite(Long creator);
	
	public boolean isFavorited(Long videoId,Long creator);
}
