package com.busap.vcs.data.model;

import com.busap.vcs.data.entity.Feedback;

/**
 * Created by huoshanwei on 2015/10/13.
 */
public class FeedbackDisplay extends Feedback {

    private String statusName;

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatusName() {
        if(getStatus() != null){
            if (getStatus() == 0) {
                return "未跟踪";
            } else if (getStatus() == 1) {
                return "已跟踪";
            }else{
                return "已处理";
            }
        }else{
            return "未跟踪";
        }
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
