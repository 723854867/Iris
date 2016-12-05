package org.Iris.util.common;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

	public static String getStackTrace(final Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
}
