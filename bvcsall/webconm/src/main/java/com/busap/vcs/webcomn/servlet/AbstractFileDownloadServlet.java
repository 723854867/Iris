package com.busap.vcs.webcomn.servlet;

import com.busap.vcs.util.MimeTypes;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by
 * User: djyin
 * Date: 12/10/13
 * Time: 5:01 PM
 */
public abstract class AbstractFileDownloadServlet extends HttpServlet {

    /**
     * Gets file.自行实现FileId到文件的匹配关系
     *
     * @param fileId the file id
     * @return the file
     */
    public abstract File getFile(String fileId);



    //处理错误
    protected void doError(HttpServletResponse resp, int statusCode,
                           String message) {
        try {
            resp.sendError(statusCode, message);
        } catch (IOException e) {
            // ignored
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String fileId = req.getParameter("fileId");
        if (fileId == null || fileId.trim().length() < 1) { //尝试在路径中分析出fileId
            fileId = req.getPathInfo();
            if (fileId != null) {
                fileId = fileId.substring(1);//去掉开始的“/”
            } else {
                doError(resp, HttpServletResponse.SC_BAD_REQUEST, "MISS fileId IN REQUEST");
                return;
            }
        }
        File file = getFile(fileId);
        if (file == null || !file.canRead() || file.isDirectory()) {
            doError(resp, HttpServletResponse.SC_NOT_FOUND, "File " + fileId + " NOT FOUND");
            return;
        }
        ServletOutputStream out = null;
        InputStream in = null;
        try {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType(MimeTypes.getContentType(FilenameUtils.getExtension(file.getPath())));
            resp.setContentLength((int)file.length());
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + FilenameUtils.getName(file.getPath()) + "\"");
            out = resp.getOutputStream();
            in = new FileInputStream(file);
            IOUtils.copy(in, out);
            out.flush();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        doError(resp, HttpServletResponse.SC_NOT_IMPLEMENTED,
                "ONLY SUPPORT HTTP GET REQUEST");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        doError(resp, HttpServletResponse.SC_NOT_IMPLEMENTED,
                "ONLY SUPPORT HTTP GET REQUEST");
    }

}
