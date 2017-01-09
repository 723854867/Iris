package com.busap.vcs.hlsmedia.http.transcode;

import com.busap.vcs.hlsmedia.http.hls.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by djyin on 8/8/2014.
 */
public abstract class Transcode {
    private static final Logger log = LoggerFactory.getLogger(MetadataTranscode.class);
    final Video video;
    final TranscodeResultHandler handler;

    public Transcode(Video video, TranscodeResultHandler handler) {
        this.video = video;
        this.handler = handler;
    }

    public abstract Shellable shellable();

    public abstract String argsTemplate();

    public abstract Object[] args();

    public abstract ShellableExecutorResultHandler shellableExecutorResultHandler();

    // default transcode() 方法
    public void transcode(ShellableExecutor executor) {
        // 准备数据
        final Video video = this.video;
        final TranscodeResultHandler handler = this.handler;
        final Shellable s = this.shellable();
        final String argsTempalte = this.argsTemplate();
        final Object[] args = this.args();
        final ShellableExecutorResultHandler shellableExecutorResultHandler = this.shellableExecutorResultHandler();

        try {
            executor.executor(s, new ShellableExecutorResultHandler() {
                @Override
                public void onProcessComplete(int exitValue, String out, String err) {
                    if (shellableExecutorResultHandler != null) {
                        shellableExecutorResultHandler.onProcessComplete(exitValue, out, err);
                    }
                    if (handler != null) {
                        handler.onProcessComplete(video);
                    }
                }

                @Override
                public void onProcessFailed(Throwable e, String out, String err) {
                    log.error(String.format("fail to transcode file %s", video.getFilepath()), e);
                    log.info(out);
                    log.error(err);
                    if (shellableExecutorResultHandler != null) {
                        shellableExecutorResultHandler.onProcessFailed(e, out, err);
                    }
                    if (handler != null) {
                        handler.onProcessFailed(video, e);
                    }
                }
            }, argsTempalte, args);
        } catch (IOException e) {
            log.error(String.format("fail to %s file %s", s, video.getFilepath()), e);
        }
    }

}
