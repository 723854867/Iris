package com.busap.vcs.restadmin.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;

@Controller()
@RequestMapping("deviceConfig")
public class DeviceConfigController{

	private static final Logger logger = LoggerFactory.getLogger(DeviceConfigController.class);
	
	@Resource(name="jedisService")
	private JedisService jedisService;

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	@Value("${files.localpath}")
	private String basePath;

	@RequestMapping("info")
	public String info(HttpServletRequest req) {
		req.setAttribute("version", jedisService.get(BicycleConstants.DEVICE_CONFIG_VERSION));
		req.setAttribute("url", jedisService.get(BicycleConstants.DEVICE_CONFIG_URL));
		
		return "deviceConfig/info";
	}
	
	@RequestMapping("uploadConfig")
	@ResponseBody
	public RespBody uploadConfig(MultipartFile configFile){
		String fileName = configFile.getOriginalFilename();
    	String relPath = File.separator+"deviceConfig"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss")+File.separator;
        try { 
        	FileUtils.copyInputStreamToFile(configFile.getInputStream(), new File(basePath+relPath, fileName));
        	String url = relPath+fileName;
            return this.respBodyWriter.toSuccess(url); 
        } catch (IOException e) {
        	logger.error("文件[" + fileName + "]上传失败",e);
        	return this.respBodyWriter.toError(""); 
        }
	}
	
	@RequestMapping("save")
	@ResponseBody
	public RespBody save(String version,String url){
		jedisService.set(BicycleConstants.DEVICE_CONFIG_VERSION, version.trim());
    	jedisService.set(BicycleConstants.DEVICE_CONFIG_URL, url.trim());
        return this.respBodyWriter.toSuccess(1); 
	}
	
}
