package org.Iris.util.lang;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DateUtils {

	private final static String GMT_ZONE = "GMT";
	
	public static final String yyyyMMdd = "yyyyMMdd";
	public static final String ISO8601_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	/**
	 * 获取当前时间的 iso 8601 格式
	 * 
	 * @return
	 */
	public static String getISO8601Time() {
        return getISO8601Time(new Date());
    }
	
	/**
	 * 获取指定时间的 iso 8601 格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getISO8601Time(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_UTC);
        df.setTimeZone(new SimpleTimeZone(0, GMT_ZONE));
        return df.format(date);
    }
	
	public static int currentTime() {
		return (int) (System.currentTimeMillis() / 1000);
	}
	
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
