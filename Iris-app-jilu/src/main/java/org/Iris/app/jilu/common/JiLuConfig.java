package org.Iris.app.jilu.common;

import org.Iris.app.jilu.model.Env;

public class JiLuConfig {

	private static Env env;						
	private static int captchaDigit;							// 验证码位数
	private static int captchaLifeTime;							// 验证码有效时间
	private static int captchaCountMaximum;						// 验证码获取次数限制，一般和 codeCountLifeTime 一起限制验证码的获取频率
	private static int captchaCountLifeTime;					// 验证码次数累加超时时间，超过该时间没有获取验证码则次数清零
	
	public static Env getEnv() {
		return env;
	}
	
	public static void setEnv(String env) {
		JiLuConfig.env = Env.match(env);
	}
	
	public static int getCaptchaDigit() {
		return captchaDigit;
	}
	
	public static void setCaptchaDigit(int captchaDigit) {
		JiLuConfig.captchaDigit = captchaDigit;
	}
	
	public static int getCaptchaLifeTime() {
		return captchaLifeTime;
	}
	
	public static void setCaptchaLifeTime(int captchaLifeTime) {
		JiLuConfig.captchaLifeTime = captchaLifeTime;
	}
	
	public static int getCaptchaCountMaximum() {
		return captchaCountMaximum;
	}
	
	public static void setCaptchaCountMaximum(int captchaCountMaximum) {
		JiLuConfig.captchaCountMaximum = captchaCountMaximum;
	}
	
	public static int getCaptchaCountLifeTime() {
		return captchaCountLifeTime;
	}
	
	public static void setCaptchaCountLifeTime(int captchaCountLifeTime) {
		JiLuConfig.captchaCountLifeTime = captchaCountLifeTime;
	}
}
