package com.busap.vcs.oauth.third.weibo.weibo4j.util;

import java.util.Properties;

public class WeiboConfig {
	
	public static String CLIENT_ID="client_ID";
	public static String CLIENT_SERCRET="client_SERCRET";
	public static String URI_REDIRECT="redirect_URI";
	public static String URL_BASE="baseURL";
	public static String URL_ACCESS_TOKEN="accessTokenURL";
	public static String URL_AUTHORIZE="authorizeURL";
	public static String URL_RM="rmURL";
	
	private static Properties props = new Properties(); 
	private Properties pros=new Properties();
	

	public void setPros(Properties pros) {
		this.pros=pros;
		WeiboConfig.props = this.pros;
		this.pros=null;//释放资源
	}
	public static String getValue(String key){
		return props.getProperty(key);
	}
	public static void updateProperties(String key,String value) {    
            props.setProperty(key, value); 
    } 
	
}
