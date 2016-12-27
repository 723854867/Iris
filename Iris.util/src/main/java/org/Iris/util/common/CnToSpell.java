package org.Iris.util.common;

import org.Iris.util.lang.StringUtil;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * 中文转换成拼音
 * 
 * @author ahab
 */
public class CnToSpell {
	
	public static String convertToSpell(String chinese, boolean first) { 
		String pinyin = "";
		char[] nameChars = chinese.toCharArray();
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0, len = chinese.length(); i < len; i++) {
			String s = String.valueOf(nameChars[i]);
			if (s.matches("[\\u4e00-\\u9fa5]")) {
				try {
					if (first) 
						pinyin += PinyinHelper.toHanyuPinyinStringArray(nameChars[i], format)[0].charAt(0);
					else
						pinyin += PinyinHelper.toHanyuPinyinStringArray(nameChars[i], format)[0];
				} catch (Exception e) {
					throw new RuntimeException("Chinese to spell convertion failure!", e);
				}
			} else
				pinyin += s;
		}
		
		return pinyin.toLowerCase();
	}
	
	public static String getFirstChar(String chinese) {
		chinese = StringUtil.trimWhitespace(chinese);
		if (!StringUtil.hasText(chinese))
			return null;
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		char[] arr = chinese.toCharArray();
		String first = String.valueOf(arr[0]);
		if (first.matches("[\\u4e00-\\u9fa5]")) {
			try {
				return String.valueOf(PinyinHelper.toHanyuPinyinStringArray(arr[0], format)[0].charAt(0));
			} catch (Exception e) {
				throw new RuntimeException("Chinese to spell convertion failure!", e);
			}
		} else
			return first;
	}
}
