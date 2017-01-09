package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 歌手
 * Created by busap on 2016/5/30.
 */
@Entity
@Table(name = "singer")
public class Singer extends BaseEntity{

    private static final long serialVersionUID = 1210748564579821677L;

    @Column(name = "name",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String name; //歌手名称

    @Column(name = "singer_type",columnDefinition = "int(4) NULL",nullable=true)
    private Long singerType; //歌手类型

    @Column(name = "state",columnDefinition = "int(4) NOT NULL",nullable=false)
    private Long state; //状态 1正常 0删除\

    @Column(name = "avatar",columnDefinition = "varchar(255) NULL",nullable=true)
    private String avatar;//歌手头像

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSingerType() {
        return singerType;
    }

    public void setSingerType(Long singerType) {
        this.singerType = singerType;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
