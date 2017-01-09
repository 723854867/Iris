package com.busap.vcs.data.vo;

import java.util.List;
import java.util.Map;

/**
 * 用户查询签到记录页面返回类型
 * Created by Knight on 15/10/19.
 */
public class UserSignDetailVO {

    private List<Map<String, String>> sign;

    private Long userId;

    private Integer integralSum;

    // 连续签到次数
    private Integer continueNum;

    public List<Map<String, String>> getSign() {
        return sign;
    }

    public void setSign(List<Map<String, String>> sign) {
        this.sign = sign;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getIntegralSum() {
        return integralSum;
    }

    public void setIntegralSum(Integer integralSum) {
        this.integralSum = integralSum;
    }

    public Integer getContinueNum() {
        return continueNum;
    }

    public void setContinueNum(Integer continueNum) {
        this.continueNum = continueNum;
    }
}
