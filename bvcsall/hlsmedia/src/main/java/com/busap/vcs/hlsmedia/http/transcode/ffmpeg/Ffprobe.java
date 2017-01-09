package com.busap.vcs.hlsmedia.http.transcode.ffmpeg;

import com.busap.vcs.hlsmedia.http.transcode.Shellable;

import java.io.File;

/**
 * Created by djyin on 8/6/2014.
 */
public class Ffprobe implements Shellable {

    public static final String exec;

    static {
        String loc = FfmpegLocator.locate();
        String aff = FfmpegLocator.aff();
        if (loc != null) {
            exec = loc + File.separator + "ffprobe" + aff;
        } else {
            exec = null;
        }
    }

    @Override
    public String toString() {
        return exec;
    }

    /**
     * ffprobe -v quiet -print_format json -show_format -show_streams  D:\busap\maishi\develop\trunk\bvcs\hlsmedia\trailer2_1280.mp4
     */
    public static final String ARGS_PRINT_JSON_INFO = " -v quiet -print_format json -show_format -show_streams %s";

}
