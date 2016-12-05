package org.Iris.core.exception;

public class IrisException extends Exception {

	private static final long serialVersionUID = 5634450529393839633L;

	public IrisException(String message) {
		super(message);
	}
	
	public IrisException(String message, Throwable cause) {
		super(message, cause);
	}
}
