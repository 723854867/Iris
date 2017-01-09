package com.busap.vcs.hlsmedia.http.transcode;

import com.busap.vcs.hlsmedia.http.hls.UUIDFilenameUtils;
import com.busap.vcs.hlsmedia.http.hls.Video;
import com.busap.vcs.hlsmedia.http.hls.Video.STATUS;
import com.busap.vcs.hlsmedia.http.transcode.ffmpeg.Ffmpeg;
import com.spnetty.server.util.SettingsUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 将H264-AAC编码的MP4文件转换成HLS流
 */

public class H264Mp4HlsTranscode extends Transcode {

    private static final Logger log = LoggerFactory.getLogger(MetadataTranscode.class);
    static Ffmpeg cmd = new Ffmpeg();

    public H264Mp4HlsTranscode(Video video, TranscodeResultHandler handler) {
        super(video, handler);
    }

    @Override
    public Shellable shellable() {
        return cmd;
    }

    @Override
    public String argsTemplate() {
        return Ffmpeg.ARGS_MP4_TO_HLS;
    }

    @Override
    public Object[] args() {
        /**
         *
         * -i %s -codec copy -map 0 -vbsf h264_mp4toannexb -flags -global_header -hls_time %s -hls_list_size 10000 %s
         */
        Integer segtime = 5; // 切面按照10秒一个文件切. 修
        final String input = video.getFilepath();
        String ext = FilenameUtils.getExtension(input);
        final String noext = input.substring(0, input.length() - ext.length() - 1);
        String m3u8 = noext + ".m3u8";
        Object[] args = {
                input, segtime, m3u8};
        return args;
    }

    @Override
    public ShellableExecutorResultHandler shellableExecutorResultHandler() {
        return new ShellableExecutorResultHandler() {
            @Override
            public void onProcessComplete(int exitValue, String out, String err) {
                String uuid = video.getUuid();
                String filepath = video.getFilepath();
                String ext = FilenameUtils.getExtension(filepath);
                String noext = filepath.substring(0, filepath.length() - ext.length() - 1);
                // 处理 m3u8文件,将ts文件访问地址补全
                String name = UUIDFilenameUtils.toBasename(uuid);
                name = "^" + name;
                // TODO 移除m3u8文件的额外处理
                File f = new File(noext + ".m3u8");
                try {
                    List<String> ss = FileUtils.readLines(f, "utf-8");
                    StringBuffer buf = new StringBuffer();
                    for (int i = 0; i < ss.size(); i++) {
                        String s = ss.get(i);
                        s = s.replaceFirst(name, uuid);
                        buf.append(s);
                        buf.append("\n");
                    }
                    FileUtils.write(f, buf.toString(), false);
                    log.info(String.format("fix m3u8 with full uuid, path %s", f.getPath()));
                } catch (IOException e) {
                    log.error(String.format("fail to fix m3u8 with full uuid, path %s", video.getFilepath()), e);
                    // ignored
                }
                video.setStatus(STATUS.transcode_ok);

                // 输出html5播放页面
                Boolean testhtml5 = SettingsUtils.getProperty("hls.test.html5", Boolean.class);
                if (testhtml5 != null && testhtml5) {
                    String html = "<video width='%s' height='%s' autoplay controls autobuffer src='%s' type='application/vnd.apple.mpegurl'> Your browser does not support HTML5/video </video>";
                    String htmlOut = String.format(html, video.getW(), video.getH(), video.getUuid() + ".m3u8");
                    try {
                        f = new File(noext + ".html");
                        FileUtils.write(f, htmlOut, false);
                        log.info(String.format("generate html5 player %s", f.getPath()));
                    } catch (IOException e) {
                        log.error(String.format("fail to generate play html for file %s", video.getFilepath()), e);
                        // ignored
                    }
                }


                // 输出jw播放页面
                Boolean testflash = SettingsUtils.getProperty("hls.test.flash", Boolean.class);
                if (testflash != null && testflash) {
                    String html = "<html>\n" +
                            "<head>\n" +
                            "    <title>jwplayer</title>\n" +
                            "    <meta charset='utf-8'/>\n" +
                            "    <script type='text/javascript' src='/files/players/js/jquery-1.10.2.min.js'></script>\n" +
                            "    <script type='text/javascript' src='/files/players/js/jwplayer.js'></script>\n" +
                            "    <script type='text/javascript'>jwplayer.key = 'N8zhkmYvvRwOhz4aTGkySoEri4x+9pQwR7GHIQ=='; </script>\n" +
                            "    <script type='text/javascript'>\n" +
                            "        var _player = null;\n" +
                            "        var _url = null;\n" +
                            "        $(document).ready(function () {\n" +
                            "            var player = $('<div/>');\n" +
                            "            $(player).attr('id', 'player_id');\n" +
                            "            $('#div_container').append(player);\n" +
                            "            var conf = {\n" +
                            "                file: '%s',\n" +
                            "                image: '%s',\n" +
                            "                height: %s,\n" +
                            "                width: %s,\n" +
                            "                autostart: true,\n" +
                            "                analytics: { enabled: false}\n" +
                            "            };\n" +
                            "            _player = jwplayer('player_id').setup(conf);\n" +
                            "        });\n" +
                            "    </script>\n" +
                            "</head>\n" +
                            "<body style='text-align:center;'>\n" +
                            "<div class='container' style='margin:0px auto'>\n" +
                            "\n" +
                            "    <div id='main_modal' >\n" +
                            "        <div id='player'>\n" +
                            "            <div id='div_container'>\n" +
                            "                <div id='player_id'>载入中</div>\n" +
                            "            </div>\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "\n" +
                            "</div>\n" +
                            "</body>\n";
                    String htmlOut = String.format(html, video.getUuid() + ".m3u8", video.getUuid() + ".jpg", video.getH(), video.getW());
                    try {
                        f = new File(noext + ".jw.html");
                        FileUtils.write(f, htmlOut, false);
                        log.info(String.format("generate jwplayer html5 player %s", f.getPath()));
                    } catch (IOException e) {
                        log.error(String.format("fail to generate jwplayer html for file %s", video.getFilepath()), e);
                        // ignored
                    }
                }
                // 输出 sewiseplyer 页面
                if (testflash != null && testflash) {
                    String html = "<html>\n" +
                            "<head>\n" +
                            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n" +
                            "    <title>sewise player</title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<div style=\"width: "+video.getW()+"px;height: "+video.getH()+";\">\n" +
                            "    <script type=\"text/javascript\" src=\"/files/sewise-player/player/sewise.player.min.js\"></script>\n" +
                            "    <script type=\"text/javascript\" src=\"/files/sewise-player/player/js/jquery.min.js\"></script>\n" +
                            "    <script type=\"text/javascript\">\n" +
                            "        var lhost = window.location.host;\n" +
                            "        var lprotocol = window.location.protocol;\n" +
                            "        var id = '%s';\n" +
                            "        SewisePlayer.setup({\n" +
                            "            server: \"vod\",\n" +
                            "            type: \"m3u8\",\n" +
                            "            videourl: lprotocol+\"//\"+lhost+\"/hls/play/\"+id+\".m3u8\",\n" +
                            "            skin: \"vodWhite\",\n" +
                            "            title: \"麦视测试\",\n" +
                            "            lang: 'zh_CN',\n" +
                            "            logo: \"/files/sewise-player/player/logo.png\",\n" +
                            "            poster:lprotocol+\"//\"+lhost+\"/hls/play/\"+id+\".jpg\",\n" +
                            "            autostart: \"false\"\n" +
                            "        });\n" +
                            "    </script>\n" +
                            "</div>\n" +
                            "<div style=\"padding: 20px;float: left;\">注：如果当前设备为Mobile平台，播放器将自动启用HTML5播放模块播放m3u8文件。</div>\n" +
                            "</body>\n" +
                            "</html>";
                    String htmlOut = String.format(html,video.getUuid());
                    try {
                        f = new File(noext + ".sewise.html");
                        FileUtils.write(f, htmlOut, false);
                        log.info(String.format("generate sewise html5 player %s", f.getPath()));
                    } catch (IOException e) {
                        log.error(String.format("fail to generate sewise html5 html for file %s", video.getFilepath()), e);
                        // ignored
                    }
                }

            }

            @Override
            public void onProcessFailed(Throwable e, String out, String err) {
                // ignored
                video.setStatus(STATUS.transcode_err);
                video.setError(String.format("fail to transcode file %s, error is %s", video.getTitle(), err));
            }
        };
    }


}
