package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 歌曲歌手关系表
 * Created by busap on 2016/5/30.
 */
@Entity
@Table(name = "song_singer")
public class SongSinger extends BaseEntity {

    private static final long serialVersionUID = 848218132826675965L;

    @Column(name = "song_id",columnDefinition = "bigint(8) NOT NULL",nullable=false)
    private Long songId; //歌曲ID

    @Column(name = "singer_id",columnDefinition = "bigint(8) NOT NULL",nullable=false)
    private Long singerId; //歌手ID

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public Long getSingerId() {
        return singerId;
    }

    public void setSingerId(Long singerId) {
        this.singerId = singerId;
    }
}
