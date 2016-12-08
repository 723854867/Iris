package org.Iris.util;

import java.util.regex.Pattern;

public class Patterns {

	private static Pattern emailPattern        = Pattern.compile("\\w+\\x40\\w+\\x2e\\w+");
	
	public static boolean isEmail(String content) {
		return emailPattern.matcher(content).matches();
	}
}
