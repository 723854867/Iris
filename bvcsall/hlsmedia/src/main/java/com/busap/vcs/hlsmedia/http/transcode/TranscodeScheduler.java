package com.busap.vcs.hlsmedia.http.transcode;

import com.busap.vcs.hlsmedia.http.hls.Video;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.spnetty.server.util.SimpleSpringCtxLaunchUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service("transcodeScheduler")
public class TranscodeScheduler {

    private static final Logger log = LoggerFactory.getLogger(TranscodeScheduler.class);
    @Resource(name = "executor")
    ThreadPoolTaskExecutor taskExecutor;

    public void execute(final Transcode transcode) throws Exception {
        try {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    ShellableExecutor executor = new ShellableExecutor();
                    transcode.transcode(executor);
                }
            });
        } catch (Exception e) {
            log.error("faile to execute transcode %", transcode.video.getUuid(), e);
            // 更新状态
            transcode.video.setStatus(Video.STATUS.exception);
            transcode.video.setError(e.getMessage());
            throw e;
        }

    }
}
