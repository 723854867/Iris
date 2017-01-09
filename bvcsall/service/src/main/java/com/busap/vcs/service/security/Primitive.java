package com.busap.vcs.service.security;


import com.busap.vcs.util.DateFormatUtils;
import com.busap.vcs.util.DateUtils;

/**
 * Created by
 * User: djyin
 * Date: 12/24/13
 * Time: 4:07 PM
 * 自动将String转换成几种基本数据类型用的枚举
 */
public enum Primitive {

    Integer, Long, Short, Double, Float, Boolean, String, Date;

    /**
     * From string.构造一个枚举
     *
     * @param value the value 类名或者短名，比如 java.util.Date 或者 Date
     * @return the primitive
     */
    public static Primitive fromString(String value) {
        int index = value.lastIndexOf(".");
        if (index > 0) {
            return valueOf(value.substring(index+1));
        } else {
            return valueOf(value);
        }
    };

    /**
     * To primitive. 利用String参数的构造函数，
     *  转换成Primitive代表的真正类型的对象
     *
     * @param primitive the primitive
     * @param value the value
     * @return the object
     */
    @SuppressWarnings("static-access")
	public static Object toObject(Primitive primitive, String value) {
        Object obj = null;
        switch (primitive){
            case Boolean:
                obj = Boolean.valueOf(value);
                break;
            case Long:
                obj = Long.valueOf(value);
                break;
            case Integer:
                obj = Integer.valueOf(value);
                break;
            case String:
                obj = String.valueOf(value);
                break;
            case Date:
                obj = DateUtils.parseDate(value, DateFormatUtils.PATTEN_DEFAULT_FULL, DateFormatUtils.PATTEN_DEFAULT_YMD, DateFormatUtils.PATTEN_LONG_FULL, DateFormatUtils.PATTEN_LONG_YMD);
                break;
            case Double:
                obj = Double.valueOf(value);
                break;
            case Float:
                obj = Float.valueOf(value);
                break;
            case Short:
                obj = Short.valueOf(value);
                break;

            default:
                obj = value;
        }
        return obj;
    };

}
