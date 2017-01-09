package com.busap.vcs.hlsmedia.http.hls;

import java.nio.charset.Charset;

/**
 * Created by djyin on 8/5/2014.
 */
public class Video {
    public enum STATUS {
        upload_ok, upload_err, // 上传成功或者失败
        meta_ok, meta_err, // 获取媒体信息成功或者失败
        capture_ok, capture_err, // 截图成功或者失败
        transcode_ok, transcode_err, // 转码成功或者失败
        unsupported_format, // 不支持的媒体格式
        exception // 处理出现异常
    }

    STATUS status;
    String error;
    String uuid;
    String title;
    String filepath; // 本地文件存放全路径
    String playuri; // 播放地址
    Double duration; // 时间长度
    String codec; // 视频音频编码格式
    String playId; //播放时使用的ID

    int w; // 分辨率的宽度
    int h; // 分辨率的高度
    float fps; // 帧率
    // 一些文件上传时的属性
    String contentTransferEncoding;
    String contentType;
    Charset charset;
    Long contentLength;

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public float getFps() {
        return fps;
    }

    public void setFps(float fps) {
        this.fps = fps;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlayuri() {
        return playuri;
    }

    public void setPlayuri(String playuri) {
        this.playuri = playuri;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPlayId() {
        return playId;
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public String getContentTransferEncoding() {
        return contentTransferEncoding;
    }

    public void setContentTransferEncoding(String contentTransferEncoding) {
        this.contentTransferEncoding = contentTransferEncoding;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }
}
