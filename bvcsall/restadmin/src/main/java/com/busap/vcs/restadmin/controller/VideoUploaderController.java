package com.busap.vcs.restadmin.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.util.page.EnablePaging;
import com.busap.vcs.util.page.JQueryPage;
import com.busap.vcs.util.page.PagingContextHolder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.parser.OrderBySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.base.Filter;
import com.busap.vcs.base.OrderByObject;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.enums.UserType;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.impl.SolrUserService;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.controller.CRUDForm;

/**
 * 暂时结合easyui写的增删查改的例子
 * 
 * @author meizhiwen
 *
 */
@Controller()
@RequestMapping("videoUploader")
public class VideoUploaderController extends CRUDController<Ruser, Long> {

	private static final Logger logger = LoggerFactory.getLogger(VideoUploaderController.class);
	
	@Resource(name="jedisService")
	private JedisService jedisService;
	/*
	 * @Resource(name="videoUploaderService") private VideoUploaderService
	 * videoUploaderService;
	 */
	@Resource(name = "ruserService")
	RuserService ruserService;
	
	@Value("${files.localpath}")
	private String basePath;
	
	@Resource(name = "solrUserService")
	private SolrUserService solrUserService;

	@Resource(name = "ruserService")
	@Override
	public void setBaseService(BaseService<Ruser, Long> baseService) {
		this.baseService = baseService;
	}

	@RequestMapping("videoUploaderlist")
	public String list() {
		return "videoUploader/list";
	}

	/**
	 * 添加用户信息
	 * @param ruser ruser对象
	 * @author huoshanwei
	 * @return map ok 成功  error失败
	 */
	@RequestMapping("doInsertRuser")
	@ResponseBody
	public Map<String,Object> doInsertRuser(@ModelAttribute("file") MultipartFile file,@ModelAttribute("ruser") Ruser ruser){
		Ruser has = ruserService.findByUsername(ruser.getUsername());
		Map<String,Object> map = new HashMap<String, Object>();
		if(has != null){
			map.put("resultCode","error");
			map.put("resultMessage","用户名"+ruser.getUsername()+"已存在！");
			return map;
		}
		if(file.getSize() > 0){
			uploadHeadPic(file,ruser);//上传头像
		}
		ruser.setPhone("13800138000");
		ruser.setIsEnabled(true);
		ruser.setIsLocked(false);
		ruser.setName(ruser.getUsername().trim());
		ruser.setType(UserType.马甲.getName());
		ruser.setPassword("password@_!@#");
		//ruser.setCreateDate(new Date());
		int result = ruserService.insertRuser(ruser);
		if(result == 1){
			jedisService.saveAsMap(BicycleConstants.USER_INFO+ruser.getId(), ruser);
			//马甲用户信息写入redis
			setMajiaToRedis(ruser.getId());
			//新注册用户昵称加入solr引擎索引
        	try {
    			//solrUserService.addUser(user.getId(), user.getName());
    			solrUserService.addUser(ruser.getId(), ruser.getName(),ruser.getPhone());
    		} catch (SolrServerException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	
			map.put("resultCode","ok");
			map.put("resultMessage","添加用户成功！");
		}else{
			map.put("resultCode","error");
			map.put("resultMessage","添加用户失败！");
		}
		return map;
	}

	@RequestMapping("add")
	public String add(@RequestParam MultipartFile file,Ruser user) {
		
		if(file.getSize() > 0){
			uploadHeadPic(file,user);//上传头像
		}
		
		Ruser has = ruserService.findByUsername(user.getUsername());
		if (has != null) {
			return "videoUploader/list";
		}
		user.setPhone("13800138000");
		user.setName(user.getUsername().trim());
		user.setType(UserType.马甲.getName());
		user.setPassword("password@_!@#");

		this.create(user);
		
		jedisService.saveAsMap(BicycleConstants.USER_INFO+user.getId(), user);
		
		//新注册用户昵称加入solr引擎索引
    	try {
			//solrUserService.addUser(user.getId(), user.getName());
			solrUserService.addUser(user.getId(), user.getName(),user.getPhone());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "videoUploader/list";
	}
	
	
	private void uploadHeadPic(@RequestParam MultipartFile file,Ruser user){
		if(!StringUtils.isEmpty(user.getPic())){
			//删除老头像
		}
		String relPath = File.separator + "userHeadPic" + File.separator + "majiaHeadPic" + File.separator + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator;
        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_") + file.getOriginalFilename();
        String relUrl = "";
        try {
        	FileUtils.copyInputStreamToFile(file.getInputStream(), new File(basePath+relPath, originalFilename));
            relUrl = relPath+originalFilename;
            logger.info(relUrl);
            user.setPic(relUrl);//用户头像存储路径
        } catch (IOException e) {
        	logger.error("文件[" + originalFilename + "]上传失败",e);
        	e.printStackTrace();
        }
	}

	@RequestMapping("updatepage")
	public String updatepage(@RequestParam MultipartFile file,Ruser user) {
		
		if(file.getSize() > 0){
			uploadHeadPic(file,user);//上传头像
		}
		
		Ruser u=this.ruserService.find(user.getId());
		if(StringUtils.isNotBlank(user.getUsername())){
			u.setName(user.getName());
			u.setUsername(user.getUsername());
		}
		if(StringUtils.isNotBlank(user.getSex())){
			u.setSex(user.getSex());
		}
//		if(StringUtils.isNotBlank(user.getAddr())){
			u.setAddr(user.getAddr());
//		}
//		if(StringUtils.isNotBlank(user.getSignature())){
			u.setSignature(user.getSignature());
//		}
		if(StringUtils.isNotBlank(user.getPic())){
			u.setPic(user.getPic());
		}
		this.ruserService.update(u);
		
		jedisService.saveAsMap(BicycleConstants.USER_INFO+user.getId(), user);
		
		//新注册用户昵称加入solr引擎索引
    	try {
			//solrUserService.addUser(user.getId(), user.getName());
			solrUserService.addUser(user.getId(), user.getName(),user.getPhone());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:videoUploaderlist";
	}
	
	//生成密码
	@RequestMapping(value = { "/generatePassword" })
	public String generatePassword(Long id){
		this.request.setAttribute("id", id);
		return "videoUploader/generatePassword";
	}
	
	//保存密码
	@RequestMapping(value = { "/savePassword" })
	public String savePassword(Long id,String password){
		Ruser ruser=this.ruserService.find(id);
		
		//todo 加密
		
		ruser.setPassword(password);
		this.ruserService.update(ruser);
		return "redirect:videoUploaderlist";
	}
	
	 /**
     * 获取单个字符串的MD5值
     * 
     * @param text
     * @return
     */
    public static String getStringMD5(String text) {
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance("MD5").digest(text.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    @RequestMapping("rankAble")
    @ResponseBody
    public RespBody rankAble(Long uid,Integer able){
    	Ruser user = ruserService.find(uid);
    	user.setRankAble(able);
    	this.ruserService.update(user);
    	return this.respBodyWriter.toSuccess();
    }

    @RequestMapping(value = {"/listMajia"},
            method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map listpage(Integer page, Integer rows, CRUDForm curdForm) {
        // 从request中获取通用查询条件
    	if(page==0){
    		page=1;
    	}
    	Map<String,Object> params = new HashMap<String,Object>();

    	for (Map.Entry<String, String> entry : curdForm.getFilters().entrySet()) {
        	if(StringUtils.isNotBlank(entry.getValue())){
        		
        		if("userName".equals(entry.getKey())){
        			params.put("username", entry.getValue());
        		}
        	}
        }
        
        params.put("pageStart", (page-1)*rows);
        params.put("pageSize", rows);	
               
        params.put("type", "majia");
        
        Page pinfo = ruserService.searchNormalUserList(page, rows, params);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
	    jsonMap.put("total", pinfo.getTotalElements());//total键 存放总记录数，必须的 
	    jsonMap.put("rows", pinfo.getContent());//rows键 存放每页记录 list  
	    
        return jsonMap;
    }

	/**
	 * 查询马甲用户列表信息
	 * @param queryPage
	 * @param name
	 * @param stat
	 * @param vipStat
	 * @param rankAble
	 * @param sex
	 * @param addr
	 * @param startCount
	 * @param endCount
	 * @author huoshanwei
	 * @return map
	 */
	@RequestMapping("queryMajiaUserList")
	@ResponseBody
	@EnablePaging
	public Map<String,Object> queryMajiaUserList(@ModelAttribute("queryPage") JQueryPage queryPage,
												 @RequestParam(value = "name",required = false) String name,
												 @RequestParam(value = "stat",required = false) Integer stat,
												 @RequestParam(value = "vipStat",required = false) Integer vipStat,
												 @RequestParam(value = "rankAble",required = false) Integer rankAble,
												 @RequestParam(value = "sex",required = false) Integer sex,
												 @RequestParam(value = "addr",required = false) String addr,
												 @RequestParam(value = "startCount",required = false) Integer startCount,
												 @RequestParam(value = "endCount",required = false) Integer endCount){
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("type",UserType.马甲.getName());
		params.put("name",name);
		params.put("stat",stat);
		params.put("vipStat",vipStat);
		params.put("rankAble",rankAble);
		params.put("sex",sex);
		params.put("addr",addr);
		params.put("startCount",startCount);
		params.put("endCount",endCount);
		List<Ruser> list = ruserService.selectRusers(params);
		com.busap.vcs.util.page.Page pageInfo = PagingContextHolder.getPage();
		map.put("rows",list);
		map.put("total",pageInfo.getTotalResult());
		return map;
	}

	//将马甲信息更新或添加至redis
	private void setMajiaToRedis(Long id) {
		Ruser ruser = ruserService.selectByPrimaryKey(id);
		if (ruser.getType().equals("majia")) {
			String uidStr = String.valueOf(id);
			//将用户的uid放到set中
			jedisService.setValueToSetInShard(BicycleConstants.MAJIA_UID, uidStr);
			//将用户信息放到map中
			jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "uid", uidStr);
			jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "name", ruser.getName() == null ? "" : ruser.getName());
			jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "pic", ruser.getPic() == null ? "" : ruser.getPic());
			jedisService.setValueToMap(BicycleConstants.MAJIA + uidStr, "vstat", String.valueOf(ruser.getVipStat()));
		}
	}

}
