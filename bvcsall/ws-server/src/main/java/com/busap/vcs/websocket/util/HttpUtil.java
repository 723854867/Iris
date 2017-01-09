package com.busap.vcs.websocket.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpUtil {
	
	public static Map<String, String> parseUri(String uri){
		Map<String, String> params = new HashMap<String, String>();
		if(uri != null && uri.indexOf("?") > 0){
			String temp = uri.substring(uri.indexOf("?") + 1);
			String[] paramStr = temp.split("&");
			params = new HashMap<String, String>();
			for(int i = 0 ; i < paramStr.length; i ++ ){
				String tempStr = paramStr[i];
				if(tempStr != null && tempStr.trim().length() > 0){
					String[] result = tempStr.split("=", 2);
					if(result.length == 2){
						params.put(result[0], result[1]);
					}
				}
			}
		}
		return params;
	}
	public static String getOptName (String url){
		try {
			if(url != null){
				if(url.indexOf("?")>=0)
					return url.substring(url.indexOf("/") + 1, url.indexOf("?"));
				else
					return url.substring(url.indexOf("/") + 1, url.length());
			}
		} catch (Exception e) {
		}
		
		return "";
	}
	public static String mapToString(Map<Object,Object> map){
		StringBuffer result = new StringBuffer();
		if(null != map){
			Set<Object> keySet = map.keySet();
			int count = 0;
			for (Object object : keySet) {
				if(count > 0){
					result.append(",");
				}
				result.append(object.toString());
				result.append("=");
				result.append(map.get(object));
				count ++;
			}
		}
		return result.toString();
	}
	
	/**
	 * 获取url返回的内容
	 * @param targetUrl http url
	 * @param timeout 超时时间（秒）
	 * @return
	 */
	public static String doGet(String targetUrl, int timeout){
		
		URL url = null;
		HttpURLConnection conn = null;
		StringBuffer sResult = new StringBuffer();
		BufferedReader in = null;
		try {
			url = new URL(targetUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(timeout * 1000);
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String sCurrentLine = null;
			while ((sCurrentLine = in.readLine()) != null) {
				sResult.append(sCurrentLine.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sResult.toString();
	}
	
	public static void main(String[] args){
//		String uri = "sh?ccid=852507&amp;pid=7&amp;v=1073860&amp;videoid=110438664&amp;tseqid=MA==&amp;wxf=1&amp;c=np0HZc/8QtwictXM+6gN5TjAE/96AhI1nHzup4dwciABjc2EHChctrtAsB8BDrSR&amp;sto=3";
//		uri = uri.replaceAll("&amp;", "&");
//		System.out.println(uri);
//		Map<String, String> parameters = HttpUtil.parseUri(uri);
//		Set<String> keys = parameters.keySet();
//		for (String key : keys) {
//			System.out.println(key + "=" + parameters.get(key));
//			
//		}
		System.out.println(getOptName(""));
	}
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                