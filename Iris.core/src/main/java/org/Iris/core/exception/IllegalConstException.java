package org.Iris.core.exception;

import org.Iris.core.consts.IrisConst;

public class IllegalConstException extends IrisRuntimeException {

	private static final long serialVersionUID = -3701621791996004997L;
	
	private IrisConst<?> constant;
	private boolean nil;
	
	private IllegalConstException(String message, IrisConst<?> constant) {
		super(message);
		this.constant = constant;
	}
	
	private IllegalConstException(String message, Throwable cause, IrisConst<?> constant) {
		super(message, cause);
		this.constant = constant;
	}
	
	public IrisConst<?> constant() {
		return constant;
	}
	
	public boolean isNil() {
		return nil;
	}
	
	public void setNil(boolean nil) {
		this.nil = nil;
	}
	
	public static final IllegalConstException nullException(IrisConst<?> constant) {
		IllegalConstException e = exception(constant.key() + " not exist!", constant);
		e.setNil(true);
		return e;
	}
	
	public static final IllegalConstException nullException(String message, IrisConst<?> constant) {
		IllegalConstException e = exception(message, constant);
		e.setNil(true);
		return e;
	}
	
	public static final IllegalConstException errorException(IrisConst<?> constant) {
		return exception(constant.key() + " error!", constant);
	}
	
	public static final IllegalConstException errorException(IrisConst<?> constant, Throwable cause) {
		return exception(constant.key() + " error!", cause, constant);
	}
	
	public static final IllegalConstException exception(String message, IrisConst<?> constant) {
		return new IllegalConstException(message, constant);
	}
	
	public static final IllegalConstException exception(String message, Throwable cause, IrisConst<?> constant) {
		return new IllegalConstException(message, cause, constant);
	}
}
