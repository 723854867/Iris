package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 音乐专辑
 * Created by busap on 2016/5/30.
 */
@Entity
@Table(name = "album")
public class Album extends BaseEntity {

    private static final long serialVersionUID = -647467027823293562L;

    @Column(name = "name",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String name; //专辑名称

    @Column(name = "album_cover",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String albumCover; //专辑封面

    @Column(name = "state",columnDefinition = "int(4) NOT NULL",nullable=false)
    private Long state; //状态 1正常 0删除

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }
}
