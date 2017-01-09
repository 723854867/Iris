package com.busap.vcs.hlsmedia.http.transcode;

import com.busap.vcs.hlsmedia.http.hls.Video;
import org.apache.commons.exec.ExecuteException;

/**
 * Created by djyin on 8/6/2014.
 */
public interface TranscodeResultHandler {
    public void onProcessComplete(Video video);
    public void onProcessFailed(Video video, final Throwable e);
}
