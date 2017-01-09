package com.busap.vcs.data.model;

import java.util.Date;

/**
 * Created by busap on 2016/3/29.
 */
public class AnchorDetailDisplay {

    private Long id;

    private String title;

    private String name;

    private String phone;

    private String bandPhone;

    private String orgName;

    private Date createDate;

    private Integer giftNumber;

    private Integer pointNumber;

    private Long duration;

    private String totalDuration;

    private String hourDuration;

    private Integer maxAccessNumber;

    private Integer praiseNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBandPhone() {
        return bandPhone;
    }

    public void setBandPhone(String bandPhone) {
        this.bandPhone = bandPhone;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getGiftNumber() {
        return giftNumber;
    }

    public void setGiftNumber(Integer giftNumber) {
        this.giftNumber = giftNumber;
    }

    public Integer getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber(Integer pointNumber) {
        this.pointNumber = pointNumber;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Integer getMaxAccessNumber() {
        return maxAccessNumber;
    }

    public void setMaxAccessNumber(Integer maxAccessNumber) {
        this.maxAccessNumber = maxAccessNumber;
    }

    public Integer getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(Integer praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public String getHourDuration() {
        return hourDuration;
    }

    public void setHourDuration(String hourDuration) {
        this.hourDuration = hourDuration;
    }
}
