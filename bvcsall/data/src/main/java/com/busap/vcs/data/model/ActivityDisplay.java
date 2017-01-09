package com.busap.vcs.data.model;

import com.busap.vcs.data.entity.Activity;

/**
 * Created by huoshanwei on 2015/9/22.
 */
public class ActivityDisplay extends Activity {

    private Integer avCount;
    private Integer pCount;
    private Integer eCount;
    private Integer rCount;

    public Integer getAvCount() {
        return avCount == null ? 0 : avCount;
    }

    public void setAvCount(Integer avCount) {
        this.avCount = avCount;
    }

    public Integer getpCount() {
        return pCount == null ? 0 : pCount;
    }

    public void setpCount(Integer pCount) {
        this.pCount = pCount;
    }

    public Integer geteCount() {
        return eCount == null ? 0 : eCount;
    }

    public void seteCount(Integer eCount) {
        this.eCount = eCount;
    }

    public Integer getrCount() {
        return rCount == null ? 0 : rCount;
    }

    public void setrCount(Integer rCount) {
        this.rCount = rCount;
    }
}
