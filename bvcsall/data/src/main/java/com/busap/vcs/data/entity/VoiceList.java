package com.busap.vcs.data.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 榜单
 * Created by busap on 2016/8/1.
 */
@Entity
@Table(name = "voice_list")
public class VoiceList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //主键自增

    @Column(name = "name",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String name;//榜单名称

    @Column(name = "start_time",columnDefinition = "datetime NOT NULL",nullable=false)
    private Date startTime;//开始时间

    @Column(name = "end_time",columnDefinition = "datetime NOT NULL",nullable=false)
    private Date endTime;//结束时间

    @Column(name = "person_number",columnDefinition = "int(8) NULL DEFAULT 0",nullable=true)
    private Integer personNumber;//榜单人数

    @Column(name = "type",columnDefinition = "int(4) NOT NULL DEFAULT 1",nullable=false)
    private Long type;//榜单类型

    @Column(name = "create_time",columnDefinition = "datetime NOT NULL",nullable=false)
    private Date createTime;

    @Column(name = "update_time",columnDefinition = "datetime NULL",nullable=true)
    private Date updateTime;

    @Column(name = "state",columnDefinition = "int(4) NOT NULL DEFAULT 1",nullable=false)
    private Long state;//状态 1正常 其他不正常

    @Column(name = "url",columnDefinition = "varchar(255) NULL",nullable=true)
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Integer personNumber) {
        this.personNumber = personNumber;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
