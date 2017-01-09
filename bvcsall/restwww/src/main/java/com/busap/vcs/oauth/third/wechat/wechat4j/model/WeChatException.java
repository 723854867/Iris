package com.busap.vcs.oauth.third.wechat.wechat4j.model;

import com.busap.vcs.oauth.third.wechat.wechat4j.org.json.JSONException;
import com.busap.vcs.oauth.third.wechat.wechat4j.org.json.JSONObject;

/**
 * 
 * @author shouchen.shan@10020.cn
 *
 */
public class WeChatException extends Exception {

	private int statusCode = -1;
	private int errorCode = -1;
	private String request;
	private String error;
	private static final long serialVersionUID = 434455229354708573L;

	public WeChatException(String msg) {
		super(msg);
	}

	public WeChatException(Exception cause) {
		super(cause);
	}

	public WeChatException(String msg, int statusCode) throws JSONException {
		super(msg);
		this.statusCode = statusCode;
	}

	public WeChatException(String msg, JSONObject json, int statusCode) throws JSONException {
		super(msg + "\n error:" + json.getString("error") + " error_code:" + json.getInt("error_code") + json.getString("request"));
		this.statusCode = statusCode;
		this.errorCode = json.getInt("error_code");
		this.error = json.getString("error");
		this.request = json.getString("request");

	}

	public WeChatException(String msg, Exception cause) {
		super(msg, cause);
	}

	public WeChatException(String msg, Exception cause, int statusCode) {
		super(msg, cause);
		this.statusCode = statusCode;

	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getRequest() {
		return request;
	}

	public String getError() {
		return error;
	}

}
