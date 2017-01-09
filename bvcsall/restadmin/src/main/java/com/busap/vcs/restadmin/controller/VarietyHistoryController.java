package com.busap.vcs.restadmin.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.data.entity.VarietyHistory;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.VarietyHistoryService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;

@Controller
@RequestMapping("varietyHistory")
public class VarietyHistoryController extends CRUDController<VarietyHistory, Long> {
	@Value("${uploadfile_remote_url}")
	private String uploadfile_remote_url;
	
	@Value("${video_play_url_prefix}")
	private String video_play_url_prefix;
	
	@Value("${uploadpic_url_prefix}")
	private String uploadpic_url_prefix;
	
	@Value("${uploadpic_path}")
	private String uploadpic_path;
	
	@Value("${files.localpath}")
	private String basePath;
	
	@Resource(name = "varietyHistoryService")
	private VarietyHistoryService varietyHistoryService;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;

	@Override
	@Resource(name = "varietyHistoryService")
	public void setBaseService(BaseService<VarietyHistory, Long> baseService) {
		this.baseService = varietyHistoryService;
	}
	
	@RequestMapping("listVarietyHistory")
	public String listVarietyHistory(){
		
		this.request.setAttribute("uploadfile_remote_url", uploadfile_remote_url);
		this.request.setAttribute("video_play_url_prefix", video_play_url_prefix);
		
		return "variety/list";
	}
	
	@RequestMapping("ajaxList")
	@ResponseBody
	public Map<String,Object> ajaxList(Integer page,Integer rows){
		if(page <= 0 || page == null){
    		page=1;
    	}
		if(rows <= 0 || rows == null){
			rows=30;
		}
		
		List<VarietyHistory> result = varietyHistoryService.findAllVariety();
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(result != null){
			Integer total = result.size();
    		Integer startIndex = 0;
    		Integer toIndex = 0;
    		
    		if((page-1)*rows > total){
    			page = total/rows;
    			startIndex = page * rows;
    			toIndex = total;
    		} else if(page*rows > total) {
    			startIndex = (page-1) * rows;
    			toIndex = total;
    		} else {
    			startIndex = (page-1) * rows;
    			toIndex = page * rows;
    		}
			jsonMap.put("total", total);//total键 存放总记录数，必须的 
			jsonMap.put("rows", result.subList(startIndex, toIndex));//rows键 存放每页记录 list  
		}
	    return jsonMap;
	}
	
	@RequestMapping("addVarietyHistory")
	@ResponseBody
	public RespBody addVariety(VarietyHistory variety){
		if(variety.getUids()!=null){
			String uids = variety.getUids();
			String uidArr[] = uids.split(",");
			
			String str = null;
			Set<String> set = new HashSet<String>();
			for(String uid:uidArr){
				if(StringUtils.isNotBlank(uid)){
					if(!StringUtils.isNumeric(uid)){
						continue;
					}
					if(set.size() == 0){
						str = uid;
						set.add(uid);
					} else if(!set.contains(uid)){
						set.add(uid);
						str = str+","+uid;
					}
					
				}
			}
			if(str == null){
				return this.respBodyWriter.toError("用户列表格式不正确");
			}
			variety.setUids(str);
		}
		varietyHistoryService.addVarietyHistory(variety);
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("modifyVarietyHistory")
	@ResponseBody
	public RespBody modifyVariety(VarietyHistory variety){
		if(variety.getId()!=null){
			if(variety.getUids()!=null){
				String uids = variety.getUids();
				String uidArr[] = uids.split(",");
				
				String str = null;
				Set<String> set = new HashSet<String>();
				for(String uid:uidArr){
					if(StringUtils.isNotBlank(uid)){
						if(!StringUtils.isNumeric(uid)){
							continue;
						}
						if(set.size() == 0){
							str = uid;
							set.add(uid);
						} else if(!set.contains(uid)){
							set.add(uid);
							str = str+","+uid;
						}
						
					}
				}
				if(str == null){
					return this.respBodyWriter.toError("用户列表格式不正确");
				}
				variety.setUids(str);
			}
			varietyHistoryService.updateVarietyHistory(variety);
			return this.respBodyWriter.toSuccess();
		}
		
		return this.respBodyWriter.toError("要更新的内容不存在");
	}
	
	@RequestMapping("removeVarietyHistory")
	@ResponseBody
	public RespBody deleteVariety(Long id){
		varietyHistoryService.deleteVarietyHistory(id);
		return this.respBodyWriter.toSuccess();
	}
	
	
	@RequestMapping("uploadpic")
	@ResponseBody
	public RespBody uploadpic(MultipartFile file){
		String picpath=uploadVideoPic(file);
		return this.respBodyWriter.toSuccess(picpath);
	}

	private String uploadVideoPic(MultipartFile file){ 
		String fileName = file.getOriginalFilename();
		String sufix = fileName.substring(fileName.lastIndexOf("."));
    	String relPath = File.separator+"varietyPic"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + sufix;
        try { 
        	FileUtils.copyInputStreamToFile(file.getInputStream(), new File(basePath+relPath, originalFilename));
            return relPath+originalFilename; 
        } catch (IOException e) {
        	return "failed";
        }
    }
}
