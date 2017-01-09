package com.busap.vcs.service.utils.encrypt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class EncryptUtil {
	private static final String DEFAULT_PATTERN = "yyyyMMddHHmmssSSS";
	
	 /**
	  * @param pirKey 私钥
	  * @param busiCode
	  * @param requestSource
	  * @param userName
	  * @param passWord
	  * @return
	  */
	public static String getSignature(String pirKey,String requestSource,String transactionID,String userId,String userPwd,String sn){
		String source=requestSource+transactionID+userId+userPwd+sn;
		return  new Rsa.Encoder(pirKey).encode(source);
	}
	/**
	 * 
	 * @param key
	 * @param json
	 * @return
	 * @throws Exception 
	 */
	public static String getEncryptParams(String key,String json) throws Exception{
		return new RealNameMsDesPlus(key).encrypt(json);
	}
	
	public static String getSn(){
		
		String date = new SimpleDateFormat(DEFAULT_PATTERN).format(new Date());
		String random = generateRandomCode();// 生成6位随机数 
		String sn =  date + random;
		return sn;
	}
	
	/**
	 * 生成6位随机数
	 */
	private static String generateRandomCode(){
		return String.valueOf(new Random().nextInt(899999) + 100000);
	}
}
