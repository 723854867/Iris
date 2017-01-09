package com.busap.vcs.chat.util;

import java.util.HashMap;

public class UrlParseUtil {


	/**
	 * 
	 * @param cipher
	 * @return
	 */
	public static HashMap<String, String> parseCipher(String cipher,
			String key, String algorithm) {
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		if (cipher == null || cipher.length() == 0) {
			return paramMap;
		}

		String param = CodecUtil.decrypt(cipher, key, algorithm);
		String paramName = null;
		String paramValue = null;
		if (param.indexOf("&") != -1) {
			String[] params = param.split("&");
			for (int i = 0; i < params.length; i++) {
				if (params[i] != null && params[i].length() > 1
						&& params[i].indexOf("=") != -1) {
					paramName = params[i].split("=")[0];
					paramValue = params[i].replace(paramName + "=", "");
					paramMap.put(paramName, paramValue);
				}
			}
		} else {
			if (param.length() > 1 && param.indexOf("=") != -1) {
				paramName = param.split("=")[0];
				paramValue = param.replace(paramName + "=", "");
				paramMap.put(paramName, paramValue);
			}
		}
		return paramMap;
	}
	
	public static HashMap<String, String> parseCipher(String cipher,
			String key, String algorithm, String iphoneKey) {
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		if (cipher == null || cipher.length() == 0) {
			return paramMap;
		}
		
		String param = CodecUtil.decrypt(cipher, key, algorithm, iphoneKey);
		String paramName = null;
		String paramValue = null;
		if (param.indexOf("&") != -1) {
			String[] params = param.split("&");
			for (int i = 0; i < params.length; i++) {
				if (params[i] != null && params[i].length() > 1
						&& params[i].indexOf("=") != -1) {
					paramName = params[i].split("=")[0];
					paramValue = params[i].replace(paramName + "=", "");
					paramMap.put(paramName, paramValue);
				}
			}
		} else {
			if (param.length() > 1 && param.indexOf("=") != -1) {
				paramName = param.split("=")[0];
				paramValue = param.replace(paramName + "=", "");
				paramMap.put(paramName, paramValue);
			}
		}
		return paramMap;
	}
	
	public static HashMap<String, String> paramString2obj(String paramString) {
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		if (paramString == null || paramString.length() == 0) {
			return paramMap;
		}

		String paramName = null;
		String paramValue = null;
		if (paramString.indexOf("&") != -1) {
			String[] params = paramString.split("&");
			for (int i = 0; i < params.length; i++) {
				if (params[i] != null && params[i].length() > 1
						&& params[i].indexOf("=") != -1) {
					paramName = params[i].split("=")[0];
					paramValue = params[i].replace(paramName + "=", "");
					paramMap.put(paramName, paramValue);
				}
			}
		} else {
			if (paramString.length() > 1 && paramString.indexOf("=") != -1) {
				paramName = paramString.split("=")[0];
				paramValue = paramString.replace(paramName + "=", "");
				paramMap.put(paramName, paramValue);
			}
		}
		return paramMap;
	}
}
