package com.busap.vcs.data.model;

import java.util.Date;

/**
 * Created by busap on 2016/8/3.
 */
public class SingVoteDisplay {

    private Long popularity;

    private Date createTime;

    private String operator;

    private Date updateTime;

    public Long getPopularity() {
        return popularity;
    }

    public void setPopularity(Long popularity) {
        this.popularity = popularity;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
