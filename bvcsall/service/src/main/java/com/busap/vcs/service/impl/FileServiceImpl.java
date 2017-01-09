package com.busap.vcs.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.busap.vcs.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("fileServiceImpl")
public class FileServiceImpl implements FileService {

    /**
     * 默认上传到当前用户目录
     */
    public static String DEFAULT_PATH = "." + File.separator + "files";


    // 上传文件保存路径
    @Value("${files.localpath}")
    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public FileServiceImpl() {
        this.path = DEFAULT_PATH;
    }


    public String newname(String filename) {
        String ext = FilenameUtils.getExtension(filename);
        String uuid = UUID.randomUUID().toString();
        String rawpath = uuid.replace("-", File.separator);
        String newname = getPath() + File.separatorChar + rawpath + "." + ext;
        return newname;
    }

    public String basename(String filename) {
        String name = filename;
        if (name.startsWith(getPath())) {
            name = name.substring(getPath().length());
        }
        return name;
    }

    public String put(File file) throws IOException {
        String newname = newname(file.getPath());
        FileUtils.moveFile(file, new File(newname));
        return newname.substring(newname.indexOf(File.separator, 2), newname.length());
    }


    // 测试
    public static void main(String[] args) throws Exception {
        FileServiceImpl service = new FileServiceImpl();
        File file = new File("C:/Users/Administrator/Desktop/log.txt");
        String file_path = service.put(file);
        System.out.println(file_path);
    }

}
