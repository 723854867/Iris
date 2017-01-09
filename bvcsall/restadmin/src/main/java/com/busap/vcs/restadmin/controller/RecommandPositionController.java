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
import com.busap.vcs.data.entity.RecommandPosition;
import com.busap.vcs.data.entity.Tag;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.HotLabelService;
import com.busap.vcs.service.RecommandPositionService;
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
@RequestMapping("recommandPosition")
public class RecommandPositionController extends CRUDController<RecommandPosition, Long> {
	
	private static final Logger logger = LoggerFactory.getLogger(RecommandPositionController.class);

	@Resource(name = "recommandPositionService")
	private RecommandPositionService recommandPositionService;
	
	@Value("${files.localpath}")
	private String basePath;
	
	@Resource(name = "activityService")
	private ActivityService activityService;
	
	@Resource(name = "tagService")
	private TagService tagService;
	@Resource(name = "hotLabelService")
	private HotLabelService hotLabelService;

	@Resource(name = "recommandPositionService")
	@Override
	public void setBaseService(BaseService<RecommandPosition, Long> baseService) {
		this.baseService = baseService;
	}

	@RequestMapping("/initPage")
	public String initPage(){
		List<Activity> activityList = this.activityService.findAll();
		List<Tag> tagList = this.tagService.findAll();
		this.request.setAttribute("activites", activityList);
		this.request.setAttribute("tags", tagList);
		this.request.setAttribute("labels", hotLabelService.queryHotLabels(null));
		this.request.setAttribute("picBasePath", basePath);
		return "recommandPosition/list";
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
	public String add(@RequestParam MultipartFile file,RecommandPosition temp,HttpServletResponse resp) {
			uploadHeadPic(file,temp);//上传开机大图
			temp.setCreatorId(U.getUid());
			temp.setStatus(1);//默认生效，不过要在有效期内才可以使用
			this.create(temp);
		
		return "redirect:/recommandPosition/initPage";
	}
	
	@RequestMapping("deleteTemp")
	@ResponseBody
	public RespBody deleteRecommandPosition(long id){
		RecommandPosition temp = this.recommandPositionService.find(id);
		clearFiles(temp.getPicPath());//清除所有相关图片
		this.delete(id);
		return respBodyWriter.toSuccess();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("updateTemp")
	public String updateRecommandPosition(@RequestParam MultipartFile file,RecommandPosition temp){
		RecommandPosition recPosition = this.recommandPositionService.find(temp.getId());
		String basePath = recPosition.getPicPath();
		
		clearFiles(recPosition.getPicPath());
		
		temp.setPicPath(basePath);
		temp.setStatus(recPosition.getStatus());
		uploadHeadPic(file,temp);//上传图片
		
		this.update(temp);
		return "redirect:/recommandPosition/initPage";
	}
	//一键失效
	@RequestMapping("invalidAll")
	@ResponseBody
	public RespBody Invalid(String id){
		String ids[] = id.split(",");
		for(String i:ids){
			RecommandPosition advImage = this.recommandPositionService.find(Long.parseLong(i));
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
	private void uploadHeadPic(@RequestParam MultipartFile file,RecommandPosition temp){
		String picPath = "";
		if(temp.getPicPath() == null){//新增
			picPath = File.separator + "recommandPosition" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator + System.currentTimeMillis() + ".jpg";
		}else{//修改
			picPath = temp.getPicPath();
		}
		
		String picFilename = picPath.substring(picPath.lastIndexOf(File.separator), picPath.length()-1);
		String picUrl = picPath + picFilename;
		
        try {
        	FileUtils.copyInputStreamToFile(file.getInputStream(), new File(basePath + picPath, picFilename));
           
            logger.info(picUrl);
        } catch (IOException e) {
        	logger.error("文件[" + picFilename + "]上传失败",e);
        	e.printStackTrace();
        }
			
		temp.setPicPath(picUrl);//开机大图存储基础路径
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
	
	//停用启用状态
	@RequestMapping("settingStatus")
	@ResponseBody
	public RespBody settingStatus(Long id,int status){
		RecommandPosition advImage = this.recommandPositionService.find(id);
		advImage.setStatus(status);
		this.update(advImage);
		return this.respBodyWriter.toSuccess();
	}

}
