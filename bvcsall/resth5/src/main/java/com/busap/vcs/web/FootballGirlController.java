package com.busap.vcs.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.FootballGirl;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.FootballGirlService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.utils.RandUtil;
import com.busap.vcs.utils.RequestValidateUtil;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.util.SmsSendUtil;

 
@Controller
@RequestMapping("/footballGirl")
public class FootballGirlController extends CRUDController<FootballGirl, Long> {

    private Logger logger = LoggerFactory.getLogger(FootballGirlController.class); 

    @Resource(name="footballGirlService") 
    FootballGirlService footballGirlService; 
    
    @Value("${files.localpath}")
	private String basePath;
    
    @Resource(name = "ruserService")
	private RuserService ruserService;
    
    @Resource(name = "requestValidateUtil")
	private RequestValidateUtil requestValidateUtil;
    
    @Autowired
    JedisService jedisService;
    
    @Resource(name="footballGirlService")
    @Override
    public void setBaseService(BaseService<FootballGirl, Long> baseService) {
        this.baseService = baseService;
    } 
    
    @RequestMapping("/home")
	public String home(HttpServletResponse response) {
    	requestValidateUtil.referReqKey(response);
    	return "html5/app_within/soccer_recruit/index";
	}
    
    @RequestMapping("/getCode")
    @ResponseBody
    public RespBody getCode(String phone,HttpServletRequest request,HttpServletResponse response){ 
    	
    	if (!requestValidateUtil.validateReqKey(request, response)) {
    		return respBodyWriter.toError("非法请求",ResponseCode.CODE_350.toCode()); 
    	}
    	
    	if(!SmsSendUtil.isPhone(phone)){
    		return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
        }
    	
    	if (footballGirlService.isPhoneExist(phone)) {
    		return respBodyWriter.toError("该手机号已经存在",ResponseCode.CODE_350.toCode()); 
    	}
    	
    	//生成短信验证码
    	String code = RandUtil.getSixRandCode(); 
    	
    	//redis存储短信验证码
    	jedisService.set(BicycleConstants.FOOTBALL_GIRL_CODE+phone, code); 
    	jedisService.expire(BicycleConstants.FOOTBALL_GIRL_CODE+phone, 5*60);
    	
    	//发送短信
    	boolean ret; 
    	ret=SmsSendUtil.sendMsg(phone,code,5);//发短信
    	if(!ret){ 
    		return respBodyWriter.toError(ResponseCode.CODE_334.toString(), ResponseCode.CODE_334.toCode()); 
    	} 
    	return respBodyWriter.toSuccess("发送成功", "ok"); 
    }  
    
    @RequestMapping("/register")
    @ResponseBody
    public RespBody register(String code,Long uid,String area,String job,String phone,HttpServletRequest request,HttpServletResponse response){ 
    	
    	if (!requestValidateUtil.validateReqKey(request, response)) {
    		return respBodyWriter.toError("非法请求",ResponseCode.CODE_350.toCode()); 
    	}
    	
    	if (code == null || "".equals(code) || !code.equals(jedisService.get(BicycleConstants.FOOTBALL_GIRL_CODE+phone))) {
			return this.respBodyWriter.toError("验证码错误或与手机号不匹配", -1);
		}
    	
    	Ruser ruser = ruserService.find(uid);
    	if (ruser == null) {
    		return this.respBodyWriter.toError("该用户id不存在", -1);
    	}
    	
    	if(!SmsSendUtil.isPhone(phone)){
    		return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
        }
    	
    	if (footballGirlService.isPhoneExist(phone)) {
    		return respBodyWriter.toError("该手机号已经存在",ResponseCode.CODE_350.toCode()); 
    	}
    	
    	if (footballGirlService.isUidExist(uid)) {
    		return respBodyWriter.toError("该用户id已存在",ResponseCode.CODE_350.toCode()); 
    	}
    	
    	FootballGirl fg = new FootballGirl();
    	fg.setCreatorId(uid);
    	fg.setArea(area);
    	fg.setJob(job);
    	fg.setPhone(phone);
    	
    	this.create(fg);
    	
    	return respBodyWriter.toSuccess("提交成功", "ok"); 
    }  
    
    @RequestMapping("/rank")
   	public String rank() {
       	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        List<Long> uids = footballGirlService.findUids();
       	if (uids != null && uids.size() > 0) {
       		for (int i=0;i<uids.size();i++){
       			Map<String,Object> map = new HashMap<String,Object>();
       			map.put("uid", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uids.get(i), "id"));
       			map.put("name", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uids.get(i), "name"));
       			map.put("signature", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uids.get(i), "signature"));
       			map.put("pic", jedisService.getValueFromMap(BicycleConstants.USER_INFO+uids.get(i), "pic"));
       			map.put("popularity", jedisService.getSetSizeFromShard("football_rank_"+uids.get(i)));
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
       	return "html5/app_within/soccer_recruit/rank";
   	}

}
