package org.Iris.core.exception;

public class ConfigLoadFailedException extends IrisRuntimeException {

	private static final long serialVersionUID = -1450420193275161087L;

	public ConfigLoadFailedException(String message) {
		super(message);
	}
	
	public ConfigLoadFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
