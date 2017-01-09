package com.busap.vcs.hlsmedia.http.hls;

/**
 * Created by djyin on 8/5/2014.
 */
public class HlsVideo extends Video{

    /**
     * 分段时间间隔
     */
    Integer segmentTime;

    /**
     * 分段数目
     */
    Integer segments;

    public Integer getSegmentTime() {
        return segmentTime;
    }

    public void setSegmentTime(Integer segmentTime) {
        this.segmentTime = segmentTime;
    }


    public Integer getSegments() {
        return segments;
    }

    public void setSegments(Integer segments) {
        this.segments = segments;
    }
}
