package com.busap.vcs.hlsmedia.http.transcode.ffmpeg;

/**
 * Created by djyin on 8/6/2014.
 * 根据ffprobe输出json对象创建的metadata对象
 */
public class FormatMetadata {
    String filename;
    String format_name;
    String format_long_name;
    Double duration;
    Long size;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFormat_name() {
        return format_name;
    }

    public void setFormat_name(String format_name) {
        this.format_name = format_name;
    }

    public String getFormat_long_name() {
        return format_long_name;
    }

    public void setFormat_long_name(String format_long_name) {
        this.format_long_name = format_long_name;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
