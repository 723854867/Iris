package com.busap.vcs.service;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.model.SingerDisplay;
import com.busap.vcs.data.model.SongDisplay;
import com.busap.vcs.data.vo.SongVo;

import java.util.List;
import java.util.Map;

public interface SongService extends BaseService<Song, Long>{

    int insertSingerType(SingerType singerType);

    int insertSinger(Singer singer);

    int insertAlbum(Album album);

    int insertSong(Song song);

    List<SongVo> querySongList(Map<String,Object> params);

    List<SongDisplay> querySongAll(Map<String,Object> params);

    Song selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Song song);

    List<SingerType> querySingerTypeAll(Map<String,Object> params);

    List<SingerDisplay> querySingerAll(Map<String,Object> params);

    List<Album> queryAlbumAll(Map<String,Object> params);

    Singer querySingerByPrimaryKey(Long id);

    SingerType querySingerTypeByPrimaryKey(Long id);

    Album queryAlbumByPrimaryKey(Long id);

    int updateSingerByPrimaryKey(Singer singer);

    int updateSingerTypeByPrimaryKey(SingerType singerType);

    int updateAlbumByPrimaryKey(Album album);

    Integer queryCountSongList(Map<String,Object> params);

    Singer selectSingerByName(String name);

    String querySingerGroupByIds(String singerIds);

    int insertSongSinger(SongSinger songSinger);

    List<SongDisplay> querySongAllForImport(Map<String,Object> params);

    int updateSongSinger(SongSinger songSinger);

    SongSinger selectSongSinger(Map<String,Object> params);

}
