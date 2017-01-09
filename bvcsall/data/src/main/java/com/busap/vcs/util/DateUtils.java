package com.busap.vcs.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by
 * User: djyin
 * Date: 12/13/13
 * Time: 1:57 PM
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);

    /**
     * Parser date.
     *
     * @param patterns the pattern
     * @param date the date
     * @return the date
     */
    public static Date parseDate(String date,String... patterns) {
        for (String pattern : patterns) {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            try {
                return format.parse(date);
            } catch (ParseException e) {
                // ignored
                log.debug("try parse Date fail.", e);
            }
        }

        return null;
    }

    /**
     * Parser date.
     *
     * @param pattern the pattern
     * @param date the date
     * @return the date
     */
    public static Date parseDate(String pattern, String date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date d = null;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            // ignored
            log.error("parseDate:", e);
        }
        return d;
    }



    /**
     * 当前时间的字符串
     *
     * @return the now as str
     */
    public static String getNowAsStr() {
        SimpleDateFormat sf = new SimpleDateFormat(DateFormatUtils.PATTEN_DEFAULT_FULL);
        return sf.format(new Date());
    }

    /**
     * 当前年月日的字符串
     *
     * @return the string
     */
    public static String getNowYMDAsStr(){
        SimpleDateFormat sf = new SimpleDateFormat(DateFormatUtils.PATTEN_DEFAULT_YMD);
        return sf.format(new Date());
    }
    
    /**
     * 当前年月日时间为23：59:59
     *
     * @return the string
     */
    public static Long getNowUnixYMDAsStr(){
        SimpleDateFormat sf = new SimpleDateFormat(DateFormatUtils.PATTEN_UNIX_YMD);
        String ymd = sf.format(new Date());
        String hms = "23:59:59";
        String result=ymd+" "+hms;
        Date epoch=new Date();
        try {
			 epoch = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(result);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Long.parseLong(epoch.toString());
    }

    /**
     * 当年时分秒的字符串
     *
     * @return the string
     */
    public static String getNowHMSAsStr(){
        SimpleDateFormat sf = new SimpleDateFormat(DateFormatUtils.PATTEN_DEFAULT_HMS);
        return sf.format(new Date());
    }
	
	    /**
     * lj 获取当天最晚时间字符串
     *
     * @return the string
     */
    public static String getNowUnixYMDAsStr1(){
        SimpleDateFormat sf = new SimpleDateFormat(DateFormatUtils.PATTEN_DEFAULT_YMD);
        String ymd = sf.format(new Date());
        String hms = "23:59:59";
        return ymd+" "+hms;
    }

    public static boolean compareDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        String dateStr1 = new SimpleDateFormat("yyyyMMdd").format(date1);
        String dateStr2 = new SimpleDateFormat("yyyyMMdd").format(date2);
        return dateStr1.equals(dateStr2);
    }
}
