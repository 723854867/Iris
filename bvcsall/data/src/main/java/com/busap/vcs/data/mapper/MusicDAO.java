package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Music;
import com.busap.vcs.data.entity.MusicType;
import com.busap.vcs.data.model.MusicDisplay;
import com.busap.vcs.data.vo.MusicVO;

public interface MusicDAO {

	/**
	 * 获得全部音乐
	 * @return
	 */
	public List<MusicVO> selectAllMusic();

	List<MusicDisplay> selectMusics(Map<String,Object> params);

/*	Integer selectMusicCount(Map<String,Object> params);*/

	int deleteInMusicId(String[] ids);

	int updateStatusInMusicIds(String[] ids);

	int update(Music music);

	Music selectByPrimaryKey(Long id);

	int insert(Music music);

	List<MusicType> selectMusicType();

	int insertMusicType(MusicType musicType);

	MusicType selectMusicTypeByName(String name);

	int deleteMusicType(Long id);

	MusicType selectMusicTypeByPrimaryKey(Long id);

	int updateMusicType(MusicType musicType);

}
