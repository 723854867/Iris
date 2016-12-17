package org.Iris.app.jilu.common;

import org.Iris.app.jilu.common.model.Config;

public class AppConfig extends Config {
	
	public static final int CREATE_WAIT_TIMEOUT					= 180;

	private static int captchaDigit;							// 验证码位数
	private static int captchaLifeTime;							// 验证码有效时间
	private static int captchaCountMaximum;						// 验证码获取次数限制，一般和 codeCountLifeTime 一起限制验证码的获取频率
	private static int captchaCountLifeTime;					// 验证码次数累加超时时间，超过该时间没有获取验证码则次数清零
	
	private static String aliyunOssBucket;						// 阿里云 oss 对应的 bucket name
	private static String aliyunStsRoleArn;						// 阿里云 sts 的 roleArn
	private static String aliyunOssEndpoint;					// 阿里云 oss 的endpoint
	private static String aliyunOssFolderPrefix;				// 阿里云 oss 文件夹前缀
	
	public static int getCaptchaDigit() {
		return captchaDigit;
	}
	
	public static void setCaptchaDigit(int captchaDigit) {
		AppConfig.captchaDigit = captchaDigit;
	}
	
	public static int getCaptchaLifeTime() {
		return captchaLifeTime;
	}
	
	public static void setCaptchaLifeTime(int captchaLifeTime) {
		AppConfig.captchaLifeTime = captchaLifeTime;
	}
	
	public static int getCaptchaCountMaximum() {
		return captchaCountMaximum;
	}
	
	public static void setCaptchaCountMaximum(int captchaCountMaximum) {
		AppConfig.captchaCountMaximum = captchaCountMaximum;
	}
	
	public static int getCaptchaCountLifeTime() {
		return captchaCountLifeTime;
	}
	
	public static void setCaptchaCountLifeTime(int captchaCountLifeTime) {
		AppConfig.captchaCountLifeTime = captchaCountLifeTime;
	}
	
	public static String getAliyunOssBucket() {
		return aliyunOssBucket;
	}
	
	public static void setAliyunOssBucket(String aliyunOssBucket) {
		AppConfig.aliyunOssBucket = aliyunOssBucket;
	}
	
	public static String getAliyunStsRoleArn() {
		return aliyunStsRoleArn;
	}
	
	public static void setAliyunStsRoleArn(String aliyunStsRoleArn) {
		AppConfig.aliyunStsRoleArn = aliyunStsRoleArn;
	}
	
	public static String getAliyunOssEndpoint() {
		return aliyunOssEndpoint;
	}
	
	public static void setAliyunOssEndpoint(String aliyunOssEndpoint) {
		AppConfig.aliyunOssEndpoint = aliyunOssEndpoint;
	}
	
	public static void setAliyunOssFolderPrefix(String aliyunOssFolderPrefix) {
		AppConfig.aliyunOssFolderPrefix = aliyunOssFolderPrefix;
	}
	
	public static String getAliyunOssFolderPrefix() {
		return aliyunOssFolderPrefix;
	}
}
