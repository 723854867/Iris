package org.Iris.util.lang;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

	public static final String yyyyMMdd = "yyyyMMdd";
	public static final String ISO8601_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	/**
	 * {@link #getUTCDate(long)}
	 * @return
	 */
	public static String getUTCDate() { 
		return getDate(ISO8601_UTC, System.currentTimeMillis(), TimeZone.getTimeZone("UTC"));
	}
	
	/**
	 * 获取指定 unix 时间的 UTC 时间格式
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String getUTCDate(long timestamp) { 
		return getDate(ISO8601_UTC, timestamp, TimeZone.getTimeZone("UTC"));
	}
	
	/**
	 * {@link #getDate(String, long, TimeZone)}
	 * @param format
	 * @return
	 */
	public static String getDate(String format) {
		return getDate(format, System.currentTimeMillis(), TimeZone.getDefault());
	}
	
	/**
	 * 将 unix 时间戳转换为指定格式的时间字符串
	 * 
	 * @param format
	 * @param timestamp
	 * @param timeZone
	 * @return
	 */
	public static String getDate(String format, long timestamp, TimeZone timeZone) {
		DateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(timeZone);
		return df.format(new Date(timestamp));
	}
}
