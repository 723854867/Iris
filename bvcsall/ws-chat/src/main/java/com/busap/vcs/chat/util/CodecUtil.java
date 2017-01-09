package com.busap.vcs.chat.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;


// TODO: Auto-generated Javadoc
/**
 * The Class CodecUtil.
 */
public class CodecUtil {

	/** The logger. */
	private static Logger logger = Logger.getLogger(CodecUtil.class);
	
	/**
	 * Encrypt.
	 *
	 * @param source the source
	 * @param key the key
	 * @param algorithm the algorithm
	 * @param needURLEncode true:URLEncoder.encode
	 * @return the string
	 */
	public static String encrypt(String source, String key, String algorithm,boolean needURLEncode) {
        
		try {
			SecretKey secKey = new SecretKeySpec(key.getBytes(), algorithm);
			Cipher cp = Cipher.getInstance(algorithm);
			cp.init(Cipher.ENCRYPT_MODE, secKey);
			byte[] ctext = cp.doFinal(source.getBytes());
			//for (int i = 0; i < ctext.length; i++) {
			//	System.out.print(ctext[i]);
			//}
			byte[] t = Base64.encodeBase64(ctext);
			String a = new String(t);
			logger.debug("###########@"+a);
			if(needURLEncode){
				return URLEncoder.encode(a, "UTF-8");
			}else{
				return a;
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Decrypt.
	 *
	 * @param cipher the cipher
	 * @param key the key
	 * @param algorithm the algorithm
	 * @param iphoneKey the iphone key
	 * @return the string
	 */
	public static String decrypt(String cipher, String key, String algorithm, String iphoneKey) {

		SecretKey secKey = new SecretKeySpec(key.getBytes(), algorithm);
		try {
			//cipher = URLDecoder.decode(cipher, "UTF-8");
			//logger.debug(cipher);
			byte[] b = Base64.decodeBase64(cipher);
			Cipher cp = Cipher.getInstance(algorithm);
			cp.init(Cipher.DECRYPT_MODE, secKey);
			byte[] ctext = cp.doFinal(b);
			String str = new String(ctext);
			if(!str.contains("cv")){
				secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				ctext = cp.doFinal(b);
				str = new String(ctext);
			}
			return str;
		} catch (NoSuchAlgorithmException e) {
			secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
			try {
				byte[] b = Base64.decodeBase64(cipher);
				Cipher cp = Cipher.getInstance(algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				byte[] ctext = cp.doFinal(b);
				return new String(ctext);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
			try {
				byte[] b = Base64.decodeBase64(cipher);
				Cipher cp = Cipher.getInstance(algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				byte[] ctext = cp.doFinal(b);
				return new String(ctext);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		} catch (InvalidKeyException e) {
			secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
			try {
				byte[] b = Base64.decodeBase64(cipher);
				Cipher cp = Cipher.getInstance(algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				byte[] ctext = cp.doFinal(b);
				return new String(ctext);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
			try {
				byte[] b = Base64.decodeBase64(cipher);
				Cipher cp = Cipher.getInstance(algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				byte[] ctext = cp.doFinal(b);
				return new String(ctext);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		} catch (BadPaddingException e) {
			secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
			try {
				byte[] b = Base64.decodeBase64(cipher);
				Cipher cp = Cipher.getInstance(algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				byte[] ctext = cp.doFinal(b);
				return new String(ctext);
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				e1.printStackTrace();
			} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		} 
//		catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		return null;

	}
	
	
	/**
	 * Decrypt user.
	 *
	 * @param cipher the cipher
	 * @param key the key
	 * @param algorithm the algorithm
	 * @param iphoneKey the iphone key
	 * @return the string
	 */
	public static String decryptUser(String cipher, String key, String algorithm, String iphoneKey) {

		SecretKey secKey = new SecretKeySpec(key.getBytes(), algorithm);
		try {
			//cipher = URLDecoder.decode(cipher, "UTF-8");
			//logger.debug(cipher);
			byte[] b = Base64.decodeBase64(cipher);
			Cipher cp = Cipher.getInstance(algorithm);
			cp.init(Cipher.DECRYPT_MODE, secKey);
			byte[] ctext = cp.doFinal(b);
			String str = new String(ctext);
			if(!str.contains("userid")){
				secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				ctext = cp.doFinal(b);
				str = new String(ctext);
			}
			return str;
		} catch (NoSuchAlgorithmException e) {
			secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
			try {
				byte[] b = Base64.decodeBase64(cipher);
				Cipher cp = Cipher.getInstance(algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				byte[] ctext = cp.doFinal(b);
				return new String(ctext);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
			try {
				byte[] b = Base64.decodeBase64(cipher);
				Cipher cp = Cipher.getInstance(algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				byte[] ctext = cp.doFinal(b);
				return new String(ctext);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		} catch (InvalidKeyException e) {
			secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
			try {
				byte[] b = Base64.decodeBase64(cipher);
				Cipher cp = Cipher.getInstance(algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				byte[] ctext = cp.doFinal(b);
				return new String(ctext);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
			try {
				byte[] b = Base64.decodeBase64(cipher);
				Cipher cp = Cipher.getInstance(algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				byte[] ctext = cp.doFinal(b);
				return new String(ctext);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		} catch (BadPaddingException e) {
			secKey = new SecretKeySpec(iphoneKey.getBytes(), algorithm);
			try {
				byte[] b = Base64.decodeBase64(cipher);
				Cipher cp = Cipher.getInstance(algorithm);
				cp.init(Cipher.DECRYPT_MODE, secKey);
				byte[] ctext = cp.doFinal(b);
				return new String(ctext);
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				e1.printStackTrace();
			} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		} 
//		catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		return null;

	}
	
	/**
	 * Decrypt.
	 *
	 * @param cipher the cipher
	 * @param key the key
	 * @param algorithm the algorithm
	 * @return the string
	 */
	public static String decrypt(String cipher, String key, String algorithm) {

		SecretKey secKey = new SecretKeySpec(key.getBytes(), algorithm);
		try {
			//cipher = URLDecoder.decode(cipher, "UTF-8");
			//logger.debug(cipher);
			byte[] b = Base64.decodeBase64(cipher);
			Cipher cp = Cipher.getInstance(algorithm);
			cp.init(Cipher.DECRYPT_MODE, secKey);
			byte[] ctext = cp.doFinal(b);
			return new String(ctext);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} 
//		catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		return null;

	}

	/**
	 * The main method.
	 *
	 * @param agrs the arguments
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static void main(String[] agrs) throws NoSuchAlgorithmException {
		
	}
}
