package org.Iris.core.util;

import org.Iris.core.exception.AssertFailedException;

public class Assert {

	public static final void isTrue(boolean expression) {
		if (expression) 
			throw new AssertFailedException(Boolean.TRUE, expression);
	}
}
