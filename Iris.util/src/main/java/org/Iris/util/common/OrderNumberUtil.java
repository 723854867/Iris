package org.Iris.util.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 生成订单号：当前年月日时分秒毫秒+N位随机数
 * 
 * @author liusiyuan 2017年3月3日
 */
public class OrderNumberUtil {
	
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	public static String getRandomOrderId(int n) {
		String str = simpleDateFormat.format(new Date());
		String rannum = getRandNum(n);
		return str + rannum;
	}
	
	public static String getRandNum(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }
	public static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }
}
