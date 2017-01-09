package com.busap.vcs.util;

/**
 * Created by
 * User: djyin
 * Date: 12/13/13
 * Time: 2:32 PM
 */
public class DateFormatUtils extends org.apache.commons.lang.time.DateFormatUtils{

    /**
     * 默认的年月日时分秒的格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String PATTEN_DEFAULT_FULL = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认的年月日的格式. yyyy-MM-dd
     */
    public static final String PATTEN_DEFAULT_YMD = "yyyy-MM-dd";

    /**
     * 默认的时分秒的格式.HH:mm:ss
     */
    public static final String PATTEN_DEFAULT_HMS = "HH:mm:ss";

    /**
     * 默认整形数据中的年月日时分秒的格式. yyyyMMddHHmmss
     */
    public static final String PATTEN_LONG_FULL = "yyyyMMddHHmmss";

    /**
     * 默认整形数据中的年月日的格式.yyyymmdd
     */
    public static final String PATTEN_LONG_YMD = "yyyymmdd";
    /**
     * 默认整形数据中的时分秒的格式. HHmmss
     */
    public static final String PATTEN_LONG_HMS = "HHmmss";
    
    /**
     * 默认整形数据中的时分秒的格式. dd/MM/yyyy
     */
    public static final String PATTEN_UNIX_YMD = "dd/MM/yyyy";
    
    
}
