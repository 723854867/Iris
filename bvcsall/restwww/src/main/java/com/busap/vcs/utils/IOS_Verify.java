package com.busap.vcs.utils;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

import java.security.MessageDigest;

/**
 * ios 验证
 * Created by Knight on 16/4/6.
 */
public class IOS_Verify {

    public static String getUrl(String environment) {

        if ("Real".equals(environment)) {
            return "https://buy.itunes.apple.com/verifyReceipt";
        }
        if ("Sandbox".equals(environment) || environment == null) {
            return "https://sandbox.itunes.apple.com/verifyReceipt";
        }
        return "";
    }

    public static String getEnvironment(String receipt)
    {
        try{
            JSONObject job = JSONObject.fromObject(receipt);
            if(job.containsKey("environment")){
                return job.getString("environment");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "Real";
    }

    /**
     * 用BASE64加密
     * @param str 加密字符串
     * @return result
     */
    public static String getBASE64(String str) {
        byte[] b = str.getBytes();
        String s = null;
        if (b != null) {
            s = new sun.misc.BASE64Encoder().encode(b);
        }
        return s;
    }

    /**
     * 解密BASE64字窜
     * @param s 解密字符串
     * @return result
     */
    public static String getFromBASE64(String s) {
        byte[] b = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                return new String(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new String(b);
    }

    /**
     * md5加密方法
     * Jul 30, 2010 4:38:28 PM
     * @param plainText 加密字符串
     * @return String 返回32位md5加密字符串(16位加密取substring(8,24))
     * each engineer has a duty to keep the code elegant
     */
    public static String md5(String plainText) {
        // 返回字符串
        String md5Str = null;
        try {
            // 操作字符串
            StringBuilder buf = new StringBuilder();
            /**
             * MessageDigest 类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
             * 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
             *
             * MessageDigest 对象开始被初始化。
             * 该对象通过使用 update()方法处理数据。
             * 任何时候都可以调用 reset()方法重置摘要。
             * 一旦所有需要更新的数据都已经被更新了，应该调用digest()方法之一完成哈希计算。
             *
             * 对于给定数量的更新数据，digest 方法只能被调用一次。
             * 在调用 digest 之后，MessageDigest 对象被重新设置成其初始状态。
             */
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 添加要进行计算摘要的信息,使用 plainText 的 byte 数组更新摘要。
            md.update(plainText.getBytes());
            // 计算出摘要,完成哈希计算。
            byte b[] = md.digest();
            int i;
            for (byte aB : b) {
                i = aB;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                // 将整型 十进制 i 转换为16位，用十六进制参数表示的无符号整数值的字符串表示形式。
                buf.append(Integer.toHexString(i));
            }
            // 32位的加密
            md5Str = buf.toString();
            // 16位的加密
            // md5Str = buf.toString().md5Strstring(8,24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Str;
    }
}
