package com.busap.vcs.data.vo;

import com.busap.vcs.data.entity.Task;

/**
 * 用户任务列表展示
 * Created by yangxinyu on 15/10/10.
 */
public class UserTaskVO extends Task {

    private Integer isFinished;

    private Long integralId;

    public Integer getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
    }

    public Long getIntegralId() {
        return integralId;
    }

    public void setIntegralId(Long integralId) {
        this.integralId = integralId;
    }

}
