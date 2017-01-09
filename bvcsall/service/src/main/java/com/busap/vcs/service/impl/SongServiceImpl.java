package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.mapper.SongDao;
import com.busap.vcs.data.model.SingerDisplay;
import com.busap.vcs.data.model.SongDisplay;
import com.busap.vcs.data.vo.SongVo;
import com.busap.vcs.service.SongService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2015/12/23.
 */
@Service("songService")
public class SongServiceImpl extends BaseServiceImpl<Song, Long> implements SongService {

	@Resource
	private SongDao songDao;

	@Override
	public int insertSingerType(SingerType singerType) {
		return songDao.insertSingerType(singerType);
	}

	@Override
	public int insertSinger(Singer singer) {
		return songDao.insertSinger(singer);
	}

	@Override
	public int insertAlbum(Album album) {
		return songDao.insertAlbum(album);
	}

	@Override
	public int insertSong(Song song) {
		return songDao.insertSong(song);
	}

	@Override
	public List<SongVo> querySongList(Map<String, Object> params) {
		return songDao.selectSongList(params);
	}

	@Override
	public List<SongDisplay> querySongAll(Map<String, Object> params) {
		return songDao.selectSongAll(params);
	}

	@Override
	public Song selectByPrimaryKey(Long id) {
		return songDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(Song song) {
		return songDao.updateByPrimaryKey(song);
	}

	@Override
	public List<SingerType> querySingerTypeAll(Map<String, Object> params) {
		return songDao.selectSingerTypeAll(params);
	}

	@Override
	public List<SingerDisplay> querySingerAll(Map<String, Object> params) {
		return songDao.selectSingerAll(params);
	}

	@Override
	public List<Album> queryAlbumAll(Map<String, Object> params) {
		return songDao.selectAlbumAll(params);
	}

	@Override
	public Album queryAlbumByPrimaryKey(Long id) {
		return songDao.selectAlbumByPrimaryKey(id);
	}

	@Override
	public SingerType querySingerTypeByPrimaryKey(Long id) {
		return songDao.selectSingerTypeByPrimaryKey(id);
	}

	@Override
	public Singer querySingerByPrimaryKey(Long id) {
		return songDao.selectSingerByPrimaryKey(id);
	}

	@Override
	public int updateAlbumByPrimaryKey(Album album) {
		return songDao.updateAlbumByPrimaryKey(album);
	}

	@Override
	public int updateSingerTypeByPrimaryKey(SingerType singerType) {
		return songDao.updateSingerTypeByPrimaryKey(singerType);
	}

	@Override
	public int updateSingerByPrimaryKey(Singer singer) {
		return songDao.updateSingerByPrimaryKey(singer);
	}

	@Override
	public Integer queryCountSongList(Map<String, Object> params) {
		return songDao.selectCountSongList(params);
	}

	@Override
	public Singer selectSingerByName(String name) {
		return songDao.selectSingerByName(name);
	}

	@Override
	public String querySingerGroupByIds(String singerIds) {
		return songDao.selectSingerGroupByIds(singerIds);
	}

	@Override
	public int insertSongSinger(SongSinger songSinger) {
		return songDao.insertSongSinger(songSinger);
	}

	@Override
	public List<SongDisplay> querySongAllForImport(Map<String, Object> params) {
		return songDao.selectSongAllForImport(params);
	}

	@Override
	public int updateSongSinger(SongSinger songSinger){
		return songDao.updateSongSinger(songSinger);
	}

	@Override
	public SongSinger selectSongSinger(Map<String,Object> params){
		return songDao.selectSongSinger(params);
	}

}
