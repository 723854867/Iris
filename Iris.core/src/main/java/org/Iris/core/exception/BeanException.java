package org.Iris.core.exception;

public class BeanException extends IrisRuntimeException {

	private static final long serialVersionUID = 7075752118345682318L;

	public BeanException(String message) {
		super(message);
	}

	public BeanException(String message, Throwable cause) {
		super(message, cause);
	}
}
