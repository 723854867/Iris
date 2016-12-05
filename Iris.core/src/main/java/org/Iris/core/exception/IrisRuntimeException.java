package org.Iris.core.exception;

public class IrisRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -7947964000525855099L;

	public IrisRuntimeException(String message) {
		super(message);
	}
	
	public IrisRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
