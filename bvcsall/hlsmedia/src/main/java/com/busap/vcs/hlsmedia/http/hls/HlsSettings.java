package com.busap.vcs.hlsmedia.http.hls;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by djyin on 8/4/2014.
 */
@Repository("hlsSettings")
public class HlsSettings {
    /**
     * 默认的URI地址
     */
    public static final String DEFAULT_MAPPING_LOCAL_PATH = "./files/hls/upload";
    /**
     * 默认的URI地址
     */
    public static final String DEFAULT_UPLOAD_MAPPING_URI_PATH = "/hls/upload";

    /**
     * 默认的URI地址
     */
    public static final String DEFAULT_DOWNLOAD_MAPPING_URI_PATH = "/hls/download";


    /**
     * 使用的本地目录
     */
    @Value("${hls.mapping.playuri}")
    public String playMappingURIPath;
    @Value("${hls.mapping.uploaduri}")
    public String uploadMappingURIPath;
    @Value("${hls.mapping.localpath}")
    public String mappingLocalPath;
    public HlsSettings(){
        this.uploadMappingURIPath = DEFAULT_UPLOAD_MAPPING_URI_PATH;
        this.playMappingURIPath = DEFAULT_DOWNLOAD_MAPPING_URI_PATH;
        this.mappingLocalPath = DEFAULT_MAPPING_LOCAL_PATH;
    }

    public HlsSettings(String uploadMappingURIPath, String playMappingURIPath, String mappingLocalPath) {
        if (uploadMappingURIPath == null) {
            this.uploadMappingURIPath = DEFAULT_UPLOAD_MAPPING_URI_PATH;
        } else {
            this.uploadMappingURIPath = uploadMappingURIPath;
        }
        if (playMappingURIPath == null) {
            this.playMappingURIPath = DEFAULT_DOWNLOAD_MAPPING_URI_PATH;
        } else {
            this.playMappingURIPath = playMappingURIPath;
        }
        if (mappingLocalPath == null) {
            this.mappingLocalPath = DEFAULT_MAPPING_LOCAL_PATH;
        } else {
            this.mappingLocalPath = mappingLocalPath;
        }
    }

    // callback
    // 
    public String callbackOnCapturedURI;
    public String callbackOnHLSEncodedURI;
    public String callbackOnUploaded;
}
