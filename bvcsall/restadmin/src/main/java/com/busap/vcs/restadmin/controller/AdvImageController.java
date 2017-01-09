package com.busap.vcs.restadmin.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.restadmin.utils.ResultData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.AdvImage;
import com.busap.vcs.data.entity.Tag;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.AdvImageService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.TagService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;

/**
 * 开机大图管理
 * 
 * @author linghqi.kong
 *
 */
@Controller()
@RequestMapping("advImage")
public class AdvImageController extends CRUDController<AdvImage, Long> {
	
	private static final Logger logger = LoggerFactory.getLogger(AdvImageController.class);

	@Resource(name = "advImageService")
	private AdvImageService advImageService;
	
	@Value("${files.localpath}")
	private String basePath;
	
	@Resource(name = "activityService")
	private ActivityService activityService;
	
	@Resource(name = "tagService")
	private TagService tagService;

	@Resource(name = "advImageService")
	@Override
	public void setBaseService(BaseService<AdvImage, Long> baseService) {
		this.baseService = baseService;
	}

	@RequestMapping("/initPage")
	public String initPage(){
		List<Activity> activityList = this.activityService.findAll();
		List<Tag> tagList = this.tagService.findAll();
		this.request.setAttribute("activites", activityList);
		this.request.setAttribute("tags", tagList);
		return "advImage/list";
	}
	
	@RequestMapping("/getresult")
	@ResponseBody
	public Map getresult(){
		Sort sort=new Sort(Direction.ASC,"startTime");
        Page pinfo=(Page)this.listPage(1, 1000, new ArrayList(), sort).getResult();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
	}
	
	@RequestMapping("add")
	public String add(@RequestParam MultipartFile[] files,@RequestParam MultipartFile[] files2,AdvImage temp,HttpServletResponse resp) {
		if(files.length == 5 && files2.length == 5){
			String[] size  = {"640x800","640x976","750x1146","1242x1898","1080x1650"};
			String[] size2  = {"640x714","640x890","750x1044","1242x1732","720x1004"};
			uploadHeadPic(files,temp,size);//上传开机大图
			uploadHeadPic(files2,temp,size2);//上传开机大图
			temp.setCreatorId(U.getUid());
			temp.setStatus(1);//默认生效，不过要在有效期内才可以使用
			this.create(temp);
		}
		return "redirect:/advImage/initPage";
	}
	
	@RequestMapping("deleteTemp")
	@ResponseBody
	public RespBody deleteAdvImage(long id){
		AdvImage temp = this.advImageService.findOne(id);
		clearFiles(temp.getImgBasePath());//清除所有相关图片
		this.delete(id);
		return respBodyWriter.toSuccess();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("updateTemp")
	public String updateAdvImage(@RequestParam MultipartFile[] files,@RequestParam MultipartFile[] files2,AdvImage temp){
		String[] size = {"640x800","640x976","750x1146","1242x1898","1080x1650"};
		String[] size2  = {"640x714","640x890","750x1044","1242x1732","720x1004"};
		AdvImage advImage = this.advImageService.findOne(temp.getId());
		String basePath = advImage.getImgBasePath();
		for (int i = 0; i < size.length; i++) {//遍历清除旧图片
			if(files[i].getSize() > 0){
				clearFiles(basePath+size[i]+".jpg");
			}
		}
		for (int i = 0; i < size2.length; i++) {//遍历清除旧图片
			if(files2[i].getSize() > 0){
				clearFiles(basePath+size2[i]+".jpg");
			}
		}
		
		if(files.length > 0){
			temp.setImgBasePath(advImage.getImgBasePath());
			uploadHeadPic(files,temp,size);//上传新开机大图新版
		}
		if(files2.length > 0){
			temp.setImgBasePath(advImage.getImgBasePath());
			uploadHeadPic(files2,temp,size2);//上传新开机大图旧版
		}
		temp.setEndTime(new Date());
		this.update(temp);
		return "redirect:/advImage/initPage";
	}
	
	@RequestMapping("Invalid")
	@ResponseBody
	public RespBody Invalid(String id){
		String ids[] = id.split(",");
		for(String i:ids){
			AdvImage advImage = this.advImageService.findOne(Long.parseLong(i));
			advImage.setStatus(0);
			this.update(advImage);
		}
		return this.respBodyWriter.toSuccess();
	}
	
	
	/**
	 * [上传开机大图]
	 * @param files
	 * @param temp
	 */
	private void uploadHeadPic(@RequestParam MultipartFile[] files,AdvImage temp,String[] size){
		String picPath = "";
		if(temp.getImgBasePath() == null){//新增
			picPath = File.separator + "AdvImage" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator + System.currentTimeMillis()  + File.separator;
		}else{//修改
			picPath = temp.getImgBasePath();
		}
		String sufix = ".jpg";
		String picFilename = "";
		String picUrl = "";
		for (int i = 0; i < size.length; i++) {
			if(files[i].getSize() > 0){
				picFilename = size[i] + sufix;
				picUrl = "";
		        try {
		        	FileUtils.copyInputStreamToFile(files[i].getInputStream(), new File(basePath + picPath, picFilename));
		            picUrl = picPath + picFilename;
		            logger.info(picUrl);
		        } catch (IOException e) {
		        	logger.error("文件[" + picFilename + "]上传失败",e);
		        	e.printStackTrace();
		        }
			}
		}
		temp.setImgBasePath(picPath);//开机大图存储基础路径
	}
	
	/**
	 * 【删除文件和目录】
	 * @param workspaceRootPath
	 */
	private void clearFiles(String workspaceRootPath) {
		File file = new File(basePath + workspaceRootPath);
		if (file.exists()) {
			deleteFile(file);
		}
	}
	
	private void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		file.delete();
	}

	@RequestMapping("settingStatus")
	@ResponseBody
	public RespBody settingStatus(Long id,int status){
		AdvImage advImage = this.advImageService.findOne(id);
		advImage.setStatus(status);
		this.update(advImage);
		return this.respBodyWriter.toSuccess();
	}

}
