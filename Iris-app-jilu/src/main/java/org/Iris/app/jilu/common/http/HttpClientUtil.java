package org.Iris.app.jilu.common.http;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClientUtil {

	static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	public static URI setUri(String http,String host,String path){
		URIBuilder builder = new URIBuilder();
		builder.setScheme(http);
		builder.setHost(host);
		builder.setPath(path);
		try {
			return builder.build();
		} catch (URISyntaxException e) {
			logger.error(".net server uri initial failure, system will closed! {}", e);
			return null;
		}
	}
	
	public static String getUrl(String url,List<String> params){
		String str = url;
		if (params==null || params.size() == 0) {
			return url;
		}else{
			for(int i= 0;i<params.size();i++){
				if(i==0)
					str+="?"+params.get(i);
				str+="&"+params.get(i);
			}
			return str;
		}
	}
	
	public static HttpPost getPost(ApiUri apiUri){
		return new HttpPost(setUri(apiUri.getHttp(),apiUri.getHost(),apiUri.getPath()));
	}
	
	public static HttpGet getHttpGet(ApiUri apiUri){
		return new HttpGet(setUri(apiUri.getHttp(),apiUri.getHost(),apiUri.getPath()));
	}
	
	/**
	 * 获取当前网络ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
}
