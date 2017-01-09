package com.busap.vcs.data.model;

import com.busap.vcs.data.entity.Ruser;

/**
 * Created by busap on 2016/1/27.
 */
public class RuserDisplay extends Ruser{

    private Long realFansCount;

    private String orgName;

    public Long getRealFansCount() {
        return realFansCount;
    }

    public void setRealFansCount(Long realFansCount) {
        this.realFansCount = realFansCount;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
