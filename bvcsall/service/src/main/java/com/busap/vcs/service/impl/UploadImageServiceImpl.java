package com.busap.vcs.service.impl;

import com.busap.vcs.service.UploadImageService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 *  Created by Yangxinyu on 15/9/16.
 */
@Service("uploadImageService")
public class UploadImageServiceImpl implements UploadImageService {

    private static final Logger logger = LoggerFactory.getLogger(UploadImageServiceImpl.class);

    @Value("${files.localpath}")
    private String basePath;

    @Override
    public String upload(long fileSize, InputStream inputStream) {

        String picPath = File.separator + "MessageImage" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd")
                + File.separator + System.currentTimeMillis()  + File.separator;
        String suffix = ".jpg";
        if(fileSize > 0){
            String picFilename = fileSize + suffix;
            try {
                FileUtils.copyInputStreamToFile(inputStream, new File(basePath + picPath, picFilename));
                picPath = picPath + picFilename;
                logger.info(picPath);
            } catch (IOException e) {
                logger.error("文件[" + picFilename + "]上传失败",e);
                e.printStackTrace();
            }
        }
        return picPath;
    }

}
