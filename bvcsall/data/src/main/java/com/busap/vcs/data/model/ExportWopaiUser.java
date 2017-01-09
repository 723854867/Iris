package com.busap.vcs.data.model;

import java.util.Date;

/**
 * Created by huoshanwei on 2015/11/25.
 */
public class ExportWopaiUser {

    private String id;

    private String name;

    private String videoCount;

    private String fansCount;

    private String realFansCount;

    private String signSum;

    private Double twentyFourPopularity;

    private Double weekPopularity;

    private Double monthPopularity;

    private Date createDate;

    private String sex;

    private String age;

    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }

    public String getSignSum() {
        return signSum;
    }

    public void setSignSum(String signSum) {
        this.signSum = signSum;
    }

    public String getFansCount() {
        return fansCount;
    }

    public void setFansCount(String fansCount) {
        this.fansCount = fansCount;
    }

    public Double getTwentyFourPopularity() {
        return twentyFourPopularity;
    }

    public void setTwentyFourPopularity(Double twentyFourPopularity) {
        this.twentyFourPopularity = twentyFourPopularity;
    }

    public Double getWeekPopularity() {
        return weekPopularity;
    }

    public void setWeekPopularity(Double weekPopularity) {
        this.weekPopularity = weekPopularity;
    }

    public Double getMonthPopularity() {
        return monthPopularity;
    }

    public void setMonthPopularity(Double monthPopularity) {
        this.monthPopularity = monthPopularity;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRealFansCount() {
        return realFansCount;
    }

    public void setRealFansCount(String realFansCount) {
        this.realFansCount = realFansCount;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
