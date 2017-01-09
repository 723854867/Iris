package com.busap.vcs.oauth.web.exception;

import com.busap.vcs.oauth.third.weibo.weibo4j.model.WeiboException;
import com.qq.connect.QQConnectException;

public class ThirdPartException extends Exception {
	private static final long serialVersionUID = 6136981620986739755L;

	private int statusCode = -1;
	private int errorCode = -1;
	private String request;
	private String error;

	public ThirdPartException(QQConnectException qqException) {
		super(qqException.getMessage());
		this.statusCode = qqException.getStatusCode();
	}

	public ThirdPartException(WeiboException weiException) {
		super(weiException.getMessage());
		this.statusCode = weiException.getStatusCode();
		this.errorCode = weiException.getErrorCode();
		this.request = weiException.getRequest();
	}

	public ThirdPartException(String msg) {
		super(msg);
	}

	public ThirdPartException(Exception cause) {
		super(cause);
	}

	public ThirdPartException(String msg, int statusCode) {
		super(msg);
		this.statusCode = statusCode;
	}

	public ThirdPartException(String msg, Exception cause) {
		super(msg, cause);
	}

	public ThirdPartException(String msg, Exception cause, int statusCode) {
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
