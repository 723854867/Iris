package com.busap.vcs.data.vo;

import com.busap.vcs.data.entity.Integral;

/**
 * 积分
 * Created by Knight on 15/10/10.
 */
public class IntegralVO extends Integral {

    private Integer integralSum;

    public Integer getIntegralSum() {
        return integralSum;
    }

    public void setIntegralSum(Integer integralSum) {
        this.integralSum = integralSum;
    }
}
