package com.busap.vcs.restadmin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.data.model.TemplateDisplay;
import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.data.entity.Template;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.TemplateService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;

/**
 * 片头管理
 * 
 * @author linghqi.kong
 *
 */
@Controller()
@RequestMapping("template")
public class TemplateController extends CRUDController<Template, Long> {
	
	private static final Logger logger = LoggerFactory.getLogger(TemplateController.class);

	@Resource(name = "templateService")
	private TemplateService templateService;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name = "activityService")
	private ActivityService activityService;
	
	@Value("${files.localpath}")
	private String basePath;

	@Resource(name = "templateService")
	@Override
	public void setBaseService(BaseService<Template, Long> baseService) {
		this.baseService = baseService;
	}

	@RequestMapping("/initPage")
	public String initPage(){
		List aList = this.activityService.findAll();
		this.request.setAttribute("activityList", aList);
		return "template/list";
	}

	@RequestMapping("/queryInitPageList")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryInitPageList(@ModelAttribute("queryPage")JQueryPage queryPage,
												@RequestParam(value = "type",required = false) Integer type,
												@RequestParam(value = "actId",required = false) Long actId,
												@RequestParam(value = "title",required = false) String title){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("actId",actId);
		params.put("title",title);
		params.put("type",type);
		List<TemplateDisplay> list = templateService.queryTemplates(params);
		com.busap.vcs.util.page.Page page = PagingContextHolder.getPage();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total",page.getTotalResult());
		map.put("rows",list);
		return map;
	}

	@RequestMapping("/getresult")
	@ResponseBody
	public Map getresult(){
		Sort sort=new Sort(Direction.ASC,"orderNum");
        Page pinfo=(Page)this.listPage(1, 1000, new ArrayList(), sort).getResult();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
	}
	
	@RequestMapping("add")
	public String add(@RequestParam MultipartFile[] files,Template temp,HttpServletResponse resp) {
		if(files.length == 3){
			uploadHeadPic(files,temp);//上传片头与片头封面
			temp.setCreatorId(U.getUid());
			this.create(temp);
		}
		return "redirect:/template/initPage";
	}
	
	@RequestMapping("deleteTemp")
	@ResponseBody
	public RespBody deleteTemplate(long id){
		Template temp = this.templateService.findOne(id);
		String zipPath = temp.getZipUrl();
		String picPath = temp.getPic();
		if(StringUtils.isNotBlank(zipPath)){
			clearFiles(zipPath);
		}
		if(StringUtils.isNotBlank(picPath)){
			clearFiles(picPath);
		}
		this.delete(id);
		return respBodyWriter.toSuccess();
	}
	

	@RequestMapping("updateTemp")
	public String updateTemplate(@RequestParam MultipartFile[] files,Template temp,HttpServletResponse resp){
		if(files.length > 0){
			uploadHeadPic(files,temp);//上传片头与片头封面
		}
		Template template = this.templateService.findOne(temp.getId());
		String zipPath = template.getZipUrl();
		String headPath = template.getPic();
		String backgroundPath = template.getBackgroundPic();
		if(StringUtils.isNotBlank(zipPath) && files[0].getSize() > 0){
			clearFiles(zipPath);
		}
		if(StringUtils.isNotBlank(headPath) && files[1].getSize() > 0){
			clearFiles(headPath);
		}
		if(StringUtils.isNotBlank(backgroundPath) && files[2].getSize() > 0){
			clearFiles(backgroundPath);
		}
		if(StringUtils.isBlank(temp.getPic())){
			temp.setPic(template.getPic());
		}
		if(StringUtils.isBlank(temp.getZipUrl())){
			temp.setZipUrl(template.getZipUrl());
		}
		if(StringUtils.isBlank(temp.getBackgroundPic())){
			temp.setBackgroundPic(template.getBackgroundPic());
		}
		if(StringUtils.isBlank(temp.getVersionNum())){
			temp.setVersionNum(template.getVersionNum());
		}
		temp.setCreatorId(template.getCreatorId());
		this.update(temp);
		return "redirect:/template/initPage";
	}
	
	/**
	 * 生成空的xml
	 * @param path
	 */
	private void genXML(String path){
		Document dom = DocumentHelper.createDocument();
		dom.setXMLEncoding("gb2312");
		String xml = dom.asXML();
		File file = new File(basePath + path);
		try {
			PrintWriter pw;
			pw = new PrintWriter(file);
			pw.write(xml);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * xml文件+head.mp4=zip
	 * @param zipPath
	 * @param mp4Path
	 * @param xmlPath
	 * @return
	 * @throws IOException
	 */
	private String genZIP(String zipPath,String mp4Path,String xmlPath) throws IOException{
		String zipName = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_");
		File zipFile = new File(basePath + zipPath + zipName + ".zip");
		if(!zipFile.exists())
			zipFile.createNewFile();
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
		byte[] buffer = new byte[1024];
		File mp4File = new File(basePath + zipPath + mp4Path);
		if(mp4File.exists()){
			InputStream is = new FileInputStream(mp4File);
			zos.putNextEntry(new ZipEntry(mp4File.getName()));
			int len=0;
			while((len = is.read(buffer))>0) {
				zos.write(buffer,0,len);
			}
			zos.closeEntry();
			is.close();
		}
		File xmlFile = new File(basePath + xmlPath);
		if(xmlFile.exists()){
			InputStream is = new FileInputStream(xmlFile);
			zos.putNextEntry(new ZipEntry(xmlFile.getName()));
			int len=0;
			while((len = is.read(buffer))>0) {
				zos.write(buffer,0,len);
			}
			zos.closeEntry();
			is.close();
		}
		zos.close();
		return zipPath + zipName + ".zip";
	}
	
	/**
	 * [上传片头及封面]
	 * @param files
	 * @param temp
	 */
	private void uploadHeadPic(@RequestParam MultipartFile[] files,Template temp){
		//片头mp4
		if(files[0].getSize() > 0){
			String fileName = files[0].getOriginalFilename();
			String sufix = fileName.substring(fileName.lastIndexOf("."));
			String mp4Path = File.separator + "template" + File.separator + "zip" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
			String xmlPath = mp4Path + "config.xml";
			String mp4Filename = "head" + sufix;
			String zipUrl = "";
			if(temp!=null&&(temp.getType()==2||temp.getType()==3)) {
				String zipName = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_");
				zipUrl=basePath + mp4Path+zipName+".zip";
				try {
					FileUtils.copyInputStreamToFile(files[0].getInputStream(), new File(zipUrl));
					temp.setZipUrl(mp4Path+zipName+".zip");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
			try {
				FileUtils.copyInputStreamToFile(files[0].getInputStream(), new File(basePath + mp4Path, mp4Filename));
				//生成一个空的config.xml
				genXML(xmlPath);
				//打成压缩包zip
				zipUrl = genZIP(mp4Path,mp4Filename,xmlPath);
				//删除源文件
				File mp4File = new File(basePath + mp4Path + mp4Filename);
				if(mp4File.exists())//删除MP4
					mp4File.delete();
				File xmlFile = new File(basePath + xmlPath);
				if(xmlFile.exists())//删除xml
					xmlFile.delete();
				logger.info(zipUrl);
				temp.setZipUrl(zipUrl);//片头文件存储路径
			} catch (Exception e) {
				logger.error("文件[" + mp4Filename + "]上传失败",e);
				e.printStackTrace();
			}
			}
		}
		//封面pic
		if(files[1].getSize() > 0){
			String fileName = files[1].getOriginalFilename();
			String sufix = fileName.substring(fileName.lastIndexOf("."));
			String picPath = File.separator + "Template" + File.separator + "pic" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
			String picFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;
			String picUrl = "";
	        try {
	        	FileUtils.copyInputStreamToFile(files[1].getInputStream(), new File(basePath + picPath, picFilename));
	            picUrl = picPath + picFilename;
	            logger.info(picUrl);
	            temp.setPic(picUrl);//片头封面存储路径
	        } catch (IOException e) {
	        	logger.error("文件[" + picFilename + "]上传失败",e);
	        	e.printStackTrace();
	        }
		}
		//背景pic
		if(files[2].getSize() > 0){
			String fileName = files[2].getOriginalFilename();
			String sufix = fileName.substring(fileName.lastIndexOf("."));
			String picPath = File.separator + "Template" + File.separator + "backgroundpic" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
			String picFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;
			String picUrl = "";
	        try {
	        	FileUtils.copyInputStreamToFile(files[2].getInputStream(), new File(basePath + picPath, picFilename));
	            picUrl = picPath + picFilename;
	            logger.info(picUrl);
	            temp.setBackgroundPic(picUrl);//片头背景图片存储路径
	        } catch (IOException e) {
	        	logger.error("文件[" + picFilename + "]上传失败",e);
	        	e.printStackTrace();
	        }
		}
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

}
