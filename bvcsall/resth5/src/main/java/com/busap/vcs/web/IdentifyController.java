package com.busap.vcs.web;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.busap.vcs.data.entity.IdentifyInfo;
import com.busap.vcs.data.vo.CustPicResponse;
import com.busap.vcs.service.*;

import net.sf.json.JSONObject;

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

import com.busap.vcs.base.Action;
import com.busap.vcs.base.Message;
import com.busap.vcs.base.Module;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.constants.ResponseCode;
import com.busap.vcs.data.entity.Anchor;
import com.busap.vcs.data.entity.Room;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.operate.utils.CommonUtil;
import com.busap.vcs.service.kafka.producer.KafkaProducer;
import com.busap.vcs.utils.ImageCompressUtil;
import com.busap.vcs.utils.RandUtil;
import com.busap.vcs.webcomn.RespBody;
import com.busap.vcs.webcomn.RespBodyBuilder;
import com.busap.vcs.webcomn.controller.CRUDController;
import com.busap.vcs.webcomn.util.SmsSendUtil;

/**
 * 直播,房间相关controller
 *
 */
@Controller
@RequestMapping("/identify")
public class IdentifyController extends CRUDController<Room, Long> {

	private Logger logger = LoggerFactory.getLogger(IdentifyController.class);


	@Resource(name = "anchorService")
	private AnchorService anchorService;

	@Value("${files.localpath}")
	private String basePath;
	
	@Resource(name = "sensitivewordFilter")
	SensitivewordFilter sensitivewordFilter;
	
	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Resource(name="jedisService")
	private JedisService jedisService;

	@Resource(name="realAuthService")
	private RealAuthService realAuthService;
	
	@Autowired
    KafkaProducer kafkaProducer;
	
	@Resource(name = "roomService")
	@Override
	public void setBaseService(BaseService<Room, Long> baseService) {
		this.baseService = baseService;
	}

	@Resource(name = "respBodyBuilder")
	private RespBodyBuilder respBodyWriter = new RespBodyBuilder();
	
	private void setCookie(HttpServletResponse response,Map<String,String> cookies,int expire) {
		for (Map.Entry<String, String> entry : cookies.entrySet()) {  
			logger.info("h5 set cookies:{}------------->{}",entry.getKey(),entry.getValue());
			Cookie cookie = new Cookie(entry.getKey(),entry.getValue());
			cookie.setMaxAge(expire);
			cookie.setPath("/");
			response.addCookie(cookie);
		}  
	}
	
	@RequestMapping("/home")
	public String home(HttpServletResponse response){
		String uid = request.getHeader("uid");
		String accessToken = request.getHeader("access_token");
		if (StringUtils.isBlank(uid)) {
			uid = request.getParameter("uid");
		}
		if (StringUtils.isBlank(accessToken)) {
			accessToken = request.getParameter("access_token");
		}
		Ruser ruser = ruserService.find(Long.parseLong(uid));
		String phone = "";
		if (CommonUtil.isPhone(ruser.getUsername())) {
			phone = ruser.getUsername();
		} else if (StringUtils.isNotBlank(ruser.getBandPhone()) && CommonUtil.isPhone(ruser.getBandPhone())){
			phone = ruser.getBandPhone();
		}
		Map<String,String> cookies = new HashMap<String,String>();
		cookies.put("uid", uid);
		cookies.put("access_token", accessToken);
		setCookie(response, cookies, 30*60);
		request.setAttribute("phone", phone);
		return "html5/app_within/liveIdentify/index";
	}
	
	 /**
     * 获得主播认证状态
     * @return
     */
    @RequestMapping("/getIdentifyStatus")
    @ResponseBody
    public RespBody getIdentifyStatus(){ 
    	
    	String uid = this.request.getHeader("uid");
    	Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(uid));
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("isExist", anchor == null || anchor.getStatus() == -2 ? "0":"1"); //认证信息是否存在，1：存在，0：不存在
    	map.put("status", anchor == null ?"":anchor.getStatus());
    	map.put("reason", anchor == null ?"":anchor.getRejectReason());
    	
    	return respBodyWriter.toSuccess(map); 
    }  
	

    /**
     * 获得主播认证验证码
     * @param phone
     * @return
     */
    @RequestMapping("/getCode")
    @ResponseBody
    public RespBody getCode(String phone){ 
    	
    	String uid = this.request.getHeader("uid");
    	logger.info("Identify,getCode,phone={},uid={}",phone,uid);
    	if(!SmsSendUtil.isPhone(phone)){
    		return respBodyWriter.toError(ResponseCode.CODE_330.toString(), ResponseCode.CODE_330.toCode()); 
        }
    	
    	Anchor anchor = anchorService.getAnchorByPhone(phone);
    	if (anchor != null && !uid.equals(String.valueOf(anchor.getCreatorId()))) {
    		return respBodyWriter.toError("该手机号码已被占用",ResponseCode.CODE_350.toCode()); 
    	}
    	
    	//生成短信验证码
    	String code = RandUtil.getSixRandCode(); 
    	
    	//redis存储短信验证码
    	jedisService.set(BicycleConstants.ANCHOR_IDENTIFY_CODE+phone+"_"+uid, code); 
    	jedisService.expire(BicycleConstants.ANCHOR_IDENTIFY_CODE+phone+"_"+uid, 5*60);
    	
    	//发送短信
    	boolean ret; 
    	ret=SmsSendUtil.sendMsg(phone,code,3);//发短信
    	if(!ret){ 
    		return respBodyWriter.toError(ResponseCode.CODE_334.toString(), ResponseCode.CODE_334.toCode()); 
    	} 
    	return respBodyWriter.toSuccess("发送成功", "ok"); 
    }  
    
    private String validate(String code,String uid,String realName,String phone,String certificateNumber,String bankNumber,String bankAddress,String bankName){
    	if (code == null || "".equals(code) || !code.equals(jedisService.get(BicycleConstants.ANCHOR_IDENTIFY_CODE+phone+"_"+uid))) {
			return "验证码错误或与手机号不匹配"; 
		}
    	String errorMsg = "";
    	Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(uid));
    	if (anchor != null && anchor.getStatus() >=0) { //主播认证信息存在，且不是认证失败状态，不让重复提交
    		return "不能重复提交认证信息"; 
    	}
    	if (StringUtils.isBlank(realName) || StringUtils.isBlank(phone) || StringUtils.isBlank(certificateNumber) ||StringUtils.isBlank(bankNumber) || StringUtils.isBlank(bankAddress) || StringUtils.isBlank(bankName)) {
    		return "请完善主播认证资料"; 
    	}
    	if (!CommonUtil.isPhone(phone)){
    		return "手机号格式有误"; 
    	}
    	anchor = anchorService.getAnchorByPhone(phone);
    	if (anchor != null && !uid.equals(String.valueOf(anchor.getCreatorId()))) {
    		return "该手机号码已被占用"; 
    	}
    	anchor = anchorService.getAnchorByCertificateNumber(certificateNumber);
    	if (anchor != null && !uid.equals(String.valueOf(anchor.getCreatorId()))) {
    		return "该证件号码已被占用";
    	}
    	return errorMsg;
    	
    }
    
    
    
    /**
     * 主播认证
     * @param code
     * @param realName
     * @param phone
     * @param certificateType
     * @param certificateNumber
     * @param file1
     * @param file2
     * @return
     */
    @RequestMapping("/anchorIdentify")
    @ResponseBody
    public RespBody anchorIdentify(String code,String realName,String phone,String certificateType,String certificateNumber,String bankNumber
    		,String bankAddress,String bankProvince,String bankCity,String password,String bankName
    		,@RequestParam MultipartFile file1,@RequestParam MultipartFile file2,@RequestParam MultipartFile file3){ 
    	
    	try {
    		String uid = this.request.getHeader("uid");
    		
        	//验证主播信息
        	String errorMsg = validate(code,uid,realName,  phone, certificateNumber,bankNumber,bankAddress,bankName);
        	if (!"".equals(errorMsg)) {
        		return respBodyWriter.toError(errorMsg, ResponseCode.CODE_350.toCode());
        	} 
        	
        	String picOne = "";
        	String picTwo = "";
        	String picThree = "";
        	if (file1 != null) {
        		picOne = uploadPic(file1, "anchorIdentify","1");
        	}
        	if (file2 != null) {
        		picTwo = uploadPic(file2, "anchorIdentify","2");
        	}
        	if (file3 != null) {
        		picThree = uploadPic(file3, "anchorIdentify","3");
        	}
        	
        	Anchor anchor = anchorService.getAnchorByUserid(Long.parseLong(uid));
        	
        	Ruser ruser = ruserService.find(Long.parseLong(uid));
        	
        	//如果之前用户是用手机号注册，或者绑定过手机号，用此手机号作为主播信息
        	if (CommonUtil.isPhone(ruser.getUsername())) { 
    			phone = ruser.getUsername();
    		} else if (StringUtils.isNotBlank(ruser.getBandPhone()) && CommonUtil.isPhone(ruser.getBandPhone())){
    			phone = ruser.getBandPhone();
    		} else {
    			Ruser has = ruserService.findByUsername(phone);
    	    	Ruser hasBand = ruserService.findByBandPhone(phone);
    	    	if (has !=null || hasBand !=null){
    	    		return respBodyWriter.toError("该手机号码已被占用", ResponseCode.CODE_350.toCode());
    	    	}
    			if (StringUtils.isBlank(password)) {  //如果之前不是手机号注册，也没绑定过手机号，必须设置密码
    				return respBodyWriter.toError("密码不能为空",ResponseCode.CODE_350.toCode());
    			}
    		}
    			
        	
        	if (anchor != null && anchor.getStatus() < 0){ 
    			anchor.setRealName(realName);
    	    	anchor.setPhone(phone);
    	    	anchor.setCertificateType(Integer.parseInt(certificateType));
    	    	anchor.setCertificateNumber(certificateNumber);
    	    	anchor.setPicOne(picOne);
    	    	anchor.setPicTwo(picTwo);
    	    	anchor.setPicThree(picThree);
    	    	anchor.setStatus(0);
    	    	anchor.setBankNumber(bankNumber);
    	    	anchor.setBankCity(bankCity);
    	    	anchor.setBankProvince(bankProvince);
    	    	anchor.setBankName(bankName);
    	    	anchor.setBankAddress(bankAddress);
    	    	anchorService.save(anchor);
        	} else if (anchor == null) { //主播没提交过认证信息
        		anchor = new Anchor();
        		anchor.setCreatorId(Long.parseLong(uid));
        		anchor.setCreateDate(new Date());
            	anchor.setRealName(realName);
            	anchor.setPhone(phone);
            	anchor.setCertificateType(Integer.parseInt(certificateType));
            	anchor.setCertificateNumber(certificateNumber);
            	anchor.setPicOne(picOne);
            	anchor.setPicTwo(picTwo);
            	anchor.setPicThree(picThree);
            	anchor.setStatus(0);
            	anchor.setPointCount(0);
            	anchor.setTotalPointCount(0);
            	anchor.setQiniuStreamId("");
            	anchor.setBankNumber(bankNumber);
    	    	anchor.setBankCity(bankCity);
    	    	anchor.setBankProvince(bankProvince);
    	    	anchor.setBankName(bankName);
    	    	anchor.setBankAddress(bankAddress);
            	anchorService.save(anchor);
        	}
        	if (!CommonUtil.isPhone(ruser.getUsername()) && StringUtils.isBlank(ruser.getBandPhone())) { //手机号注册的,已经绑定手机号的不允许绑定
        		ruser.setBandPhone(phone);
        		ruser.setPassword(password);
        		ruserService.save(ruser);
        	}
			// 新建线程做认证
			realAuthCheck(picOne, picTwo, picThree, Long.parseLong(uid), anchor.getPhone());

         	//给kafka发消息审核
//        	Message msg = new Message();
//			msg.setModule(Module.IDENTIFY);
//			msg.setAction(Action.INSERT);
//			Map<String,Object> dataMap = new HashMap<String,Object>();
//			dataMap.put("uid",uid);
//			msg.setDataMap(dataMap);
//			kafkaProducer.send("rest-topic",msg); 
		} catch (Exception e) {
			e.printStackTrace();
			return respBodyWriter.toError("提交认证信息失败",ResponseCode.CODE_350.toCode());
		}
    	
    	return respBodyWriter.toSuccess(); 
    }  


	private void realAuthCheck(final String picZ, final String picF, final String picH, final Long uid, final String phone) {
		Thread t = new Thread(new Runnable(){
			public void run(){
				CustPicResponse responseZ = realAuthService.custPicIdentify("Z", picZ);
				IdentifyInfo info = new IdentifyInfo();

				if (responseZ.getReturnCode().equals("0000")) {
					Map<String, String> infoZMap = responseZ.getBean();
					info.setCustName(infoZMap.get("custName"));
					info.setCustCertNo(infoZMap.get("custCertNo"));
					info.setBirthday(infoZMap.get("birthday"));
					info.setAddress(infoZMap.get("address"));
					info.setGender(infoZMap.get("gender"));
					info.setNation(infoZMap.get("nation"));
				} else {
					// 验证失败 失败原因
					try {
						String reason = responseZ.getReturnMessage();
						logger.info("用户id=" + uid + "实名验证失败:" + reason);
						if (anchorService.realAuthCheck(uid, false, reason)) {
							String content = URLEncoder.encode("尊敬的用户：" + reason, "utf8");
							SmsSendUtil.sendMsg(phone, content);
						}
					} catch (Throwable t) {
						t.printStackTrace();
						logger.error("审核出错，请稍后重试！");
					}
					return;
				}
				CustPicResponse responseF = realAuthService.custPicIdentify("F", picF);
				if (responseF.getReturnCode().equals("0000")) {
					Map<String, String> infoFMap = responseF.getBean();
					info.setCertValiddate(infoFMap.get("certValiddate"));
					info.setCertExpdate(infoFMap.get("certExpdate"));
					info.setIssuingAuthority(infoFMap.get("issuingAuthority"));
				} else {
					// 验证失败 失败原因
					try {
						String reason = responseF.getReturnMessage();
						logger.info("用户id=" + uid + "实名验证失败:" + reason);
						if (anchorService.realAuthCheck(uid, false, reason)) {
							String content = URLEncoder.encode("尊敬的用户：" + reason, "utf8");
							SmsSendUtil.sendMsg(phone, content);
						}
					} catch (Throwable t) {
						t.printStackTrace();
						logger.error("审核出错，请稍后重试！");
					}
					return;
				}
				logger.info("用户id=" + uid + "实名验证第一步通过，info=" + JSONObject.fromObject(info).toString());
				// 第二步 验证身份证信息
				CustPicResponse	checkInfoResp = realAuthService.custInfoPicVerify("0", "0", info);
				if ("0000".equals(checkInfoResp.getReturnCode()) && "1".equals(checkInfoResp.getBean().get("checkResult"))) {
					// 验证身份证信息成功
					logger.info("用户id=" + uid + "实名验证第二步通过，身份证有效");
//					String token = checkInfoResp.getBean().get("token");
					// 第三步 对比人像与身份证信息
					CustPicResponse	checkResp = realAuthService.cusPicCompare("0", "0", info, null, picH, "1");
					if ("0000".equals(checkResp.getReturnCode()) && "1".equals(checkResp.getBean().get("compareResult"))) {
						// 比对成功
						logger.info("用户id=" + uid + "实名验证第三步通过，人像对比成功");
						try {
							anchorService.realAuthCheck(uid, true, "");
							String content = URLEncoder.encode("尊敬的用户：您的直播申请已经通过审核，登陆LIVE客户端开始您的直播吧！【巴士在线】", "utf8");
							SmsSendUtil.sendMsg(phone, content);
						} catch (Throwable t) {
							t.printStackTrace();
							logger.error("审核出错，请稍后重试！");
						}
					} else {
						// 验证失败 失败原因
						try {
							logger.info("用户id=" + uid + "实名验证第三步未通过，身份证与人像对比不符");
							if (anchorService.realAuthCheck(uid, false, "身份证与人像对比不符")) {
								String content = URLEncoder.encode("尊敬的用户：身份证与人像对比不符，请您重新上传清晰的照片【巴士在线】", "utf8");
								SmsSendUtil.sendMsg(phone, content);
							}
						} catch (Throwable t) {
							t.printStackTrace();
							logger.error("审核出错，请稍后重试！");
						}
					}
				} else {
					// 验证失败 失败原因
					logger.info("用户id=" + uid + "实名验证第二步未通过，身份证无效");
					try {
						anchorService.realAuthCheck(uid, false, "身份证信息验证失败");
						String content = URLEncoder.encode("尊敬的用户：您的身份证信息验证失败【巴士在线】", "utf8");
						SmsSendUtil.sendMsg(phone, content);

					} catch (Throwable t) {
						t.printStackTrace();
						logger.error("审核出错，请稍后重试！");
					}
				}
			}
		});
		t.start();
	}

    /**
	 * 上传直播封面
	 * @param file 文件
	 * @return 封面地址
	 */
	private String uploadPic(MultipartFile file,String catalog,String number) {
		String relUrl = "";
		String relPath = File.separator + catalog + File.separator
				+ DateFormatUtils.format(new Date(), "yyyy-MM-dd")
				+ File.separator;
		try {
			String fileName = file.getOriginalFilename();
			String sufix = fileName.substring(fileName.lastIndexOf("."));
			String originalFilename = DateFormatUtils.format(new Date(),
					"yyyy-MM-dd_HHmmss")+number;
			
			String compressFilename = originalFilename + "compress";

			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(
					basePath + relPath, originalFilename+sufix));
			//压缩图片
			ImageCompressUtil.saveMinPhoto(basePath + relPath+originalFilename+sufix, basePath + relPath+compressFilename+sufix, 800, 0.9d);
			relUrl = relPath + compressFilename+sufix;
		} catch (IOException e) {
			logger.error("文件上传失败", e);
		}
		return relUrl;
	}
    
}
