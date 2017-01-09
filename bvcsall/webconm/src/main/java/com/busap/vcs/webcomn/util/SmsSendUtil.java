package com.busap.vcs.webcomn.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

//该Demo主要解决Java在Linux等环境乱码的问题
public class SmsSendUtil {
	private static final Logger logger = LoggerFactory.getLogger(SmsSendUtil.class);

//	private static final String sn = "SDK-BBX-010-18410";
//	private static final String pwd = "ea2da@66";
//	private static Client client; 
//	static {
//		try { 
//			client = new Client(sn, pwd);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			logger.error(ex.toString(), ex);
//		}
//	}

	static String regEx_mobile = "^1";
	
	public static boolean isPhone(String s){
		Pattern regEx_number = Pattern.compile("^\\d{11}$"); 
		Matcher mat = regEx_number.matcher(s);  
		if(!mat.find()){
			return false;
		}  
		Pattern pat_mobile = Pattern.compile(regEx_mobile);
		mat = pat_mobile.matcher(s);
		if(!mat.find()){
			return false;
		} 
		return true;
	}  
	public static boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches();   
	 } 
	
	public static boolean sendMsg(String phone, String code,int type) {
		try {  
			String reg = "欢迎您注册LIVE，本次注册激活码为：" + code + "(有效时间：5分钟)【巴士在线】"; 
			String use = "欢迎您使用LIVE，本次找回密码激活码为：" + code + "(有效时间：5分钟)【巴士在线】"; 
			String anchor = "欢迎您使用LIVE，直播认证验证码为：" + code + "(有效时间：5分钟)【巴士在线】"; 
			String band = "欢迎您使用LIVE，绑定手机号验证码为：" + code + "(有效时间：5分钟)【巴士在线】"; 
			String footballGirl = "欢迎您使用LIVE，报名验证码为：" + code + "(有效时间：5分钟)【巴士在线】"; 
			String result_mt ="" ;
			Client client = Client.getInstance();
			if(type == 1) {
				result_mt = client.mdsmssend(phone, reg);
			} else if (type == 2) {
				result_mt = client.mdsmssend(phone, use); 
			} else if (type == 3) {
				result_mt = client.mdsmssend(phone, anchor); 
			} else if (type == 4) {
				result_mt = client.mdsmssend(phone, band); 
			} else if (type == 5) {
				result_mt = client.mdsmssend(phone, footballGirl); 
			}
			//logger.info(phone + " send result:" + result_mt);
			
			// 发送短信，如果是以负号开头就是发送失败。
			if (result_mt.startsWith("-") || result_mt.equals("")) {
				logger.error("发送失败！返回值为：" + result_mt + "请查看webservice返回值对照表");
				return false;
			}
			return true;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return false;
		}
	}

	/**
	 * 发送短信
	 * @param phone 手机号码
	 * @param content 短信内容
	 * @return boolean true成功 false失败
	 */
	public static boolean sendMsg(String phone, String content) {
		try {
			Client client = Client.getInstance();
			String result = client.mdsmssend(phone, content);
			// 发送短信，如果是以负号开头就是发送失败。
			if (result.startsWith("-") || result.equals("")) {
				logger.error("发送失败！返回值为：" + result + "请查看webservice返回值对照表");
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error(e.toString(), e);
			e.printStackTrace();
			return false;
		}
	}

}
