package com.busap.vcs.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * aes128 cbc
 * 
 * @author surf
 *
 */

public class AES128Common {
	private static final Logger logger = LoggerFactory.getLogger(AES128Common.class);

	private static String  key,iv;

	static {
		getIvKey();
	}

	private static void getIvKey(){
		Properties prop = new Properties();
		InputStream in = null;
		try {
			String configFilePath = "encrypt.properties"; 
			Resource resource = new ClassPathResource(configFilePath);
			in = resource.getInputStream();
			//in = new BufferedInputStream(new FileInputStream("d:/encrypt.properties"));

			prop.load(in);

			key = (String)prop.getProperty("key");

			iv = (String)prop.getProperty("iv");

		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString(),e);
		}
		finally {
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static byte[] hexDecode(String input) {
		try {
			return Hex.decode(input);
		} catch (Exception e) {
			logger.error(e.toString(),e);
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	public static String encrypt(String content){
		if(key == null || iv == null || content == null)
			return null;
		try{
			BufferedBlockCipher engine = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));  

			engine.init(true, new ParametersWithIV(new KeyParameter(hexDecode(key)),hexDecode(iv)));
			byte[] enc = new byte[engine.getOutputSize(content.getBytes().length)];  
			int size1 = engine.processBytes(content.getBytes(), 0, content.getBytes().length, enc, 0);  
			int size2 = engine.doFinal(enc, size1);  
			byte[] encryptedContent =new byte[size1+size2];  
			System.arraycopy(enc, 0, encryptedContent, 0, encryptedContent.length);  

			return new String(Base64.encode(encryptedContent));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String content)
	{
		if(key == null || iv == null || content == null)
			return null;
		try {

			BufferedBlockCipher engine = new PaddedBufferedBlockCipher(
					new CBCBlockCipher(new AESFastEngine()));
			//byte[] encryptedContent = hexDecode(content);
			byte[] encryptedContent = Base64.decode(content);
			engine.init(false, new ParametersWithIV(new KeyParameter(
					hexDecode(key)), hexDecode(iv)));
			byte[] dec = new byte[engine.getOutputSize(encryptedContent.length)];
			int size1 = engine.processBytes(encryptedContent, 0,
					encryptedContent.length, dec, 0);
			int size2 = engine.doFinal(dec, size1);
			byte[] decryptedContent = new byte[size1 + size2];
			System.arraycopy(dec, 0, decryptedContent, 0,
					decryptedContent.length);
			return new String(decryptedContent);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {

		String temp = AES128Common.encrypt("13683028357");
		System.out.println(temp);
		System.out.println(AES128Common.decrypt(temp));

	}
}
