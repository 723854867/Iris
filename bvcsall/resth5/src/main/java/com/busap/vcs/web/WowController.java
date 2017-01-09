package com.busap.vcs.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.mapper.InviteRegisterDAO;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.impl.SolrUserService;
import com.busap.vcs.utils.RandUtil;
import com.busap.vcs.utils.RequestValidateUtil;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import com.busap.vcs.webcomn.util.SmsSendUtil;

 
@Controller
@RequestMapping("/wow")
public class WowController {

    private Logger logger = LoggerFactory.getLogger(WowController.class); 

    @Resource(name = "ruserService")
	private RuserService ruserService;
    
    @Resource(name = "requestValidateUtil")
	private RequestValidateUtil requestValidateUtil;
    
    @Resource(name = "solrUserService")
	private SolrUserService solrUserService;
    
    @Autowired
    InviteRegisterDAO inviteRegisterDao;
    
    @Autowired
    JedisService jedisService;
    
    @Resource(name = "respBodyBuilder")
    protected RespBodyBuilder respBodyWriter = new RespBodyBuilder();
    
    @RequestMapping("/home")
	public String home(HttpServletResponse response) {
    	requestValidateUtil.referReqKey(response);
    	return "html5/app_within/wow/index";
	}
    @RequestMapping("/share")
    public String share(String inviteUid,HttpServletRequest request,HttpServletResponse response) {
    	requestValidateUtil.referReqKey(response);
    	request.setAttribute("inviteUid", inviteUid);
    	return "html5/app_within/wow/share";
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
    	
    	Ruser has = ruserService.findByUsername(phone);
    	Ruser hasBand = ruserService.findByBandPhone(phone);
    	if(has!=null || hasBand !=null){ 
        	return respBodyWriter.toError(ResponseCode.CODE_301.toString(), ResponseCode.CODE_301.toCode()); 
    	}
    	
    	//生成短信验证码
    	String code = RandUtil.getSixRandCode(); 
    	
    	//redis存储短信验证码
    	jedisService.set(BicycleConstants.REG_CODE+phone, code); 
    	jedisService.expire(BicycleConstants.REG_CODE+phone, 5*60);
    	
    	//发送短信
    	boolean ret; 
    	ret=SmsSendUtil.sendMsg(phone,code,1);//发短信
    	if(!ret){ 
    		return respBodyWriter.toError(ResponseCode.CODE_334.toString(), ResponseCode.CODE_334.toCode()); 
    	} 
    	return respBodyWriter.toSuccess("发送成功", "ok"); 
    }  
    
    @RequestMapping("/register")
    @ResponseBody
    public RespBody register(String code,String phone,String pass,String inviteUid,HttpServletRequest request,HttpServletResponse response){ 
    	
    	if (!requestValidateUtil.validateReqKey(request, response)) {
    		return respBodyWriter.toError("非法请求",ResponseCode.CODE_350.toCode()); 
    	}
    	
    	String redisCode = jedisService.get(BicycleConstants.REG_CODE+phone);
    	if(redisCode==null||!redisCode.equals(code)){ 
        	return respBodyWriter.toError(ResponseCode.CODE_303.toString(), ResponseCode.CODE_303.toCode()); 
    	} 
    	
    	if(!SmsSendUtil.isPhone(phone)){ 
        	return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
    	}
    	
    	if(pass==null||pass.trim().length()==0){ 
        	return respBodyWriter.toError(ResponseCode.CODE_321.toString(), ResponseCode.CODE_321.toCode()); 
    	}
    	
    	Ruser has = ruserService.findByUsername(phone);
    	Ruser hasBand = ruserService.findByBandPhone(phone);
    	
    	if(has!=null || hasBand !=null){ 
        	return respBodyWriter.toError(ResponseCode.CODE_301.toString(), ResponseCode.CODE_301.toCode()); 
    	}
    	Ruser user =new Ruser();
    	user.setUsername(phone); 
    	user.setPhone(phone);
    	user.setName("串门的B宝"+phone.substring(9)); 
    	
    	//注册时给用户默认头像
    	String headPic = jedisService.get(BicycleConstants.DEFAULT_HEAD_PIC);
    	if (headPic != null && !"".equals(headPic)){
    		user.setPic(headPic);
    	}
    	
    	user.setDataFrom(DataFrom.麦视rest接口.getName()); 
    	user.setPassword(pass); 
    	
	    user.setRegPlatform("h5");
    	user.setIsBlacklist(1);//新用户默认可以提现
    	ruserService.save(user);
    	
    	//新注册用户id放到redis里
    	if (user.getId() != null && user.getId() != 0){
    		Long score = System.currentTimeMillis();
    		jedisService.setValueToSortedSetAndDel(BicycleConstants.NEW_REGISTER_USER, score, String.valueOf(user.getId()),score-24*60*60*1000);
    		
    		//新注册用户昵称加入solr引擎索引
        	try {
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
        	
        	try {
				Ruser inviteUser = ruserService.find(Long.parseLong(inviteUid));
				if (inviteUser != null) {
					//插入邀请表
		        	Map<String,Object> map = new HashMap<String,Object>();
		        	map.put("creatorId", user.getId());
		        	map.put("inviteUid", inviteUid);
		        	map.put("createDate", new Date());
		        	map.put("dataFrom", "h5");
		        	inviteRegisterDao.insert(map);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        	
    	}
    	jedisService.delete(BicycleConstants.REG_CODE+phone);
    	return  respBodyWriter.toSuccess("提交成功", "ok"); 
    }  


}
