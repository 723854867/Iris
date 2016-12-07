package org.Iris.app.jilu.common.model.jms;

import java.io.Serializable;

import org.Iris.app.jilu.common.model.AccountType;

/**
 * 验证码消息
 * 
 * @author Ahab
 */
public class CaptchaMessage implements Serializable {

	private static final long serialVersionUID = 3128414792298024362L;
	
	private AccountType type;		// 账号类型
	private String account;			// 账号
	private String captcha;			// 验证码值
	
	public CaptchaMessage() {}
	
	public CaptchaMessage(AccountType type, String account, String captcha) {
		this.type = type;
		this.account = account;
		this.captcha = captcha;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
