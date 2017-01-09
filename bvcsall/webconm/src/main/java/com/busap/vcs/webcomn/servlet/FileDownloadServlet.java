package com.busap.vcs.webcomn.servlet;


import java.io.File;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.busap.vcs.util.FileUtils;

/**
 * Created by
 * User: djyin
 * Date: 12/26/13
 * Time: 11:03 AM
 * 配合fileservice使用
 */
public class FileDownloadServlet extends AbstractFileDownloadServlet  {

	private Logger logger = LoggerFactory.getLogger(FileDownloadServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
        ctx.getAutowireCapableBeanFactory().autowireBean(this);
        if (basePath.endsWith("${files.localpath}")) {
            //throw new RuntimeException("miss configuration ${files.localpath}");
            basePath = "." + File.separator + "files";
        }
        if (!basePath.endsWith("/")) {
            basePath = basePath + "/";
        }
    }

    /**
     * The Base path.
     */
    @Value("${files.localpath}")
    String basePath = "." + File.separator + "files";

    /**
     * Gets file.自行实现FileId到文件的匹配关系
     *
     * @param fileId the file id
     * @return the file
     */
    public File getFile(String fileId) {
        String path = FileUtils.formatFile(fileId);
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String fullPath = basePath + path; 
//        logger.info("fullPath======================="+fullPath);
        return new File(fullPath);
    }

}

