package com.busap.vcs.hlsmedia.http.transcode.ffmpeg;

import com.busap.vcs.hlsmedia.http.transcode.Shellable;

import java.io.File;

/**
 * Created by djyin on 8/6/2014.
 */
public class Ffmpeg implements Shellable {
    public static final String exec;
    static {
        String loc = FfmpegLocator.locate();
        String aff = FfmpegLocator.aff();
        if (loc != null) {
            exec = loc + File.separator + "ffmpeg" + aff;
        } else {
            exec = null;
        }
    }

    /**
     * ffmpeg -i input.mp4 -codec copy -map 0 -f segment -vbsf h264_mp4toannexb -flags -global_header -segment_format mpegts -segment_list_type m3u8 -segment_list output.m3u8 -segment_time 10 output%03d.ts
     * ffmpeg -i in.mkv -codec copy -map 0 -f segment -segment_list out.list out%03d.nut
     */
    public static final String ARGS_MP4_TO_SEGMENTS = " -i %s -codec copy -map 0 -vbsf h264_mp4toannexb -flags -global_header -f segment -segment_list %s -segment_list_flags live -segment_list_type hls -segment_time %s %s%%03d.ts";

    /**
     * -i %s -codec copy -map 0 -vbsf h264_mp4toannexb -flags -global_header -hls_time %s -hls_list_size 10000 %s
     * -hls_base_url 这个命令只有ffmpeg 2.3版本以上才支持
     * 而ffmpeg 2.3 版本对android拍摄的视频兼容性不好...
     */
    public static final String ARGS_MP4_TO_HLS = " -i %s -codec copy -map 0 -vbsf h264_mp4toannexb -flags -global_header -hls_time %s -hls_list_size 10000 %s";

    /**
     * ffmpeg -y -ss  6000 -i beijing-480p.mp4 -s 320x180 -frames 1 -f image2 result.jpg
     */
    public static final String ARGS_CAPTURE = " -y -ss %s -i %s -vframes 1 -f image2 %s";

    @Override
    public String toString() {
        return exec;
    }
}
