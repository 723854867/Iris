package com.busap.vcs.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Bstar;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.BstarService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.utils.RandUtil;
import com.busap.vcs.utils.RequestValidateUtil;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.util.SmsSendUtil;

 
@Controller
@RequestMapping("/bstar")
public class BstarController extends CRUDController<Bstar, Long> {

    private Logger logger = LoggerFactory.getLogger(BstarController.class); 

    @Resource(name="bstarService") 
    BstarService bstarService; 
    
    @Resource(name = "ruserService")
	private RuserService ruserService;
    
    @Resource(name = "requestValidateUtil")
	private RequestValidateUtil requestValidateUtil;
    
    @Autowired
    JedisService jedisService;
    
    @Resource(name="bstarService")
    @Override
    public void setBaseService(BaseService<Bstar, Long> baseService) {
        this.baseService = baseService;
    } 
    
    @RequestMapping("/home")
	public String home(HttpServletResponse response) {
    	requestValidateUtil.referReqKey(response);
    	return "html5/app_within/bstar/index";
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
    public RespBody register(String code,Long uid,String number,Integer sex,String phone,HttpServletRequest request,HttpServletResponse response){ 
    	
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
    	
    	if (bstarService.isUidExist(uid)) {
    		return respBodyWriter.toError("该用户id已存在",ResponseCode.CODE_350.toCode()); 
    	}
    	
    	Bstar b = new Bstar();
    	b.setCreatorId(uid);
    	b.setSex(sex);
    	b.setPhone(phone);
    	b.setNumber(number);
    	
    	this.create(b);
    	
    	return respBodyWriter.toSuccess("提交成功", "ok"); 
    }  
    
}
