package com.busap.vcs.hlsmedia.http;

import java.util.ArrayList;
import java.util.List;


/**
 * 上传结果用的类
 */
public class HttpUploadResponse {
    public boolean isMultipart = false;
    public boolean isChunked = false;
    public String uri;
    public boolean isFinished = false;
    public List<HttpUploadDataFile> httpDataFileuploadDataFiles = new ArrayList<HttpUploadDataFile>();
    public String exception = "";
}
