package org.Iris.util.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.Iris.util.common.uuid.AlternativeJdkIdGenerator;
import org.Iris.util.lang.StringUtil;

public class IrisSecurity {

	public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	public static String encodeToken(String context) {
		return StringUtil.hasText(context) ? 
				toMd5(context + System.currentTimeMillis()) : 
					toMd5(AlternativeJdkIdGenerator.INSTANCE.generateId().toString() + System.currentTimeMillis());
	}
	
	public static String toMd5(String password) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			byte[] byteDigest = md5.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < byteDigest.length; offset++) {
				i = byteDigest[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(IrisSecurity.toMd5("123"));
	}
}
