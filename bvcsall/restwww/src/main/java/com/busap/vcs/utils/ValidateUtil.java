package com.busap.vcs.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.service.JedisService;

@Service("validateUtil")
public class ValidateUtil {

	private Logger logger = LoggerFactory.getLogger(ValidateUtil.class); 
		
	@Autowired
	JedisService jedisService;

	private final String appSecret = "a2a866274137a1ac454a73886cf77cdb";

	public boolean isLegal(String reqTimestamp, String reqSignature,HttpServletRequest request) {
		logger.info("ValidateUtil,reqTimestamp={},reqSignature={}",reqTimestamp,reqSignature);
		String flag = jedisService.get(BicycleConstants.CLIENT_VALIDATE_SWITCH);
		if (StringUtils.isNotBlank(flag) && "1".equals(flag)) {
			if (StringUtils.isBlank(reqTimestamp)
					|| StringUtils.isBlank(reqSignature)) {
				return false;
			}
			if (!MD5(appSecret + reqTimestamp).equals(reqSignature)
					&& !MD5(reqTimestamp + appSecret).equals(reqSignature)) {
				logger.info("ValidateUtil,验证客户端签名失败");
				return false;
			}
			Long now = System.currentTimeMillis(); //获得当前时间
			Long timestamp = Long.valueOf(reqTimestamp);
			Long diff = now - timestamp;
			if (Math.abs(diff) > 1000 * 60 * 8) { // 请求时间在当前时间正负8分钟之外，为非法请求
				logger.info("ValidateUtil,过期请求,diff={}",diff);
				return false;
			}
			String localOutIp = getLocalOutIp(request);
			Long index = jedisService.zrank("req_info", reqSignature+"|"+localOutIp); // 如果该签名已经存在，为非法请求
			if (index != null && index.intValue() >= 0) {
				logger.info("ValidateUtil,重复请求,reqSignature={}",reqSignature);
				return false;
			}
			jedisService.setValueToSortedSetAndDel("req_info",
					Double.valueOf(now), reqSignature+"|"+localOutIp,
					now - 1000 * 60 * 10); // 清除10分钟之前的请求
		}
		return true;
	}
	
	public String getLocalOutIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		try {
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getHeader("Proxy-Client-IP");
			}
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getRemoteAddr();
			}
			logger.info("getLocalOutIp,allIp={}",ip);
			if (StringUtils.isNotBlank(ip)) {
				String[] ss = ip.split(",");
				if (ss.length > 1) {
					ip = ss[0];
				}
			}
			logger.info("getLocalOutIp,ip={}",ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	private String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		ValidateUtil v = new ValidateUtil();
		Long now = System.currentTimeMillis();
//		now = now -1000*60*8;
		System.out.println(now);
		System.out.println(v.MD5("1466505822229"+v.appSecret));
	}
}
