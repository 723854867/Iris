package org.Iris.app.pay.util;

import java.security.MessageDigest;

/**
 * 
 * @author 樊水东
 * 2016年5月19日
 */
public class MD5 {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5编码
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
    
    public static void main(String[] args) {
    	String str = "appId=wx9284bb4b7b29a9e2&nonceStr=3mcth29dq0n9ac4452zjfob38qgegtyl&prepay_id=prepay_id=wx201605251812094896f1b7fd0614062910&signType=MD5&timeStamp=1464171116&key=pz1p2lbsprxvjp9ejc5hk21oxn43obzz";
		System.out.println(MD5Encode(str).toUpperCase());
	}
    
}
