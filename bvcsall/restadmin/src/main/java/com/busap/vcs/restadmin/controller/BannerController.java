package com.busap.vcs.restadmin.controller;

/**
 * 评论管理
 * @author dmsong
 * @createdate 2015-1-14
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.restadmin.utils.ResultData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.base.OrderByObject;
import com.busap.vcs.data.entity.Activity;
import com.busap.vcs.data.entity.Banner;
import com.busap.vcs.data.entity.RootInfo;
import com.busap.vcs.data.entity.Tag;
import com.busap.vcs.service.ActivityService;
import com.busap.vcs.service.BannerService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.TagService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;

@Controller()
@RequestMapping("banner")
public class BannerController extends CRUDController<Banner, Long>{
	@Value("${files.localpath}")
	private String basePath;
	
	@Resource(name = "bannerService")
	private BannerService bannerService;
	
	@Resource(name = "activityService")
	private ActivityService activityService;
	
	@Resource(name = "tagService")
	private TagService tagService;
	
	@Resource(name = "bannerService")
	@Override
	public void setBaseService(BaseService<Banner, Long> baseService) {
		this.baseService = baseService;		
	}
	
	@RequestMapping("listbanner")
	public String list(){
		return "banner/list";
	}
	
	@RequestMapping("newbanner")
	public String newMess(){
		List<Activity> activityList = this.activityService.findAll();
		List<Tag> tagList = this.tagService.findAll();
		this.request.setAttribute("activites", activityList);
		this.request.setAttribute("tags", tagList);
		return "banner/create";
	}
	
	@RequestMapping("modifybanner")
	public String updateBanner(Long id){
		List<Activity> activityList = this.activityService.findAll();
		List<Tag> tagList = this.tagService.findAll();
		Banner banner = this.bannerService.find(id);
		this.request.setAttribute("activites", activityList);
		this.request.setAttribute("tags", tagList);
		this.request.setAttribute("banner", banner);
		return "banner/update";
	}
	
	@RequestMapping("searchListPage")
	@ResponseBody
	public Map searchListPage(Integer page, Integer rows, CRUDForm curdForm) {
		  // 从request中获取通用查询条件
    	if(page==0){
    		page=1;
    	}
    	
    	List<RootInfo> rootList=new ArrayList<RootInfo>();
		RootInfo rf=new RootInfo();
		rf.setName("loop_banner");
		rf.setClassType(Banner.class);
		rootList.add(rf);
    	
    	Pageable pageable = new PageRequest(page == null ? 1 : page, rows == null ? 10 : rows,null);
    	
    	List<OrderByObject> orderByObjList=new ArrayList<OrderByObject>();
		OrderByObject orderByObject=new OrderByObject("loop_banner", "orderNum", OrderByObject.OrderByType.DESC.getCode());
		orderByObjList.add(orderByObject);
    	
    	Page pinfo = bannerService.findAll(pageable, rootList, new ArrayList(), new ArrayList(), null, orderByObjList);
    	        
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
        jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
        return jsonMap;
	}
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	@RequestMapping("remove")
	@ResponseBody
	public RespBody deleteBanner(Long id){
		bannerService.delete(id);
		return this.respBodyWriter.toSuccess();
	}
	
	/**
	 * 显示、隐藏
	 * @param id
	 * @return
	 */
	@RequestMapping("showable")
	@ResponseBody
	public RespBody deleteBanner(Long id,Integer show){
		Banner b = bannerService.find(id);
		b.setShowAble(show);
		bannerService.update(b);
		
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("modify")
	public String updateBanner(@RequestParam MultipartFile file,@ModelAttribute Banner banner){
		uploadBanner(file,banner);

		banner.setModifyDate(new Date());
		
		bannerService.update(banner);
		
		return "banner/list";
	}
		
	@RequestMapping("add")
	public String saveSysmess(@RequestParam MultipartFile file,@ModelAttribute Banner banner){
		uploadBanner(file,banner);
		banner.setCreatorId(U.getUid());
		banner.setCreateDate(new Date());
		
		bannerService.save(banner);
		
		return "banner/list";
	}
	
	/**
	 * 上传banner图
	 * @param files
	 * @param banner
	 */
	private void uploadBanner(@RequestParam MultipartFile files,Banner banner){
		String fileName = files.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
		String zipPath = File.separator + "music" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
		String zipFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss") + sufix;
		String zipUrl = "";
		try {
			File mFile = new File(basePath + zipPath, zipFilename);
			FileUtils.copyInputStreamToFile(files.getInputStream(), mFile);
			zipUrl = zipPath + zipFilename;
			banner.setImgSrc(zipUrl);//模板文件存储路径
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@RequestMapping("bannerSort")
	@ResponseBody
	public ResultData bannerSort(Long id, Integer type) throws Throwable{
		ResultData resultData = new ResultData();
		boolean result = bannerService.updateBannerSort(id, type);
		if(result){
			resultData.setResultCode("success");
			resultData.setResultMessage("排序成功！");
		}else{
			resultData.setResultCode("fail");
			resultData.setResultMessage("排序失败！");
		}
		return resultData;
	}
	
	
	
}
