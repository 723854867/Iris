package com.busap.vcs.hlsmedia.http.transcode;

import com.alibaba.fastjson.JSON;
import com.busap.vcs.hlsmedia.http.hls.Video;
import com.busap.vcs.hlsmedia.http.hls.Video.STATUS;
import com.busap.vcs.hlsmedia.http.transcode.ffmpeg.Ffprobe;
import com.busap.vcs.hlsmedia.http.transcode.ffmpeg.FfprobeMetadata;
import com.busap.vcs.hlsmedia.http.transcode.ffmpeg.StreamMetadata;
import com.spnetty.server.util.SettingsUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 获取视频的基本洗洗
 */

public class MetadataTranscode extends Transcode {

    private static final Logger log = LoggerFactory.getLogger(MetadataTranscode.class);
    static Ffprobe cmd = new Ffprobe();

    public MetadataTranscode(Video video, TranscodeResultHandler handler) {
        super(video, handler);
    }

    @Override
    public Shellable shellable() {
        return cmd;
    }

    @Override
    public String argsTemplate() {
        return Ffprobe.ARGS_PRINT_JSON_INFO;
    }

    @Override
    public Object[] args() {
        final String input = video.getFilepath();
        Object[] args = {
                input
        };
        return args;
    }

    @Override
    public ShellableExecutorResultHandler shellableExecutorResultHandler() {
        return new ShellableExecutorResultHandler() {
            @Override
            public void onProcessComplete(int exitValue, String out, String err) {
                FfprobeMetadata metadata = JSON.parseObject(out, FfprobeMetadata.class);
                if (metadata.getFormat() != null && metadata.getStreams() != null && metadata.getStreams().size() > 1) {
                    Double duration = metadata.getFormat().getDuration();
                    video.setDuration(duration);
                    String codec = "";
                    for (StreamMetadata streamMetadata : metadata.getStreams()) {
                        if (streamMetadata.getCodec_type().equalsIgnoreCase("video")) {
                            video.setH(streamMetadata.getHeight());
                            video.setW(streamMetadata.getWidth());
                            codec = streamMetadata.getCodec_name() + codec;
                        } else if (streamMetadata.getCodec_type().equalsIgnoreCase("audio")) {
                            codec = codec + "-" + streamMetadata.getCodec_name();
                        }
                    }
                    video.setCodec(codec);
                    video.setStatus(STATUS.meta_ok);
                } else {
                    video.setStatus(STATUS.meta_err);
                    video.setError(String.format("fail to get meta data from file %s, error is %s", video.getTitle(), err));
                    return;
                }


                String filename = video.getFilepath();
                String baseName = FilenameUtils.getBaseName(filename);
                String ext = FilenameUtils.getExtension(filename);
                String noext = filename.substring(0, filename.length() - ext.length() - 1);

                Boolean enabled = SettingsUtils.getProperty("hls.test.nfo", Boolean.class);
                if (enabled != null && enabled) {
                    // 输出nfo页面
                    String nfo = JSON.toJSONString(video);
                    try {
                        File f = new File(noext + ".nfo");
                        FileUtils.write(f, nfo, false);
                        log.info(String.format("generate nfo  %s", f.getPath()));
                    } catch (IOException e) {
                        log.error(String.format("fail to generate nfo for file %s", video.getFilepath()), e);
                        // ignored
                    }
                }
                Boolean hps = SettingsUtils.getProperty("hls.test.hps", Boolean.class);
                if (hps != null && hps) {
                    // 输出播放页面 HPS
                    if (ext.equalsIgnoreCase("mp4") || ext.equalsIgnoreCase("flv")) {
                        String html = "<video width='%s' height='%s' poster='%s' autoplay controls autobuffer> " +
                                "<source src='%s' type='video/mp4' />" +
                                "Your browser does not support HTML5/video </video>";
                        String htmlOut = String.format(html, video.getW(), video.getH(), baseName + ".jpg", video.getUuid() + ".mp4");
                        try {
                            File f = new File(noext + ".hps.html");
                            FileUtils.write(f, htmlOut, false);
                            log.info(String.format("generate hps html %s", f.getPath()));
                        } catch (IOException e) {
                            log.error(String.format("fail to generate play html(hps) for file %s", video.getFilepath()), e);
                            // ignored
                        }
                    }
                }


            }

            @Override
            public void onProcessFailed(Throwable e, String out, String err) {
                // ignored
                video.setStatus(STATUS.meta_err);
                video.setError(String.format("fail to get meta data from file %s, error is %s", video.getTitle(), err));
            }
        };
    }


}
