package com.busap.vcs.data.entity;


public class DailyLiveData extends BaseEntity {

    private String date;

    // 最大同时在线人数
    private String maxOnlineNum;

    // 当日直播人数
    private String liveNum;

    //当日注册开播人数
    private String newRegLiveNum;

    //当日新开播人数
    private String newLiveNum;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMaxOnlineNum() {
        return maxOnlineNum;
    }

    public void setMaxOnlineNum(String maxOnlineNum) {
        this.maxOnlineNum = maxOnlineNum;
    }

    public String getLiveNum() {
        return liveNum;
    }

    public void setLiveNum(String liveNum) {
        this.liveNum = liveNum;
    }

    public String getNewRegLiveNum() {
        return newRegLiveNum;
    }

    public void setNewRegLiveNum(String newRegLiveNum) {
        this.newRegLiveNum = newRegLiveNum;
    }

    public String getNewLiveNum() {
        return newLiveNum;
    }

    public void setNewLiveNum(String newLiveNum) {
        this.newLiveNum = newLiveNum;
    }
}