package org.Iris.core.exception;

import java.io.IOException;

public class IrisIOException extends RuntimeException {

	private static final long serialVersionUID = -8040299306013579320L;

	public IrisIOException(String message, IOException cause) {
		super(message, cause);
	}
}
