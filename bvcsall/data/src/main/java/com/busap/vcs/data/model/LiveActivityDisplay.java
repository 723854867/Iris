package com.busap.vcs.data.model;

import com.busap.vcs.data.entity.LiveActivity;

/**
 * Created by Knight on 16/3/24.
 */
public class LiveActivityDisplay extends LiveActivity {

    private String gifts;

    private String actTime;

    private String createTime;

    public String getGifts() {
        return gifts;
    }

    public void setGifts(String gifts) {
        this.gifts = gifts;
    }

    public String getActTime() {
        return actTime;
    }

    public void setActTime(String actTime) {
        this.actTime = actTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
