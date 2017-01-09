package com.busap.vcs.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.MusicType;
import com.busap.vcs.data.mapper.MusicDAO;
import com.busap.vcs.data.model.MusicDisplay;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Music;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.service.MusicService;

@Service("musicService")
public class MusicServiceImpl extends BaseServiceImpl<Music, Long> implements MusicService {
 
	@Resource
	private MusicDAO musicDAO;

	@Resource(name = "musicRepository")
	@Override
	public void setBaseRepository(BaseRepository<Music, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	}

	@Override
	public Page listpage(int page, int size, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MusicDisplay> queryMusics(Map<String,Object> params){
		return musicDAO.selectMusics(params);
	}

/*	@Override
	public Integer queryMusicCount(Map<String,Object> params){
		return musicDAO.selectMusicCount(params);
	}*/

	@Override
	public int deleteInMusicId(String[] ids){
		return musicDAO.deleteInMusicId(ids);
	}

	@Override
	public int updateStatusInMusicIds(String[] ids){
		return musicDAO.updateStatusInMusicIds(ids);
	}

	@Override
	public int updateMusic(Music music){
		return musicDAO.update(music);
	}

	@Override
	public Music queryMusic(Long id){
		return musicDAO.selectByPrimaryKey(id);
	}

	@Override
	public int insertMusic(Music music){
		return musicDAO.insert(music);
	}

	@Override
	public List<MusicType> selectMusicType(){
		return musicDAO.selectMusicType();
	}

	@Override
	public int insertMusicType(MusicType musicType){
		return musicDAO.insertMusicType(musicType);
	}

	@Override
	public MusicType selectMusicTypeByName(String name){
		return musicDAO.selectMusicTypeByName(name);
	}

	@Override
	public int deleteMusicType(Long id){
		return musicDAO.deleteMusicType(id);
	}

	@Override
	public MusicType selectMusicTypeByPrimaryKey(Long id){
		return musicDAO.selectMusicTypeByPrimaryKey(id);
	}

	@Override
	public int updateMusicType(MusicType musicType){
		return musicDAO.updateMusicType(musicType);
	}
	
}
