package org.Iris.redis.exception;

import org.Iris.core.exception.IrisException;

public class RedisException extends IrisException {

	private static final long serialVersionUID = 7176979538899181652L;

	public RedisException(String message, Throwable cause) {
		super(message, cause);
	}
}
