package com.busap.vcs.hlsmedia.http.transcode;

import com.busap.vcs.hlsmedia.http.hls.Video;
import com.busap.vcs.hlsmedia.http.hls.Video.STATUS;
import com.busap.vcs.hlsmedia.http.transcode.ffmpeg.Ffmpeg;
import com.spnetty.server.util.SimpleSpringCtxLaunchUtils;

import io.netty.util.internal.ThreadLocalRandom;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 对视频进行截图
 */

public class CaptureTranscode extends Transcode {

    static Ffmpeg cmd = new Ffmpeg();

    public CaptureTranscode(Video video, TranscodeResultHandler handler) {
        super(video, handler);
    }

    public Shellable shellable() {
        return cmd;
    }

    public String argsTemplate() {
        return Ffmpeg.ARGS_CAPTURE;
    }

    public Object[] args() {
        final String input = video.getFilepath();
        String ext = FilenameUtils.getExtension(input);
        String noext = input.substring(0, input.length() - ext.length() - 1);
        String jpg = noext + ".jpg";
        Integer second = ThreadLocalRandom.current().nextInt(1, 10);
        Object[] args = {
                second,
                input, jpg
        };
        return args;
    }

    public ShellableExecutorResultHandler shellableExecutorResultHandler() {
        return new ShellableExecutorResultHandler() {
            @Override
            public void onProcessComplete(int exitValue, String out, String err) {
            	video.setStatus(STATUS.capture_ok);
            }
            @Override
            public void onProcessFailed(Throwable e, String out, String err) {
                //ignored
            	video.setStatus(STATUS.capture_err);
            	video.setError(String.format("fail to capture file %s, error is %s", video.getTitle(), err));
            }
        };
    }


}
