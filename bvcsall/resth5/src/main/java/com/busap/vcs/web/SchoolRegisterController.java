package com.busap.vcs.web;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.SimpleFormatter;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.SchoolRegister;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.SchoolRegisterService;
import com.busap.vcs.utils.RandUtil;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.util.SmsSendUtil;
 
@Controller
@RequestMapping("/school")
public class SchoolRegisterController extends CRUDController<SchoolRegister, Long> {

    private Logger logger = LoggerFactory.getLogger(SchoolRegisterController.class); 

    @Resource(name="schoolRegisterService") 
    SchoolRegisterService schoolRegisterService; 
    
    @Value("${files.localpath}")
	private String basePath;
    
    @Resource(name = "ruserService")
	private RuserService ruserService;
    
    @Autowired
    JedisService jedisService;
    
    @Resource(name="schoolRegisterService")
    @Override
    public void setBaseService(BaseService<SchoolRegister, Long> baseService) {
        this.baseService = baseService;
    } 
    
    @RequestMapping("/home")
	public String home() {
    	return "html5/app_within/school_recruit/index";
	}
    
    @RequestMapping("/query")
	public String query(Integer isAdmin) {
    	if (isAdmin != null && isAdmin.intValue() == 1) {
    		request.setAttribute("isAdmin", 1);
    	} else {
    		request.setAttribute("isAdmin", 0);
    	}
    	return "html5/app_activity/inviteQuery/index";
	}
    
    @RequestMapping("/voiceRank")
	public String voiceRank() {
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	String voiceIds = jedisService.get("voice_uids");
    	if (StringUtils.isNotBlank(voiceIds)) {
    		String[] uidArray = voiceIds.split(",");
    		
    		for (int i=0;i<uidArray.length;i++){
    			Map<String,Object> map = new HashMap<String,Object>();
    			map.put("uid", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uidArray[i], "id"));
    			map.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uidArray[i], "name"));
    			map.put("signature", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uidArray[i], "signature"));
    			map.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uidArray[i], "pic"));
    			map.put("popularity", jedisService.getSetSizeFromShard("voice_rank_"+uidArray[i]));
    			list.add(map);
    		}
    		
    		Collections.sort(list, new Comparator<Map<String,Object>>(){
    			
    			@Override
    			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
    				String popularity1 = String.valueOf(o1.get("popularity"));
    				String popularity2 =  String.valueOf(o2.get("popularity"));
    				Long create1 = Long.parseLong(popularity1);
    				Long create2 = Long.parseLong(popularity2);
    				
    				return create2.compareTo(create1);
    			}
    			
    		});
    	}
    	this.request.setAttribute("list", list);
    	return "html5/app_within/hsy_rank/index";
	}
    
    @RequestMapping("/register")
    @ResponseBody
    public RespBody register(String code,Long uid,String name,Integer age,String school,String wechat,String qq,String phone,String introduction,String inviteCode){
    	if (code == null || "".equals(code) || !code.equals(jedisService.get(BicycleConstants.SCHOOL_REGISTER_CODE+phone))) {
			return this.respBodyWriter.toError("验证码错误或与手机号不匹配", -1);
		}
    	
    	if (ruserService.find(uid) == null) {
    		return respBodyWriter.toError("该用户id不存在",ResponseCode.CODE_350.toCode()); 
    	}
    	
    	SchoolRegister sr = schoolRegisterService.getRegisterInfoByUid(uid);
    	
    	if (sr != null) {
    		return respBodyWriter.toError("该用户已绑定",ResponseCode.CODE_350.toCode()); 
    	}
    	sr = schoolRegisterService.getRegisterInfo(phone);
    	
    	if (sr != null) {
    		return respBodyWriter.toError("该手机号已经存在",ResponseCode.CODE_350.toCode()); 
    	}
    	if (inviteCode == null || "".equals(inviteCode) || !schoolRegisterService.inviteCodeExist(inviteCode)) { //邀请码不存在，直接返回成功，不插入库
    		return this.respBodyWriter.toSuccess();
    	}
    	
    	SchoolRegister sc = new SchoolRegister();
    	sc.setName(name);
    	sc.setAge(age);
    	sc.setSchool(school);
    	sc.setWechat(wechat);
    	sc.setQq(qq);
    	sc.setPhone(phone);
    	sc.setIntroduction(introduction);
    	sc.setCreatorId(uid);
    	sc.setInviteCode(inviteCode);
    	String pic = "";
//		if (file == null) { 
//			this.respBodyWriter.toError("上传文件为空", -1);
//		} else {
//			pic= uploadPic(file,"schoolRegister",String.valueOf(getRandom(1, 10)));
//			if ("".equals(pic)){ 
//				this.respBodyWriter.toError("上传文件为空", -1);
//			}
//			sc.setImg(pic);
//		}
		this.create(sc);
    	return this.respBodyWriter.toSuccess();
    }
    
    @RequestMapping("/getCode")
    @ResponseBody
    public RespBody getCode(String phone){ 
    	
    	if(!SmsSendUtil.isPhone(phone)){
    		return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
        }
    	
    	SchoolRegister sr = schoolRegisterService.getRegisterInfo(phone);
    	
    	if (sr != null) {
    		return respBodyWriter.toError("该手机号已经存在",ResponseCode.CODE_350.toCode()); 
    	}
    	
    	//生成短信验证码
    	String code = RandUtil.getSixRandCode(); 
    	
    	//redis存储短信验证码
    	jedisService.set(BicycleConstants.SCHOOL_REGISTER_CODE+phone, code); 
    	jedisService.expire(BicycleConstants.SCHOOL_REGISTER_CODE+phone, 5*60);
    	
    	//发送短信
    	boolean ret; 
    	ret=SmsSendUtil.sendMsg(phone,code,3);//发短信
    	if(!ret){ 
    		return respBodyWriter.toError(ResponseCode.CODE_334.toString(), ResponseCode.CODE_334.toCode()); 
    	} 
    	return respBodyWriter.toSuccess("发送成功", "ok"); 
    }  
    
    @RequestMapping("/getInfoByInviteCode")
    @ResponseBody
    public RespBody getInfoByInviteCode(String inviteCode,Integer page,Integer size,String startTime,String endTime,Integer isAdmin){ 
    	if (inviteCode == null || "".equals(inviteCode)) {
    		return respBodyWriter.toError("请输入邀请码", -1); 
    	}
    	if (startTime == null || "".equals(startTime)) {
    		startTime = "2014-01-01";
    	}
    	startTime = startTime + " 00:00:00";
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	List<SchoolRegister> list = new ArrayList<SchoolRegister>();
    	List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
    	int count = 0;
    	try {
			list = schoolRegisterService.getRegisterByInviteCode(inviteCode, page, size, format.parse(startTime), StringUtils.isBlank(endTime)?new Date():format.parse(endTime + " 23:59:59"));
			count = schoolRegisterService.getRegisterCodeByInviteCode(inviteCode, format.parse(startTime), StringUtils.isBlank(endTime)?new Date():format.parse(endTime + " 23:59:59"));
			for (SchoolRegister sr:list) {
				Ruser ruser = ruserService.find(sr.getCreatorId());
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("uid", ruser.getId());
				map.put("name", ruser.getName());
				map.put("registerTime", ruser.getCreateDate());
				map.put("school", sr.getSchool());
				
				if (isAdmin != null && isAdmin.intValue() == 1){
					map.put("phone", sr.getPhone());
				} else {
					map.put("phone", sr.getPhone().substring(0, 3)+"****"+sr.getPhone().substring(7, 11));
				}
				result.add(map);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return respBodyWriter.toSuccess("ok", result, String.valueOf(count));
    }  
    
    @RequestMapping("/getInviteInfo")
    public String getInviteInfo(Integer type){ 
    	List<Map<String,Object>> list = schoolRegisterService.getInviteInfo(type);
    	request.setAttribute("list", list);
    	request.setAttribute("type", type);
    	return "html5/app_within/school_recruit/info";
    }  
    
    private String uploadPic(MultipartFile file,String catalog,String number) {
		String relUrl = "";
		String relPath = File.separator + catalog + File.separator
				+ DateFormatUtils.format(new Date(), "yyyy-MM-dd")
				+ File.separator;
		try {
			String fileName = file.getOriginalFilename();
			String sufix = fileName.substring(fileName.lastIndexOf("."));
			String originalFilename = DateFormatUtils.format(new Date(),
					"yyyy-MM-dd_HHmmss")+number + sufix;

			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(
					basePath + relPath, originalFilename));
			relUrl = relPath + originalFilename;
		} catch (IOException e) {
			logger.error("文件上传失败", e);
		}
		return relUrl;
	}
    
    private int getRandom(int min,int max){
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
	}

}
