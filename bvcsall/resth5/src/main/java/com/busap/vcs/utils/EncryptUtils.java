package com.busap.vcs.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 加密工具类
 *
 * @author gaowf
 */

@Service
public class EncryptUtils {


    @Value("${encryptField}")
    private Boolean encryptField;

    public final static String MD5(String s) {
        try {
            return DigestUtils.md5Hex(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean getEncryptField() {
        return encryptField == null ? false : encryptField;
    }


    public static void main(String[] args) {
        System.out.println(MD5("123456"));
    }

}
