package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.MusicType;
import com.busap.vcs.data.model.MusicDisplay;
import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.Music;

/**
 * Created by
 * User: dmsong
 * Date: 02/03/2015
 */
public interface MusicService extends BaseService<Music, Long> {
 
	/**
	 * 投诉信息分页查询，dmsong于20150115添加
	 * @param page
	 * @param size
	 * @param params
	 * @return
	 */
	public Page listpage(int page,int size,Map<String,Object> params);

	List<MusicDisplay> queryMusics(Map<String,Object> params);

	/*Integer queryMusicCount(Map<String,Object> params);*/

	int deleteInMusicId(String[] ids);

	int updateStatusInMusicIds(String[] ids);

	int updateMusic(Music music);

	Music queryMusic(Long id);

	int insertMusic(Music music);

	List<MusicType> selectMusicType();

	int insertMusicType(MusicType musicType);

	MusicType selectMusicTypeByName(String name);

	int deleteMusicType(Long id);

	MusicType selectMusicTypeByPrimaryKey(Long id);

	int updateMusicType(MusicType musicType);
}
