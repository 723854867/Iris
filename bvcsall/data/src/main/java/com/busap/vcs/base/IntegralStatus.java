package com.busap.vcs.base;

/**
 * 积分记录状态
 * Created by yangxinyu on 15/10/8.
 */
public enum IntegralStatus {

    CREATE(0, "创建"),
    RECEIVE(1, "已领取"),
    INVALID(2, "无效");

    public int status;

    public String desc;

    IntegralStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
