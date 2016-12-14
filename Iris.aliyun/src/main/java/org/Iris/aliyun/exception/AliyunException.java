package org.Iris.aliyun.exception;

public class AliyunException extends RuntimeException {

	private static final long serialVersionUID = -3493836295630201473L;

	public AliyunException(String message, Throwable cause) {
		super(message, cause);
	}
}
