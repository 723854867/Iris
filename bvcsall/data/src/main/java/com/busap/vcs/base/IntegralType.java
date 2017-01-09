package com.busap.vcs.base;

/**
 * 积分获取来源
 * Created by Knight on 15/10/8.
 */
public enum IntegralType {
//    SIGN(1, "sign"),        // 签到
//    TASK(2, "task"),        // 任务
//    INVITE(3, "invite"),    // 邀请
//    DAILY(4, "daily_task"), // 每日任务
//    ACT(5, "activity"),     // 活动
//    DOWNLOAD(6, "app_download");  // 下载app
    ;
    public int type;

    public String desc;

    IntegralType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDescByType(int type) {
        String desc = "";
        for (IntegralType integralType : IntegralType.values()) {
            if (type == integralType.getType()) {
                desc = integralType.getDesc();
            }
        }
        return desc;
    }
}
