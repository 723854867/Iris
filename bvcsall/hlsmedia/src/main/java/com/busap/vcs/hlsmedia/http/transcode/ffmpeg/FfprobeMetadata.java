package com.busap.vcs.hlsmedia.http.transcode.ffmpeg;

import java.util.List;

/**
 * Created by djyin on 8/6/2014.
 */
public class FfprobeMetadata {
    private FormatMetadata format;
    private List<StreamMetadata> streams;

    public List<StreamMetadata> getStreams() {
        return streams;
    }

    public void setStreams(List<StreamMetadata> stream) {
        this.streams = stream;
    }

    public FormatMetadata getFormat() {
        return format;
    }

    public void setFormat(FormatMetadata format) {
        this.format = format;
    }
}
