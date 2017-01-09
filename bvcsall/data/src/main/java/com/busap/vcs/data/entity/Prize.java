package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by huoshanwei on 2015/10/23.
 */

@Entity
@Table(name = "prize")
public class Prize extends BaseEntity {

    //活动奖项名称
    @Column(name = "name",columnDefinition = "varchar(255) NULL",nullable=false)
    private String name;

    //开始时间
    @Column(name = "start_date",columnDefinition = "datetime NULL",nullable=false)
    private Date startDate;

    //结束时间
    @Column(name = "end_date",columnDefinition = "datetime NULL",nullable=false)
    private Date endDate;

    //活动ID
    @Column(name = "activity_id",columnDefinition = "bigint(20) NULL",nullable=false)
    private Long activityId;

    private Long status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
