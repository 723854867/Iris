package org.Iris.core.exception;

public class AssertFailedException extends IrisRuntimeException {

	private static final long serialVersionUID = 11728188967378339L;
	
	protected Object expected;
	protected Object actual;

	public AssertFailedException(Object expected, Object actual) {
		super("Assert failure, expected = " + expected + ", actual = " + actual);
	}
}
