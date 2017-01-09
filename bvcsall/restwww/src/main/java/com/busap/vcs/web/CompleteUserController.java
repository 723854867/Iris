package com.busap.vcs.web;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.solr.client.solrj.SolrServerException;
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
import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.vo.AttentionRecordVO;
import com.busap.vcs.operate.utils.CommonUtil;
import com.busap.vcs.service.AttentionService;
import com.busap.vcs.service.BaseService;
import com.busap.vcs.service.IntegralService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.SensitiveWordService;
import com.busap.vcs.service.impl.SolrUserService;
import com.busap.vcs.util.DateUtils;
import com.busap.vcs.utils.RandUtil;
import com.busap.vcs.utils.ValidateUtil;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.util.SmsSendUtil;

/**
 * 用户完善资料 Created with IntelliJ IDEA and by User: djyin DateTime: 11/13/13 5:07 PM
 */
@Controller
@RequestMapping("/user")
public class CompleteUserController extends CRUDController<Ruser, Long> {

	private Logger logger = LoggerFactory.getLogger(CompleteUserController.class); 
	
	private SimpleDateFormat fmt_date = new SimpleDateFormat("yyyy-MM-dd");
	
	@Resource(name = "ruserService")
	RuserService ruserService;
	
	@Autowired
    JedisService jedisService;

	@Autowired
	IntegralService integralService;
	
	@Resource(name="sensitiveWordService") 
    SensitiveWordService sensitiveWordService;
	
	@Resource(name="validateUtil") 
	ValidateUtil validateUtil;
	
	@Resource(name="attentionService")
	private AttentionService attentionService;
	
	@Value("${files.localpath}")
	private String basePath;

	@Resource(name = "ruserService")
	@Override
	public void setBaseService(BaseService<Ruser, Long> baseService) {
		this.baseService = baseService;
	}
	
	@Resource(name = "solrUserService")
	private SolrUserService solrUserService;

	// 完善用户信息
	@RequestMapping("/update")
	@ResponseBody
	public RespBody update(Ruser user) {  
    	String uid = (String)this.request.getHeader("uid"); 
//		if(ruserService.existByName(user.getName(),Long.parseLong(uid))){
//			Map<String,Object> map =new HashMap<String,Object>(); 
//			String []names = new String[3];
//			int i=0 ;
//			while(i<3){
//				String s = RandUtil.getRandomString(user.getName());
//				if(!ruserService.existByName(s,Long.parseLong(uid))){
//					names[i]=s;
//					i++;
//				}
//			}
//			map.put("names", names);
//    		return respBodyWriter.toError(ResponseCode.CODE_333.toString(),map, ResponseCode.CODE_333.toCode()); 
//        }
		 
		Ruser u=this.ruserService.find(Long.parseLong(uid));
		if(StringUtils.isNotBlank(user.getName())){
			u.setName(user.getName());
		}
		if(StringUtils.isNotBlank(user.getPhone())){
			u.setPhone(user.getPhone());
		} 
		u.setSex(user.getSex());
		u.setBirthday(user.getBirthday());
		u.setAddr(user.getAddr());
	    u.setSignature(user.getSignature()); 
	    u.setEmail(user.getEmail());
	    String openId = u.getOpenId();  //openId不进行关键字过滤
	    sensitiveWordService.checkAndReplaceSensitiveWord(u);
	    if (openId != null && !"".equals(openId)) {
	    	u.setOpenId(openId);
	    }
	    jedisService.saveAsMap(BicycleConstants.USER_INFO+u.getId(), u);
	    
	  //更新用户昵称加入solr引擎索引
    	try {
			//solrUserService.addUser(u.getId(), u.getName());
			solrUserService.addUser(u.getId(), u.getName(),u.getPhone());
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
		// 添加积分记录
		if (StringUtils.isNotBlank(u.getSex()) && u.getAge() > 0
				&& StringUtils.isNotBlank(u.getName()) && StringUtils.isNotBlank(u.getAddr())
				&& StringUtils.isNotBlank(u.getPic()) && StringUtils.isNotBlank(u.getSignature())) {
			integralService.getIntegralFromCompleteInfo(Long.parseLong(uid));
		}
		return respBodyWriter.toSuccess(this.ruserService.update(u));
	}  
	
	//获取注册验证码
    @RequestMapping("/getCode")
    @ResponseBody
    public RespBody getCode(String phone,boolean isReg,String reqTimestamp,String reqSignature,String areaCode){ 
    	
    	if (StringUtils.isNotBlank(areaCode) && !"0086".equals(areaCode) && !"86".equals(areaCode)) {  //国际区号走国际短信
    		phone = areaCode+phone; //国际号码手机号存区号+手机号码
    	}
    	
    	if (StringUtils.isBlank(reqTimestamp)) {
    		reqTimestamp = request.getParameter("reqTimestamp");
    	}
    	if (StringUtils.isBlank(reqTimestamp)) {
    		reqTimestamp = request.getHeader("reqTimestamp");
    	}
    	if (StringUtils.isBlank(reqSignature)) {
    		reqSignature = request.getParameter("reqSignature");
    	}
    	if (StringUtils.isBlank(reqSignature)) {
    		reqSignature = request.getHeader("reqSignature");
    	}
    	logger.info("getCode,signature,reqTimestamp:{},reqSignature:{}",reqTimestamp,reqSignature);
    	if (!validateUtil.isLegal(reqTimestamp, reqSignature,request) && isReg) { //判断是否为非法请求
    		return respBodyWriter.toError(ResponseCode.CODE_334.toString(), ResponseCode.CODE_334.toCode());
    	}
    	
    	//存储用户每天找回密码的次数 key=phone+"-"+年-月-日
    	String key = "";
    	Integer c =0;
    	if(!isReg){//找回密码才限制发送次数，注册不限
    		key = BicycleConstants.RESETPWD_CODE+phone+"_code_count_"+fmt_date.format(new Date()); 
	    	String cstr=jedisService.get(key);  
	    	if(cstr!=null&&!cstr.trim().equals("")){ 
	    		c = Integer.parseInt(cstr.trim());
	    		if(c.intValue()>=3){
	    			if(!isReg)
	    				return respBodyWriter.toError(ResponseCode.CODE_336.toString(), ResponseCode.CODE_336.toCode()); 
	    		}
	    	}
    	}
    	
    	if (StringUtils.isNotBlank(areaCode) && !"0086".equals(areaCode) && !"86".equals(areaCode)) {  //国际区号走国际短信
    		if(!SmsSendUtil.isNumeric(phone)){   //国际号码只判断是不是纯数字
        		return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
            }
    	} else {
    		if(!SmsSendUtil.isPhone(phone)){
        		return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
            }
    	}
    	
    	Ruser has = ruserService.findByUsername(phone);
    	Ruser hasBand = ruserService.findByBandPhone(phone);
    	if(isReg&&(has!=null || hasBand !=null)){ 
        	return respBodyWriter.toError(ResponseCode.CODE_301.toString(), ResponseCode.CODE_301.toCode()); 
    	}
    	String code = RandUtil.getSixRandCode(); 
    	if(isReg){//设置注册验证码
	    	jedisService.set(BicycleConstants.REG_CODE+phone, code); 
	    	jedisService.expire(BicycleConstants.REG_CODE+phone, 5*60);
    	}else{ //设置找回密码验证码
	    	jedisService.set(BicycleConstants.RESETPWD_CODE+phone, code); 
	    	jedisService.expire(BicycleConstants.RESETPWD_CODE+phone, 5*60);
    	}
    	
    	boolean ret; 
    	if(isReg){ 
    		ret=SmsSendUtil.sendMsg(phone,code,1);//发短信
    	}else{  
    		ret=SmsSendUtil.sendMsg(phone,code,2);//发短信
    	}
    	//设置验证码次数限制
    	if(!isReg&&ret){
	    	jedisService.set(key, (c+1)+"");
	    	jedisService.expire(key, 24*60*60);//有效期24小时
    	}
    	if(!ret){ 
    		return respBodyWriter.toError(ResponseCode.CODE_334.toString(), ResponseCode.CODE_334.toCode()); 
    	} 
    	return respBodyWriter.toSuccess("发送成功", "ok"); 
    }  
    
    //获取绑定手机号验证码
    @RequestMapping("/getBandCode")
    @ResponseBody
    public RespBody getBandCode(String phone,String areaCode){ 
    	logger.info("getBandCode,phone={},areaCode={}",phone,areaCode);
    	if (StringUtils.isNotBlank(areaCode) && !"0086".equals(areaCode) && !"86".equals(areaCode)) { //国际短信
    		phone = areaCode+phone;
    		if (!SmsSendUtil.isNumeric(phone)) {
    			return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
    		}
    	} else {
    		if(!SmsSendUtil.isPhone(phone)){
    			return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
    		}
    	}
    	Ruser temp1 = ruserService.findByUsername(phone);
    	Ruser temp2 = ruserService.findByBandPhone(phone);
    	if(temp1 != null || temp2 != null){ 
        	return respBodyWriter.toError(ResponseCode.CODE_351.toString(), ResponseCode.CODE_351.toCode()); 
    	}
    	String code = RandUtil.getSixRandCode(); 
    	jedisService.set(BicycleConstants.BAND_CODE+phone, code); 
    	jedisService.expire(BicycleConstants.BAND_CODE+phone, 5*60);
    	
    	boolean ret; 
    	ret=SmsSendUtil.sendMsg(phone,code,4);//发短信
    	if(!ret){ 
    		return respBodyWriter.toError(ResponseCode.CODE_334.toString(), ResponseCode.CODE_334.toCode()); 
    	} 
    	return respBodyWriter.toSuccess("发送成功", "ok"); 
    }  
    
    //绑定手机号
    @RequestMapping("/band")
    @ResponseBody
    public RespBody band(String phone,String pass,String code,String areaCode,Integer changeBand){ 
    	String uid= this.request.getHeader("uid");
    	if (StringUtils.isNotBlank(areaCode) && !"0086".equals(areaCode) && !"86".equals(areaCode)) { //国际短信
    		phone = areaCode+phone;
    		if (!SmsSendUtil.isNumeric(phone)) {
    			return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
    		}
    	} else {
    		if(!SmsSendUtil.isPhone(phone)){
    			return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
    		}
    	}
    	Ruser ruser = ruserService.find(Long.parseLong(uid));
    	if (changeBand != null && changeBand.intValue() == 1){  //更新绑定号码操作
    		String changeBandCheck = jedisService.get(BicycleConstants.CHANGE_BAND_CHECK_+uid);
    		if (StringUtils.isNotBlank(changeBandCheck) && "1".equals(changeBandCheck)) {  //判断更换绑定验证是否通过
    			String redisCode = jedisService.get(BicycleConstants.BAND_CODE+phone);
    			if(redisCode==null||!redisCode.equals(code)){ 
    				return respBodyWriter.toError(ResponseCode.CODE_303.toString(), ResponseCode.CODE_303.toCode()); 
    			} 
    			
    			Ruser temp1 = ruserService.findByUsername(phone);
    			Ruser temp2 = ruserService.findByBandPhone(phone);
    			if(temp1 != null || temp2 != null){ 
    				return respBodyWriter.toError(ResponseCode.CODE_351.toString(), ResponseCode.CODE_351.toCode()); 
    			}
    			
    			if (!CommonUtil.isPhone(ruser.getUsername())) { //手机号注册的不允许绑定
    				ruser.setBandPhone(phone);
    			}
    		}
    	} else { //首次绑定号码操作
    		if(pass==null || pass.trim().length()==0){ 
            	return respBodyWriter.toError(ResponseCode.CODE_321.toString(), ResponseCode.CODE_321.toCode()); 
        	}
        	
        	String redisCode = jedisService.get(BicycleConstants.BAND_CODE+phone);
        	if(redisCode==null||!redisCode.equals(code)){ 
            	return respBodyWriter.toError(ResponseCode.CODE_303.toString(), ResponseCode.CODE_303.toCode()); 
        	} 
        	
        	Ruser temp1 = ruserService.findByUsername(phone);
        	Ruser temp2 = ruserService.findByBandPhone(phone);
        	if(temp1 != null || temp2 != null){ 
            	return respBodyWriter.toError(ResponseCode.CODE_351.toString(), ResponseCode.CODE_351.toCode()); 
        	}
        	
        	if (!CommonUtil.isPhone(ruser.getUsername())) { //手机号注册的不允许绑定
        		ruser.setBandPhone(phone);
        		ruser.setPassword(pass);
        	}
    	}
    	
    	return respBodyWriter.toSuccess(this.ruserService.update(ruser));
    }  
    
    //更换绑定验证
    @RequestMapping("/changeBandCheck")
    @ResponseBody
    public RespBody changeBandCheck(String pass){ 
    	String uid= this.request.getHeader("uid");
    	logger.info("changeBandCheck,uid={},pass={}",uid,pass);
    	Ruser ruser = ruserService.find(Long.parseLong(uid));
    	if (StringUtils.isNotBlank(pass) && pass.equals(ruser.getPassword())){
    		jedisService.set(BicycleConstants.CHANGE_BAND_CHECK_+ruser.getId(), "1");
    		jedisService.expire(BicycleConstants.CHANGE_BAND_CHECK_+ruser.getId(), 5*60);
    		return respBodyWriter.toSuccess(1);
    	} else {
    		return respBodyWriter.toSuccess(0);
    	}
    }  
    
    
    
    //用户注册
    @RequestMapping("/reg")
    @ResponseBody
    public RespBody reg(String phone,String pass,String code,String nickname,String regPlatform,String appVersion,String areaCode){  
    	
    	String now = DateUtils.getNowAsStr(); //获得当前的日期和时间
    	String nowYMD = DateUtils.getNowYMDAsStr();//获得当前年月日
    	jedisService.setValueToList("phone_reg_total_"+nowYMD, now+"|"+phone+"|"+pass+"|"+code+"|"+nickname+"|"+regPlatform+"|"+appVersion);
    	if (StringUtils.isNotBlank(areaCode) && !"0086".equals(areaCode) && !"86".equals(areaCode)) { //国际短信
    		phone = areaCode+phone;
    		if(!SmsSendUtil.isNumeric(phone)){ 
        		jedisService.setValueToList("phone_reg_fail_"+nowYMD, now+"|"+phone+"|"+pass+"|"+code+"|"+nickname+"|"+regPlatform+"|"+appVersion+"|"+"phoneError");
            	return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
        	}
    	} else {
    		if(!SmsSendUtil.isPhone(phone)){ 
        		jedisService.setValueToList("phone_reg_fail_"+nowYMD, now+"|"+phone+"|"+pass+"|"+code+"|"+nickname+"|"+regPlatform+"|"+appVersion+"|"+"phoneError");
            	return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
        	}
    	}
    	
    	
    	if(pass==null||pass.trim().length()==0){ 
    		jedisService.setValueToList("phone_reg_fail_"+nowYMD, now+"|"+phone+"|"+pass+"|"+code+"|"+nickname+"|"+regPlatform+"|"+appVersion+"|"+"passEmpty");
        	return respBodyWriter.toError(ResponseCode.CODE_321.toString(), ResponseCode.CODE_321.toCode()); 
    	}
    	String redisCode = jedisService.get(BicycleConstants.REG_CODE+phone);
    	if(redisCode==null||!redisCode.equals(code)){ 
    		jedisService.setValueToList("phone_reg_fail_"+nowYMD, now+"|"+phone+"|"+pass+"|"+code+"|"+nickname+"|"+regPlatform+"|"+appVersion+"|"+"codeError");
        	return respBodyWriter.toError(ResponseCode.CODE_303.toString(), ResponseCode.CODE_303.toCode()); 
    	} 
    	Ruser has = ruserService.findByUsername(phone);
    	Ruser hasBand = ruserService.findByBandPhone(phone);
    	
    	if(has!=null || hasBand !=null){ 
    		jedisService.setValueToList("phone_reg_fail_"+nowYMD, now+"|"+phone+"|"+pass+"|"+code+"|"+nickname+"|"+regPlatform+"|"+appVersion+"|"+"userExist");
        	return respBodyWriter.toError(ResponseCode.CODE_301.toString(), ResponseCode.CODE_301.toCode()); 
    	}
    	Ruser user =new Ruser();
    	user.setUsername(phone); 
    	user.setPhone(phone);
    	user.setAppVersion(appVersion);
		//设置绑定手机号 huoshanwei
		//user.setBandPhone(phone);
//    	user.setName(phone.substring(0,3)+"****"+phone.substring(7)); 
    	if (nickname != null && !"".equals(nickname)) {
    		user.setName(nickname.trim());
    	} else {
    		user.setName("串门的B宝"+phone.substring(9)); 
    	}
    	
    	//注册时给用户默认头像
    	String headPic = jedisService.get(BicycleConstants.DEFAULT_HEAD_PIC);
    	if (headPic != null && !"".equals(headPic)){
    		user.setPic(headPic);
    	}
    	
    	user.setDataFrom(DataFrom.麦视rest接口.getName()); 
    	user.setPassword(pass); 
    	
    	//判断注册用户平台
		if (regPlatform!=null && !"".equals(regPlatform)){
			user.setRegPlatform(regPlatform);
		} else {
			String platform = this.request.getParameter("osVersion");
	    	if (platform == null || "".equals(platform)){
	    		platform = this.request.getHeader("osVersion");
	    	}
	    	if (platform != null && platform.contains("ios")){
	    		platform = "ios";
	    	} else {
	    		platform = "android";
	    	}
	    	user.setRegPlatform(platform);
		}
    	user.setIsBlacklist(1);//新用户默认可以提现
    	RespBody ret =this.create(user);
    	
    	//新注册用户id放到redis里
    	if (user.getId() != null && user.getId() != 0){
    		jedisService.saveAsMap(BicycleConstants.USER_INFO+user.getId(), user); //用户信息放redis
    		
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
    	}
    	jedisService.delete(BicycleConstants.REG_CODE+phone);
    	return  ret; 
    }   
    
    //第三方用户注册
    @RequestMapping("/thirdReg")
    @ResponseBody
    public RespBody thirdReg(String phone,String pass,String code){  
    	if(!SmsSendUtil.isPhone(phone)){ 
        	return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
    	}
    	if(pass==null||pass.trim().length()==0){ 
        	return respBodyWriter.toError(ResponseCode.CODE_321.toString(), ResponseCode.CODE_321.toCode()); 
    	}
    	String redisCode = jedisService.get(BicycleConstants.REG_CODE+phone);
    	if(redisCode==null||!redisCode.equals(code)){ 
        	return respBodyWriter.toError(ResponseCode.CODE_303.toString(), ResponseCode.CODE_303.toCode()); 
    	} 
    	Ruser has = ruserService.findByUsername(phone);
    	if(has!=null){ 
        	return respBodyWriter.toError(ResponseCode.CODE_301.toString(), ResponseCode.CODE_301.toCode()); 
    	}
    	Ruser user =new Ruser();
    	user.setUsername(phone); 
    	user.setPhone(phone);
    	user.setName(phone.substring(0,3)+"****"+phone.substring(7)); 
    	user.setDataFrom(DataFrom.麦视rest接口.getName()); 
    	user.setPassword(pass); 
    	RespBody ret =this.create(user);
		//新注册的用户默认关注 我拍小助手lj
    	if (user.getId() != null && user.getId() != 0){
    		String officialUserId = jedisService.get(BicycleConstants.OFFICICAL_USER_ID);
    		if (officialUserId != null && !"".equals(officialUserId)){
	    		if (attentionService.isAttention(user.getId(), Long.parseLong(officialUserId)) != 1){
		    		int result = attentionService.payAttention(user.getId(), Long.parseLong(officialUserId), "");
	    			if (result == 1){//添加关注成功，放到redis里
	    				jedisService.setValueToSetInShard(BicycleConstants.ATTENTION_ID_OF+user.getId(), officialUserId);
	    			}
	    		}
    		}
    	}
    	jedisService.saveAsMap(BicycleConstants.USER_INFO+user.getId(), user);
    	jedisService.delete(BicycleConstants.REG_CODE+phone);
    	return  ret; 
    }   
    
    //用户重置密码
    @RequestMapping("/resetPass")
    @ResponseBody
    public RespBody resetPass(String phone,String pass,String code,String areaCode){  
    	
    	if(!SmsSendUtil.isPhone(phone)){ 
        	return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
    	}
    	if(pass==null||pass.trim().length()==0){ 
        	return respBodyWriter.toError(ResponseCode.CODE_321.toString(), ResponseCode.CODE_321.toCode()); 
    	}
    	//存储用户每天找回密码的次数 key=phone+"-"+年-月-日
    	String key = BicycleConstants.RESETPWD_CODE+phone+"_count_"+fmt_date.format(new Date());
    	String cstr=jedisService.get(key);
    	Integer c =0;
    	if(cstr!=null){ 
    		c = Integer.parseInt(cstr.trim());
    		if(c.intValue()>=3){
    			return respBodyWriter.toError(ResponseCode.CODE_323.toString(), ResponseCode.CODE_323.toCode()); 
    		}
    	}
    	String redisCode = jedisService.get(BicycleConstants.RESETPWD_CODE+phone);
    	if(redisCode==null||!redisCode.equals(code)){ 
        	return respBodyWriter.toError(ResponseCode.CODE_303.toString(), ResponseCode.CODE_303.toCode()); 
    	} 
    	Ruser user = ruserService.findByUsername(phone);
    	if(user==null){ 
    		user = ruserService.findByBandPhone(phone);
    		if (user == null) {
    			return respBodyWriter.toError(ResponseCode.CODE_302.toString(), ResponseCode.CODE_302.toCode()); 
    		}
    	}   
    	user.setPassword(pass); 
    	ruserService.update(user);
    	jedisService.saveAsMap(BicycleConstants.USER_INFO+user.getId(), user);
    	jedisService.set(key, (c+1)+"");
    	jedisService.expire(key, 24*60*60);//有效期24小时 
    	jedisService.delete(BicycleConstants.RESETPWD_CODE+phone);
    	return respBodyWriter.toSuccess(user); 
    }    
    
    //上传用户头像
	@RequestMapping("/uploadHeaderPic")
	@ResponseBody
	public RespBody uploadHeaderPic(@RequestParam MultipartFile file,String phone){
		 
    	String relPath = File.separator+"userHeadPic"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
    	
        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_")
        +file.getOriginalFilename();
        
        String relUrl = ""; 
        try {
        	Ruser user = ruserService.findByUsername(phone);
        	if(user==null)
        		return respBodyWriter.toError(ResponseCode.CODE_302.toString(), ResponseCode.CODE_302.toCode()); 
            
        	FileUtils.copyInputStreamToFile(file.getInputStream(), new File(basePath+relPath, originalFilename));
            relUrl = relPath+originalFilename;
            user.setPic(relUrl);
            ruserService.update(user);
            jedisService.saveAsMap(BicycleConstants.USER_INFO+user.getId(), user);
            jedisService.setValueToSortedSetInShard(BicycleConstants.TO_BE_VERIFIED_PIC, System.currentTimeMillis(),user.getId()+"|head"); //修改头像，进入待审核队列
        } catch (IOException e) {
        	logger.error("文件[" + originalFilename + "]上传失败",e); 
            return respBodyWriter.toError(ResponseCode.CODE_307.toString(), ResponseCode.CODE_307.toCode());
        }    
        return respBodyWriter.toSuccess(relUrl);
	}
	
	//上传个人首页背景图
	@RequestMapping("/uploadHomePic")
	@ResponseBody
	public RespBody uploadHomePic(@RequestParam MultipartFile file,String phone){
		 
    	String relPath = File.separator+"userHomePic"+File.separator+DateFormatUtils.format(new Date(), "yyyy-MM-dd")+File.separator;
    	
        String originalFilename = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss_")
        +file.getOriginalFilename();
        
        String relUrl = ""; 
        try {
        	Ruser user = ruserService.findByUsername(phone);
        	if(user==null)
        		return respBodyWriter.toError(ResponseCode.CODE_302.toString(), ResponseCode.CODE_302.toCode()); 
            
        	FileUtils.copyInputStreamToFile(file.getInputStream(), new File(basePath+relPath, originalFilename));
            relUrl = relPath+originalFilename;
            user.setHomePic(relUrl);
            ruserService.update(user);
            jedisService.saveAsMap(BicycleConstants.USER_INFO+user.getId(), user);
            jedisService.setValueToSortedSetInShard(BicycleConstants.TO_BE_VERIFIED_PIC, System.currentTimeMillis(),user.getId()+"|home"); //修改背景图，进入待审核队列
        } catch (IOException e) {
        	logger.error("文件[" + originalFilename + "]上传失败",e); 
            return respBodyWriter.toError(ResponseCode.CODE_308.toString(), ResponseCode.CODE_308.toCode());
        }    
        return respBodyWriter.toSuccess(relUrl);
	}
	
	
	//用户信息放到redis
	@RequestMapping("/putUsersToRedis")
	@ResponseBody
	public RespBody putUsersToRedis() {  
		List list = ruserService.findAll();
		String key = BicycleConstants.USER_INFO;
		jedisService.saveAsMapByList(key, list);
		return respBodyWriter.toSuccess();
	}  
	
	//黄v，蓝v，绿v用户id放到redis
	@RequestMapping("/putVUsersToRedis")
	@ResponseBody
	public RespBody putVUsersToRedis() {
		//查询蓝v放到redis
		List<Long> blueIds = ruserService.findIdsByVipState(1);
		for (Long id:blueIds) {
			jedisService.setValueToSetInShard(BicycleConstants.VIP_OF_BLUE, String.valueOf(id));
		}
		//查询黄v放到redis
		List<Long> yellowIds = ruserService.findIdsByVipState(2);
		for (Long id:yellowIds) {
			jedisService.setValueToSetInShard(BicycleConstants.VIP_OF_YELLOW, String.valueOf(id));
		}
		//查询绿V放到redis,VipState=3
		List<Long> greenIds = ruserService.findIdsByVipState(3);
		for(Long id:greenIds){
			jedisService.setValueToSetInShard(BicycleConstants.VIP_OF_GREEN, String.valueOf(id));
		}
		return respBodyWriter.toSuccess();
	}
	
	//将用户关注的用户id放到redis中
	@RequestMapping("/putAttentionIdsToRedis")
	@ResponseBody
	public RespBody putAttentionIdsToRedis() {
		List<AttentionRecordVO> list = attentionService.getAllRecords();
		for(AttentionRecordVO v:list){
			jedisService.setValueToSetInShard(BicycleConstants.ATTENTION_ID_OF+v.getCreatorId(), String.valueOf(v.getAttentionId()));
		}
		return respBodyWriter.toSuccess();
	}
	
	//根据关键字查询平台用户信息
	@RequestMapping("/findUserByKeyWord")
	@ResponseBody
	public RespBody findUserByKeyWord(String keyWord,Long timestamp,Integer count) {
		String uid = (String)this.request.getHeader("uid");
		logger.info("uid={},findUserByPhoneOrName,keyWord={},timestamp={},count={}",uid,keyWord,timestamp,count);
		if (keyWord != null && !"".equals(keyWord)) {
			return respBodyWriter.toSuccess(ruserService.findUserByKeyWord(uid,timestamp == null || timestamp.intValue()==0?null:new Date(timestamp), keyWord, count));
		} else {
			return respBodyWriter.toSuccess();
		}
	}
	
	//根据用户昵称查询平台用户信息
	@RequestMapping("/findUsersByNickNameSolr")
	@ResponseBody
	public RespBody findUsersByNickNameSolr(
			@RequestParam(value = "name", required = false)  String name,
			@RequestParam(value = "page", required = false)  Integer page,
    		@RequestParam(value = "size", required = false)  Integer size
    		) throws SolrServerException  {
		if(name==null||name.equals("")||page==null) {
			return respBodyWriter.toSuccess();
		}
		
		if (name != null && !"".equals(name)) {
			
			List userIdList=solrUserService.search(name, page, size);
			if(userIdList==null||userIdList.size()==0) {
				return respBodyWriter.toSuccess();
			}
			
			String uid = (String)this.request.getHeader("uid");
			
			List ruserList=ruserService.findUsersByIds(userIdList);
			
			
			if(uid!=null&&!uid.equals("")&&ruserList!=null&&ruserList.size()>0) {
				for (Object object : ruserList) {
					
					Ruser ru=(Ruser) object;

					ru.setIsAttention(attentionService.isAttention(Long.valueOf(uid), ru.getId()) == 1 ?
							1 + attentionService.isAttention(ru.getId(), Long.valueOf(uid)) : 0);
					
				}
				
			}
			
			List orderUserList=new ArrayList();
			for(int i=0;i<userIdList.size();i++) {
				for(int j=0;j<ruserList.size();j++) {
					if(((Long)userIdList.get(i)).longValue()==((Ruser)ruserList.get(j)).getId().longValue()) {
						orderUserList.add(ruserList.get(j));
						break;
					}
				}
				
			}
			return respBodyWriter.toSuccess(orderUserList);
		} else {
			return respBodyWriter.toSuccess();
		}
	}
	
	// 完善用户信息
	@RequestMapping("/findUserById")
	@ResponseBody
	public RespBody findUserById() {  
    	String uid = (String)this.request.getHeader("uid"); 
		 
		Ruser u=this.ruserService.find(Long.parseLong(uid));
		
//		StringBuffer countJpql = new StringBuffer();
//	    List<ParameterBean> paramList=new ArrayList<ParameterBean>();
//	    
//	    
//	    countJpql.append("SELECT COUNT(p.id)  FROM  Praise p,Video v   where p.videoId=v.id  and v.creatorId=").append(uid);
//	    
//	    try {
//			Long count =ruserService.getObjectCountByJpql(countJpql, paramList);
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return respBodyWriter.toSuccess(u);
	}
	
	
}
