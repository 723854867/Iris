package org.Iris.util.lang;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	
	public static final int MONTH_SECONDS = 24 * 30 * 3600;
	public static final int DAY_SECONDS = 24 * 3600;

	public static final String yyyyMMdd = "yyyyMMdd";
	public static final String ISO8601_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	
	public static final TimeZone TIMEZONE_UTC	= TimeZone.getTimeZone("UTC");
	
	public static int currentTime() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	/**
	 * 获取下一个零点 unix 时间戳
	 * 
	 * @return
	 */
	public static int nextZeroTime() {
		return zeroTime() + DAY_SECONDS;
	}
	
	/**
	 * 获取当天零点 unix 时间戳
	 * 
	 * @return
	 */
	public static int zeroTime() { 
		return zeroTime(System.currentTimeMillis());
	}
	
	/**
	 * 获取指定 unix 时间戳的零点的 10 位 unix 时间戳
	 * 
	 * @param millis
	 * @return
	 */
	public static int zeroTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return (int) (calendar.getTimeInMillis() / 1000);
	}
	
	public static boolean isToday(long time) {
		return zeroTime() == zeroTime(time);
	}
	
	public static long getTime(String date, String format, TimeZone zone) { 
		DateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(zone);
		try {
			return df.parse(date).getTime();
		} catch (ParseException e) {
			return 0;
		}
	}
	
	/**
	 * {@link #getUTCDate(long)}
	 * @return
	 */
	public static String getUTCDate() { 
		return getDate(ISO8601_UTC, System.currentTimeMillis(), TIMEZONE_UTC);
	}
	
	/**
	 * 获取 date1 和 date2 之间的时间差值
	 * 
	 * @param date1
	 * @param date2
	 * @param format
	 * @param zone
	 * @return
	 */
	public static long getTimeGap(String date1, String date2, String format, TimeZone zone) {
		return getTime(date1, format, zone) - getTime(date2, format, zone);
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
