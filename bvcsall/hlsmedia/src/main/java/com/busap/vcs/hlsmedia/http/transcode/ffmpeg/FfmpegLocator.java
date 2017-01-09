package com.busap.vcs.hlsmedia.http.transcode.ffmpeg;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

/**
 * 协助拼接ffmpeg的命令
 *
 * @author djyin
 */
public class FfmpegLocator {
    /**
     * FFMpeg文件所在的目录
     */
    static String[] trylist = new String[]{
            "./dependency/%s/ffmpeg/bin",
            "./src/main/assembly/%s/ffmpeg/bin"};


    /**
     * 定位 ffmpeg 所在的目录
     *
     * @return
     */
    public static String locate() {
        String location = null;
        String system = "win64_x86"; //开发环境
        String aff = aff();
        if (SystemUtils.IS_OS_LINUX) {
            system = "linux64_x86";
        } else if (SystemUtils.IS_OS_MAC_OSX) {
            system = "macosx";
        }
        for (String loc : trylist) {
            location = String.format(loc, system);
            String execpath = location
                    + File.separator + "ffmpeg" + aff;
            if (new File(execpath).canExecute()) {
                break;
            } else if (new File(execpath).canRead()) {
                throw new IllegalAccessError(execpath + " can't be execute.");
            } else {
                location = null;
            }
        }
        return location;
    }

    public static String aff() {
        String aff = ".exe";
        if (SystemUtils.IS_OS_LINUX) {
            aff = "";
        } else if (SystemUtils.IS_OS_MAC_OSX) {
            aff = "";
        }
        return aff;
    }

}
