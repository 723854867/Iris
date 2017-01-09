package com.busap.vcs.hlsmedia.http.hls;

import com.spnetty.server.util.SettingsUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 所有处理uuid和文件名相关操作的部分.
 * 目前决定采用的分布式文件系统中,可能是 Gluster+fuse 或者 Ceph, 但代码仍然以
 */
public class UUIDFilenameUtils {

    private static final AtomicInteger volumeIdx = new AtomicInteger(0);
    private static String[] volumescache = null;

    /**
     * 获取当前可以用的卷名
     *
     * @return
     */
    protected static String[] volumes() {
        String vols = SettingsUtils.getProperty("hls.mapping.volumes");
        return vols.split(",");
    }

    private static String volume() {
        // 没有初始化
        if (volumescache == null) {
            volumescache = volumes();
            if (volumescache == null) {
                volumescache = new String[]{};
            }
        }
        // 没有配置
        if (volumescache.length == 0) {
            return null;
        }
        // 用int,比用随机数消耗的小一些
        int idx = volumeIdx.addAndGet(1);
        return volumescache[idx % volumescache.length];
    }

    /**
     * @return 新的UUID
     */
    public static String newUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    /**
     * 2e3d6c3a-892a-4137-bbb6-5a063c982be8 --> 2e3d6c3a/892a/4137/bbb6
     *
     * @param uuid
     * @return
     */
    public static String toParentPath(String uuid) {
        String[] nPaths = uuid.split("-");
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < nPaths.length - 1; i++) {
            buf.append(nPaths[i]);
            if (i < nPaths.length - 2) {
                buf.append(File.separator);
            }
        }
        return buf.toString();
    }

    /**
     * 2e3d6c3a-892a-4137-bbb6-5a063c982be8 --> 2e3d6c3a-892a-4137-bbb6
     *
     * @param uuid
     * @return
     */
    public static String toParentUUID(String uuid) {
        return uuid.substring(0, uuid.lastIndexOf("-"));
    }

    /**
     * 2e3d6c3a-892a-4137-bbb6-5a063c982be8 --> 2e3d6c3a/892a/4137/bbb6/5a063c982be8
     *
     * @param uuid
     * @return
     */
    public static String toFullPath(String uuid) {
        String path = uuid.replace("-", "/");
        return path;
    }

    /**
     * 2e3d6c3a-892a-4137-bbb6-5a063c982be8 --> 5a063c982be8
     *
     * @param uuid
     * @return
     */
    public static String toBasename(String uuid) {
        String[] nPaths = uuid.split("-");
        String nFilename = nPaths[nPaths.length - 1];
        return nFilename;
    }

    /**
     * 2e3d6c3a-892a-4137-bbb6-5a063c982be8 --> 2e3d6c3a-892a-4137-bbb6-5a063c982be8
     *
     * @param uuid
     * @return
     */
    public static String toPlayID(String uuid) {
        return uuid;
    }

    /**
     * fullpath to uuid
     * 2e3d6c3a/892a/4137/bbb6/5a063c982be8.mp4 --> 2e3d6c3a-892a-4137-bbb6-5a063c982be8
     * /2e3d6c3a/892a/4137/bbb6/5a063c982be8/ --> 2e3d6c3a-892a-4137-bbb6-5a063c982be8
     */
    public static String toUUID(String path) {
        path = FilenameUtils.removeExtension(path);
        path = FilenameUtils.normalizeNoEndSeparator(path);
        String uuid = StringUtils.join(path.split("/"), "-");
        if (uuid.startsWith("-")) {
            uuid = uuid.substring(1);
        }
        return uuid;
    }

    public static void main(String[] args) {

        System.out.println(toParentUUID("2e3d6c3a-892a-4137-bbb6-5a063c982be8"));

        System.out.println("cc8cb4c226850.ts".replaceFirst("^cc8cb4c22685", "2e3d6c3a-892a-4137-bbb6-cc8cb4c22685"));

        for (int i = 0; i < 9; i++) {

            System.out.println(volume());
        }

    }

}
