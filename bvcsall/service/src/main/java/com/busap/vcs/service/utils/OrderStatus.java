package com.busap.vcs.service.utils;

/**
 * 订单状态
 * Created by Knight on 15/12/23.
 */
public enum OrderStatus {
    CREATE(1, "等待付款"),
    PAID(2, "已经付款");

    private int status;

    private String desc;

    OrderStatus(int status, String desc) {
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
