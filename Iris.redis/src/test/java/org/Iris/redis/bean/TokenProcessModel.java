package org.Iris.redis.bean;

import java.text.MessageFormat;

import org.Iris.util.common.uuid.AlternativeJdkIdGenerator;
import org.Iris.util.lang.DateUtils;

public class TokenProcessModel {

	private String merchantId;
	private String token;
	private String merchantLockKey;
	private String merchantLockId;
	private String lockTimeout;
	private String tokenMerchantMapKey;
	private String merchantDataKey;
	private String time;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMerchantLockKey() {
		return merchantLockKey;
	}

	public void setMerchantLockKey(String merchantLockKey) {
		this.merchantLockKey = merchantLockKey;
	}

	public String getMerchantLockId() {
		return merchantLockId;
	}

	public void setMerchantLockId(String merchantLockId) {
		this.merchantLockId = merchantLockId;
	}

	public String getLockTimeout() {
		return lockTimeout;
	}

	public void setLockTimeout(String lockTimeout) {
		this.lockTimeout = lockTimeout;
	}

	public String getTokenMerchantMapKey() {
		return tokenMerchantMapKey;
	}

	public void setTokenMerchantMapKey(String tokenMerchantMapKey) {
		this.tokenMerchantMapKey = tokenMerchantMapKey;
	}

	public String getMerchantDataKey() {
		return merchantDataKey;
	}

	public void setMerchantDataKey(String merchantDataKey) {
		this.merchantDataKey = merchantDataKey;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	/**
	 * 替换 token，用在 login 和 create
	 * 
	 * @param merchantId
	 * @param token
	 * @return
	 */
	public static final TokenProcessModel getMerchantAndReplaceToken(long merchantId, String token) { 
		TokenProcessModel model = new TokenProcessModel();
		model.setMerchantId(String.valueOf(merchantId));
		model.setLockTimeout("5000000");
		model.setMerchantLockId(AlternativeJdkIdGenerator.INSTANCE.generateId().toString());
		model.setMerchantLockKey(MessageFormat.format("string:cache:merchant:{0}:lock", merchantId));
		model.setTokenMerchantMapKey("hash:cache:token:merchant");
		model.setMerchantDataKey(MessageFormat.format("hash:db:merchant:{0}", merchantId));
		model.setToken(token);
		model.setTime(String.valueOf(DateUtils.currentTime()));
		return model;
	}
	
	/**
	 * 删除 token，用在踢出用户或者用户注销
	 * 
	 * @param merchantId
	 * @return
	 */
	public static final TokenProcessModel getMerchantAndDelToken(String merchantId) { 
		TokenProcessModel model = new TokenProcessModel();
		model.setMerchantId(merchantId);
		model.setLockTimeout("5000000");
		model.setMerchantLockId(AlternativeJdkIdGenerator.INSTANCE.generateId().toString());
		model.setMerchantLockKey(MessageFormat.format("string:cache:merchant:{0}:lock", merchantId));
		model.setTokenMerchantMapKey("hash:cache:token:merchant");
		model.setMerchantDataKey(MessageFormat.format("hash:db:merchant:{0}", merchantId));
		model.setTime(String.valueOf(DateUtils.currentTime()));
		return model;
	}
	
	/**
	 * 通过 token 获取商户，并且同时锁定商户
	 * 
	 * @param token
	 * @return
	 */
	public static final TokenProcessModel getMerchantModelWithLock(String token) { 
		TokenProcessModel model = new TokenProcessModel();
		model.setLockTimeout("5000000");
		model.setMerchantLockId(AlternativeJdkIdGenerator.INSTANCE.generateId().toString());
		model.setMerchantLockKey("string:cache:merchant:{0}:lock");
		model.setTokenMerchantMapKey("hash:cache:token:merchant");
		model.setMerchantDataKey("hash:db:merchant:{0}");
		model.setToken(token);
		return model;
	}
	
	/**
	 * 仅仅通过 token 获取商户，并不会锁定商户，可能获取之后商户的 token 已经被修改了
	 * 
	 * @param token
	 * @return
	 */
	public static final TokenProcessModel getMerchantModelWithoutLock(String token) { 
		TokenProcessModel model = new TokenProcessModel();
		model.setTokenMerchantMapKey("hash:cache:token:merchant");
		model.setMerchantDataKey("hash:db:merchant:{0}");
		model.setToken(token);
		return model;
	}
}
