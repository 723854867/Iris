package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.*;
import com.busap.vcs.data.model.SingerDisplay;
import com.busap.vcs.data.model.SongDisplay;
import com.busap.vcs.data.vo.SongVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SongDao {

    int insertSingerType(SingerType singerType);

    int insertSinger(Singer singer);

    int insertAlbum(Album album);

    int insertSong(Song song);

    List<SongVo> selectSongList(Map<String,Object> params);

    List<SongDisplay> selectSongAll(Map<String,Object> params);

    Song selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Song song);

    List<SingerType> selectSingerTypeAll(Map<String,Object> params);

    List<SingerDisplay> selectSingerAll(Map<String,Object> params);

    List<Album> selectAlbumAll(Map<String,Object> params);

    Singer selectSingerByPrimaryKey(Long id);

    SingerType selectSingerTypeByPrimaryKey(Long id);

    Album selectAlbumByPrimaryKey(Long id);

    int updateSingerByPrimaryKey(Singer singer);

    int updateSingerTypeByPrimaryKey(SingerType singerType);

    int updateAlbumByPrimaryKey(Album album);

    Integer selectCountSongList(Map<String,Object> params);

    Singer selectSingerByName(String name);

    String selectSingerGroupByIds(String singerIds);

    int insertSongSinger(SongSinger songSinger);

    List<SongDisplay> selectSongAllForImport(Map<String,Object> params);

    int updateSongSinger(SongSinger songSinger);

    SongSinger selectSongSinger(Map<String,Object> params);

}