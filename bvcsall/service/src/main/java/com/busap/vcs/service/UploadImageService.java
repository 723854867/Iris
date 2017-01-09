package com.busap.vcs.service;


import java.io.InputStream;

/**
 *  Created by Yangxinyu on 15/9/16.
 */

public interface UploadImageService {
    public String upload(long fileSize, InputStream inputStream);
}
