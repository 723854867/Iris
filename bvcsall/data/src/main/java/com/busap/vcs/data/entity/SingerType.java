package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 歌手分类
 * Created by busap on 2016/5/30.
 */
@Entity
@Table(name = "singer_type")
public class SingerType extends BaseEntity {

    private static final long serialVersionUID = -1254132361962056392L;

    @Column(name = "name",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String name; //分类名称

    @Column(name = "state",columnDefinition = "int(4) NOT NULL",nullable=false)
    private Long state; //状态 1正常 0删除

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }
}
