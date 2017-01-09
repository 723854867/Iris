package com.busap.vcs.service.mipush;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class MipushConfig {

	public static String ANDROID_APP_ID;
	public static String ANDROID_APP_KEY;
	public static String ANDROID_APP_SECRET;
	public static String ANDROID_PACKAGE;
	
	public static String IOS_APP_ID;
	public static String IOS_APP_KEY;
	public static String IOS_APP_SECRET;
	public static String IOS_APP_SECRET2;
	public static String IOS_PACKAGE;
	public static Boolean IOS_ISTEST = true;
		
	public static Integer RETRIES = 0;
	
	static{
		String dir = MipushConfig.class.getClassLoader().getResource("/").getPath();
		File fDir = new File(dir);
		String parentDir = fDir.getParent();
		String producerConfig = parentDir + File.separator + "mipush.properties";
//		String dir = System.getProperty("user.dir");
//		File fDir = new File(dir);
//		String parentDir = fDir.getParent();
//		String producerConfig = parentDir+File.separator+"mipush.properties";
		Properties properties = new Properties();
		try {
			InputStream is = new FileInputStream(producerConfig);
			properties.load(is);
		} catch (FileNotFoundException e) {
			System.out.println("file "+producerConfig+" not found.");
			try {
				properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("mipush.properties"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		ANDROID_APP_ID = properties.getProperty("mipush.android.appid");
		ANDROID_APP_KEY = properties.getProperty("mipush.android.appkey");
		ANDROID_APP_SECRET = properties.getProperty("mipush.android.appsecret");
		ANDROID_PACKAGE = properties.getProperty("mipush.android.package");
		
		IOS_APP_ID = properties.getProperty("mipush.ios.appid");
		IOS_APP_KEY = properties.getProperty("mipush.ios.appkey");
		IOS_APP_SECRET = properties.getProperty("mipush.ios.appsecret");
		IOS_APP_SECRET2 = properties.getProperty("mipush.ios.appsecret2");
		IOS_PACKAGE = properties.getProperty("mipush.ios.package");
		IOS_ISTEST = Boolean.valueOf(properties.getProperty("mipush.ios.istest"));
		
		RETRIES = StringUtils.isNotBlank(properties.getProperty("mipush.retries"))?Integer.parseInt(properties.getProperty("mipush.retries")):0;
		
	}

}
