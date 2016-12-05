package org.Iris.util.lang;

public class StringUtil {

	public static final String EMPTY = "";

	/**
	 * 去掉字符串的头尾空格，任何空格都算，比如制表符，换行符，回车，全角空格等
	 * 
	 * @param str
	 * @return
	 */
	public static String trimWhitespace(String str) {
		if (!hasLength(str))
			return str;
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0)))
			sb.deleteCharAt(0);
		while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1)))
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * @see #hasLength(CharSequence)
	 * @param str
	 * @return
	 */
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	/**
	 * 任何空格都算在内
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * 替换字符串
	 * 
	 * @param string
	 * @param oldPattern
	 * @param newPattern
	 * @return
	 */
	public static String replace(String string, String oldPattern, String newPattern) {
		if (!hasLength(string) || !hasLength(oldPattern) || newPattern == null)
			return string;
		StringBuilder sb = new StringBuilder();
		int pos = 0;
		int index = string.indexOf(oldPattern);
		int patLen = oldPattern.length();
		while (index >= 0) {
			sb.append(string.substring(pos, index));
			sb.append(newPattern);
			pos = index + patLen;
			index = string.indexOf(oldPattern, pos);
		}
		sb.append(string.substring(pos));
		return sb.toString();
	}
}
