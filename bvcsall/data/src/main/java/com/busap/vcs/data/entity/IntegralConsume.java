package com.busap.vcs.data.entity;

import java.util.Date;

public class IntegralConsume {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private String description;

    private String consumeType;

    private Long consumeId;

    private Integer status;

    private Long userId;

    private Integer consumeIntegral;

    private String ouderNum;

    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(String consumeType) {
        this.consumeType = consumeType;
    }

    public Long getConsumeId() {
        return consumeId;
    }

    public void setConsumeId(Long consumeId) {
        this.consumeId = consumeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getConsumeIntegral() {
        return consumeIntegral;
    }

    public void setConsumeIntegral(Integer consumeIntegral) {
        this.consumeIntegral = consumeIntegral;
    }

    public String getOuderNum() {
        return ouderNum;
    }

    public void setOuderNum(String ouderNum) {
        this.ouderNum = ouderNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}