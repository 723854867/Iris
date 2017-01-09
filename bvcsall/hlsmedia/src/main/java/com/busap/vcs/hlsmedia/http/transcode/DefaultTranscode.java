package com.busap.vcs.hlsmedia.http.transcode;

import com.busap.vcs.hlsmedia.http.hls.Video;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by djyin on 8/6/2014.
 * 串行执行获取媒体信息,转码,截图
 */
public class DefaultTranscode extends Transcode {

    private static final Logger log = LoggerFactory.getLogger(MetadataTranscode.class);

    public DefaultTranscode(Video video, TranscodeResultHandler handler) {
        super(video, handler);
    }

    @Override
    public Shellable shellable() {
        return null;
    }

    @Override
    public String argsTemplate() {
        return null;
    }

    @Override
    public Object[] args() {
        return new Object[0];
    }

    @Override
    public ShellableExecutorResultHandler shellableExecutorResultHandler() {
        return null;
    }
    @Override
    public void transcode(final ShellableExecutor executor) {

        final TranscodeResultHandler onH264Mp4 = new TranscodeResultHandler() {
            @Override
            public void onProcessComplete(Video video) {
				log.info(String.format("transcode finished [mp4-->hls] %s",
						video.getFilepath()));
                handler.onProcessComplete(video);
            }

            @Override
            public void onProcessFailed(Video video, Throwable e) {
                handler.onProcessFailed(video, e);
            }
        };
        final TranscodeResultHandler onCapture = new TranscodeResultHandler() {
            @Override
            public void onProcessComplete(Video video) {
            	log.info(String.format("transcode finished [capture] %s",video.getFilepath()));
                H264Mp4HlsTranscode transcode = new H264Mp4HlsTranscode(video,onH264Mp4);
                transcode.transcode(executor);
            }

            @Override
            public void onProcessFailed(Video video, Throwable e) {
                handler.onProcessFailed(video, e);
            }
        };
        final TranscodeResultHandler onGetMeta = new TranscodeResultHandler() {
            @Override
            public void onProcessComplete(Video video) {
            	log.info(String.format("transcode finished [metadata] %s",video.getFilepath()));
                CaptureTranscode transcode = new CaptureTranscode(video,onCapture);
                transcode.transcode(executor);
            }

            @Override
            public void onProcessFailed(Video video, Throwable e) {
                handler.onProcessFailed(video, e);
            }
        };
        MetadataTranscode transcode = new MetadataTranscode(video,onGetMeta);
        transcode.transcode(executor);
    }
}
