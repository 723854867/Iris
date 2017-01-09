package com.busap.vcs.restadmin.controller;

import java.io.File;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.data.entity.InviteInfo;
import com.busap.vcs.data.entity.User;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.InviteInfoService;
import com.busap.vcs.service.UserService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller
@RequestMapping("inviteinfo")
public class InviteInfoController extends CRUDController<InviteInfo,Long> {
	
	private static final Logger logger = LoggerFactory.getLogger(InviteInfoController.class);
	
	@Value("${files.localpath}")
	private String basePath;
	
	@Resource(name = "inviteInfoService")
	private InviteInfoService inviteInfoService;
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name="inviteInfoService")
	@Override
	public void setBaseService(BaseService<InviteInfo,Long> baseService) {
		this.baseService=baseService;
		
	}
	@RequestMapping("modify")
	public String updateInviteInfo(@RequestParam MultipartFile file,@ModelAttribute InviteInfo inviteInfo){
		if(file!=null&&!file.isEmpty()){
			uploadInviteInfo(file,inviteInfo);
		}
		Date date=new Date();
		inviteInfo.setCreateDate(date);
		inviteInfo.setModifyDate(date);
		inviteInfo.setModifyDate(new Date());
		Long creatorId = inviteInfo.getCreatorId();
		if(creatorId!=null&&creatorId>0){
			User user = userService.find(creatorId);
			inviteInfo.setModifyName(user.getUsername());
		}
	
		if(inviteInfo.getId()!=null&&inviteInfo.getId()>0){
			InviteInfo	inviteInfoOld=inviteInfoService.find(inviteInfo.getId());
			if(inviteInfoOld!=null&&inviteInfoOld.getPicPath()!=null){
				if(inviteInfo.getPicPath()==null||inviteInfo.getPicPath().isEmpty()&&!inviteInfoOld.getPicPath().isEmpty()){
					inviteInfo.setPicPath(inviteInfoOld.getPicPath());
				}
				inviteInfo.setCreateDate(inviteInfoOld.getCreateDateStr());
			}

		}
		inviteInfoService.update(inviteInfo);
		return "inviteinfo/list";
	}
	
	@RequestMapping("add")
	public String addInviteInfo(@RequestParam MultipartFile file,@ModelAttribute InviteInfo inviteInfo){
		if(file!=null&&!file.isEmpty()){
			uploadInviteInfo(file,inviteInfo);
		}
		Date date=new Date();
		inviteInfo.setCreateDate(date);
		inviteInfo.setModifyDate(date);
		Long creatorId = inviteInfo.getCreatorId();
		if(creatorId!=null&&creatorId>0){
			User user = userService.find(creatorId);
			inviteInfo.setModifyName(user.getUsername());
		}
	
		if(inviteInfo.getId()!=null&&inviteInfo.getId()>0){
			InviteInfo	inviteInfoOld=inviteInfoService.find(inviteInfo.getId());
			if(inviteInfoOld!=null&&inviteInfoOld.getPicPath()!=null){
				if(inviteInfo.getPicPath()==null||inviteInfo.getPicPath().isEmpty()&&!inviteInfoOld.getPicPath().isEmpty()){
					inviteInfo.setPicPath(inviteInfoOld.getPicPath());
				}
				inviteInfo.setCreateDate(inviteInfoOld.getCreateDateStr());
			}

		}
		inviteInfoService.save(inviteInfo);
		return "inviteinfo/list";
	}

	@RequestMapping("inviteinfolist")
	public String list(){
		return "inviteinfo/list";
	}
	
	
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public RespBody deleteBanner(Long id){
		inviteInfoService.delete(id);
		return this.respBodyWriter.toSuccess();
	}
	/**
	 * 上传InviteInfo图
	 * @param files
	 * @param inviteInfo
	 */
	private void uploadInviteInfo(@RequestParam MultipartFile files,InviteInfo inviteInfo){
		String fileName = files.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
		String dataStr= DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		String zipPath = File.separator + "inviteinfo" + File.separator + dataStr + File.separator;
		String saveZipPath = "/" + "inviteinfo" + "/" + dataStr + "/";
		String zipFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss") + sufix;
		String zipUrl = "";
		try {
			File mFile = new File(basePath + zipPath, zipFilename);
			FileUtils.copyInputStreamToFile(files.getInputStream(), mFile);
			zipUrl = saveZipPath + zipFilename;
			inviteInfo.setPicPath(zipUrl);//模板文件存储路径
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
