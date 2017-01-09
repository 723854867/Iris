package com.busap.vcs.restadmin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.AutoChat;
import com.busap.vcs.data.entity.AutoChatType;
import com.busap.vcs.data.entity.LiveSetting;
import com.busap.vcs.service.AutoChatService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.LiveSettingService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.U;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;
@Controller
@RequestMapping("liveSetting")
public class LiveSettingController extends CRUDController<LiveSetting, Long> {

	@Resource(name = "liveSettingService")
	private LiveSettingService liveSettingService;
	
	@Resource(name = "autoChatService")
	private AutoChatService autoChatService;
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	
	@Resource(name = "liveSettingService")
	@Override
	public void setBaseService(BaseService<LiveSetting, Long> baseService) {
		this.baseService = baseService;
	}

	@RequestMapping("listSetting")
	public String listSetting(){
		List<AutoChatType> list = autoChatService.findTypes();
		this.request.setAttribute("types", list);
		return "liveSetting/list";
	}
	
	@RequestMapping("ajaxPage")
	@ResponseBody
	public Map<String,Object> ajaxPage(Integer page, Integer rows,CRUDForm curdForm){
		if(page == null || page <= 0){
    		page=1;
    	}
		if(rows == null || rows <= 0){
			rows=20;
		}
		Map<String,Object> params = new HashMap<String,Object>();
    	params.put("pageStart", (page-1)*rows);
    	params.put("pageSize", rows);
		Page<LiveSetting> pinfo = liveSettingService.searchLiveSetting(page, rows, params);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", pinfo.getTotalElements());
		result.put("rows", pinfo.getContent());
		return result;
	}
	
	@RequestMapping("addUser")
	@ResponseBody
	public RespBody addUser(Long settingId,Long uid){
		if(settingId != null && uid != null){
			liveSettingService.addUser(settingId, uid);
			return this.respBodyWriter.toSuccess();
		} else {
			return this.respBodyWriter.toError("参数不正确");
		}
	}
	
	@RequestMapping("removeUser")
	@ResponseBody
	public RespBody removeUsers(Long settingId,String uid){
		if(settingId != null && StringUtils.isNotBlank(uid)){
			String ids[] = uid.split(","); 
			List<Long> uids = new ArrayList<Long>();
			for(String id:ids){
				uids.add(Long.parseLong(id));
			}
			liveSettingService.removeUser(settingId, uids);
			return this.respBodyWriter.toSuccess();
		} else {
			return this.respBodyWriter.toError("参数不正确");
		}
	}
	
	@RequestMapping("searchUser")
	@ResponseBody
	public Map<String,Object> searchUser(Integer page, Integer rows,@RequestParam(value = "settingId", required = true)Long settingId){
		if(page == null || page <= 0){
    		page=1;
    	}
		if(rows == null || rows <= 0){
			rows=20;
		}
		Map<String,Object> params = new HashMap<String,Object>();
    	params.put("pageStart", (page-1)*rows);
    	params.put("pageSize", rows);
    	params.put("settingId", settingId);
    	
    	Page<Map<String,Object>> pinfo = liveSettingService.searchSettingUser(page, rows, params);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", pinfo.getTotalElements());
		result.put("rows", pinfo.getContent());
		return result;
	}
	
	@RequestMapping("add")
	@ResponseBody
	public RespBody add(LiveSetting setting){
		setting.setStatus(0);
		liveSettingService.save(setting);
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("modify")
	@ResponseBody
	public RespBody modify(LiveSetting setting){
		Long id = setting.getId();
		if(id != null ){
			liveSettingService.update(setting);
		} else {
			return this.respBodyWriter.toError("设置ID不存在！");
		}
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("modifyUserCount")
	@ResponseBody
	public RespBody modifyUserCount(Long settingId,Integer majiaCountBegin,Integer maxUserCount,Integer userCountStep,Integer userCountPeriod,Integer maxUserCountPeriod,Integer userCountStat){
		
		LiveSetting ls;
		if((ls = liveSettingService.find(settingId))!=null){
			ls.setMajiaCountBegin(majiaCountBegin);
			ls.setMaxUserCount(maxUserCount);
			ls.setUserCountStep(userCountStep);
			ls.setUserCountPeriod(userCountPeriod);
			ls.setMaxUserCountPeriod(maxUserCountPeriod);
			ls.setUserCountStat(userCountStat);
			
			ls = liveSettingService.update(ls);
		} else {
			return this.respBodyWriter.toError("设置ID不存在！");
		}
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("remove")
	@ResponseBody
	public RespBody delete(Long id){
		liveSettingService.delete(id);
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("showAutoChat")
	public String autoChatPage(){
		List<AutoChatType> list = autoChatService.findTypes();
		this.request.setAttribute("types", list);
		return "liveSetting/listAutoChat";
	}
	
	@RequestMapping("addChatType")
	@ResponseBody
	public RespBody addChatType(String name){
		if(StringUtils.isNotBlank(name)){
			autoChatService.addAutoChatType(name);	
		}else{
			return this.respBodyWriter.toError("类型名称不能为空！");
		}
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("updateChatTypeStatus")
	@ResponseBody
	public RespBody updateChatTypeStatus(Long id,Integer status){
		autoChatService.updateAutoChatTypeStatus(id, status);
		
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("addAutoChat")
	@ResponseBody
	public RespBody addAutoChat(Long uid,Long typeId,String words){
		if(StringUtils.isNotBlank(words)){
			if(typeId==null){
				typeId = 0l;
			}
			AutoChat ac = new AutoChat();
			ac.setUid(uid);
			ac.setTypeId(typeId);
			ac.setWords(words);
			ac.setStatus(0);
			ac.setCreatorId(U.getUid());
			autoChatService.addAutoChat(ac);
		}else{
			return this.respBodyWriter.toError("内容不能为空！");
		}
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("updateAutoChatStatus")
	@ResponseBody
	public RespBody updateAutoChatStatus(Long id,Integer status){
		autoChatService.updateStatus(id, status);		
		return this.respBodyWriter.toSuccess();
	}
	
	@RequestMapping("searchAutoChat")
	@ResponseBody
	public Map<String,Object> searchAutoChat(Integer page, Integer rows,
					@RequestParam(value = "typeId", required = false)Long typeId,
					@RequestParam(value = "uid", required = false)Long uid,
					@RequestParam(value = "words", required = false)String words){
		if(page == null || page <= 0){
    		page=1;
    	}
		if(rows == null || rows <= 0){
			rows=20;
		}
		Map<String,Object> params = new HashMap<String,Object>();
    	params.put("pageStart", (page-1)*rows);
    	params.put("pageSize", rows);
    	if(uid != null){
    		params.put("uid", uid);
    	}else if(typeId != null){
    		params.put("typeId", typeId);
    	}
    	
    	if(StringUtils.isNotBlank(words)){
    		params.put("words", words);
    	}
    	
    	Page<AutoChat> pinfo = autoChatService.searchList(page, rows, params);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", pinfo.getTotalElements());
		result.put("rows", pinfo.getContent());
		return result;
	}
	
	@RequestMapping("commenSetting")
	@ResponseBody
	public RespBody getCommenSetting(){
		Map<String,String> map = jedisService.getMapByKey(BicycleConstants.LIVE_SETTING);
		return this.respBodyWriter.toSuccess(map);
	}
	
	@RequestMapping("modifyCommenSetting")
	@ResponseBody
	public RespBody modifyCommenSetting(Integer minRealUser,Integer minMajiaCount,Integer maxRealUser,Integer step,Integer maxStep,Integer period,Integer maxPeriod,Integer commenStat){
		
		Map<String,String> map = new HashMap<String,String>();
		
		map.put("minRealUser", minRealUser.toString());
		map.put("minMajiaCount", minMajiaCount.toString());
		map.put("maxRealUser", maxRealUser.toString());
		map.put("step", step.toString());
		map.put("maxStep", maxStep.toString());
		map.put("period", period.toString());
		map.put("maxPeriod", maxPeriod.toString());
		map.put("commenStat", commenStat.toString());
		
		jedisService.setValueToMap(BicycleConstants.LIVE_SETTING, map);
		
		return this.respBodyWriter.toSuccess(map);
	}
	
	@RequestMapping("searchTopUser")
	@ResponseBody
	public Map<String,Object> searchTopUser(Integer page, Integer rows){
		if(page == null || page <= 0){
    		page=1;
    	}
		if(rows == null || rows <= 0){
			rows=20;
		}
		Map<String,Object> params = new HashMap<String,Object>();
    	params.put("pageStart", (page-1)*rows);
    	params.put("pageSize", rows);
    	
    	Map<String,Object> result = new HashMap<String,Object>();
    	
    	Set<String> uids = jedisService.getSetFromShard(BicycleConstants.ROOM_USERS_TOPLIST);
    	
    	if(uids != null && uids.size()>0){
    		Integer total = uids.size();
    		Integer startIndex = 0;
    		Integer toIndex = 0;
    		List<String> userIds = new ArrayList<String>();
    		userIds.addAll(uids);
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
    		List<String> pageUid = userIds.subList(startIndex, toIndex);
    		List<Map<String,String>> users = new ArrayList<Map<String,String>>();
    		for(String uid : pageUid){
    			Map<String,String> userInfo = jedisService.getMapByKey(BicycleConstants.USER_INFO + uid);
    			if(userInfo != null && !userInfo.isEmpty()){
    				users.add(userInfo);
    			}
    		}
		
			result.put("total", total);
			result.put("rows", users);
    	}
		return result;
	}
	
	@RequestMapping("addTopUser")
	@ResponseBody
	public RespBody addTopUser(Long uid){
		if( uid != null){
			jedisService.setValueToSetInShard(BicycleConstants.ROOM_USERS_TOPLIST, uid.toString());
			return this.respBodyWriter.toSuccess();
		} else {
			return this.respBodyWriter.toError("参数不正确");
		}
	}
	
	@RequestMapping("removeTopUser")
	@ResponseBody
	public RespBody removeTopUsers(String uid){
		if(StringUtils.isNotBlank(uid)){
			String ids[] = uid.split(","); 
			jedisService.deleteSetItemFromShard(BicycleConstants.ROOM_USERS_TOPLIST, ids);
			return this.respBodyWriter.toSuccess();
		} else {
			return this.respBodyWriter.toError("参数不正确");
		}
	}
}
