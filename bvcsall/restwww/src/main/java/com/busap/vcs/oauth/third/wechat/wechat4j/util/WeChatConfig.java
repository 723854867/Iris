package com.busap.vcs.oauth.third.wechat.wechat4j.util;

import java.util.Properties;
/**
 * 
 * @author shouchen.shan@10020.cn
 *
 */
public class WeChatConfig {
	public static String APP_ID = "client_id";
	public static String APP_SERCRET = "app_sercret";
	public static String URL_BASE = "baseURL";
	public static String URL_ACCESS_TOKEN = "accessTokenURL";
	public static String URL_AUTHORIZE = "authorizeURL";

	private static Properties props = new Properties();
	private Properties pros = new Properties();

	public void setPros(Properties pros) {
		this.pros = pros;
		WeChatConfig.props = this.pros;
		this.pros = null;// 释放资源
	}

	public static String getValue(String key) {
		return props.getProperty(key);
	}

	public static void updateProperties(String key, String value) {
		props.setProperty(key, value);
	}
}
