package com.busap.vcs.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class DuibaUtil {

	private static final String APP_KEY = "ex9A9saCscVsgzxVY98i4SSyX5r";

	private static final String APP_SECRET = "4JmpURXWp6LYzZexXSFm5zRjr32P";
	
	private static final String AUTO_LOGIN_URL = "http://www.duiba.com.cn/autoLogin/autologin?";

	/**
	 * 生成兑吧请求地址
	 * 
	 * @param map
	 * @return
	 */
	public static String createRequestUrl(Map<String, String> map){
		return AUTO_LOGIN_URL + createParamString(map);
	}
	
	/**
	 * 生成请求参数串 
	 * 
	 * @param map
	 * @return
	 */
	public static String createParamString(Map<String, String> map) {
		String paramString = "";
		
		map.put("appKey", APP_KEY);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals("redirect")) {
				try {
					paramString += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),"utf-8") + "&";
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				paramString += entry.getKey() + "=" + entry.getValue() + "&";
			}
		}
		map.put("appSecret", APP_SECRET);
		
		//请求参数中加入appSecret，对key进行排序，然后用对应的value值生成sign
		Map<String, String> resultMap = sortMapByKey(map);
		paramString += "sign=" + toMD5(resultMap);
		return paramString;
	}

	/**
	 * 使用 Map按key进行排序
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, String> sortMapByKey(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}

		Map<String, String> sortMap = new TreeMap<String, String>(
				new MapKeyComparator());

		sortMap.putAll(map);

		return sortMap;
	}
	
	
	/**
	 * 签名生成MD5
	 * 
	 * @param map
	 * @return
	 */
	public static String toMD5(Map<String, String> map) {
		String inStr = "";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			inStr += entry.getValue();
		}
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

}

/**
 * TreeMap key 排序
 */
class MapKeyComparator implements Comparator<String> {

	@Override
	public int compare(String str1, String str2) {
		return str1.compareTo(str2);
	}
}
