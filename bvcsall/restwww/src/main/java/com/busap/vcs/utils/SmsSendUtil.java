package com.busap.vcs.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

//该Demo主要解决Java在Linux等环境乱码的问题
public class SmsSendUtil {
	private static final Logger logger = LoggerFactory.getLogger(SmsSendUtil.class);

	private static final String sn = "SDK-BBX-010-18410";
	private static final String pwd = "ea2da@66";
	private static Client client; 
	static {
		try { 
			client = new Client(sn, pwd);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.toString(), ex);
		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException { 
		sendMsg("13439054927","123455",2); 
	}
	 
	static String regEx_mobile = "^(130|131|132|133|134|135|136|137|138|139|147|150|151|152|153|155|156|157|158|159|180|182|185|186|187|188|189|1700|1705|1709)"; 
	
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
	
	public static void sendMsg(String phone, String code,int type) {
		try {  
			String reg = "欢迎您注册LIVE，本次注册激活码为：" + code + "(有效时间：5分钟)【巴士在线】"; 
			String use = "欢迎您使用LIVE，本次找回密码激活码为：" + code + "(有效时间：5分钟)【巴士在线】"; 
			String result_mt ="" ;
			if(type==1)
				result_mt = client.mdSmsSend_u(phone, reg, "", "", "");
			else
				result_mt = client.mdSmsSend_u(phone, use, "", "", ""); 
			logger.info(phone + " send result:" + result_mt);
			if (result_mt.startsWith("-") || result_mt.equals(""))// 发送短信，如果是以负号开头就是发送失败。
			{
				logger.error("发送失败！返回值为：" + result_mt + "请查看webservice返回值对照表");
				return;
			}
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
		}
	} 
}
