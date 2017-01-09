package com.busap.vcs.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Assert;

import javax.crypto.Cipher;
import java.security.*;

public final class RSAUtils {
    private static final Provider provider = new BouncyCastleProvider();

    private static final int KEYSIZE = 1024;

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance("RSA", provider);
            keyPairGenerator.initialize(KEYSIZE, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encrypt(PublicKey publicKey, byte[] data) {
        Assert.notNull(publicKey);
        Assert.notNull(data);
        try {
            Cipher cipher = Cipher.getInstance("RSA", provider);
            cipher.init(1, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(PublicKey publicKey, String text) {
        Assert.notNull(publicKey);
        Assert.notNull(text);
        byte[] encryptByteArray = encrypt(publicKey, text.getBytes());
        return encryptByteArray != null ? Base64.encodeBase64String(encryptByteArray)
                : null;
    }

    public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
        Assert.notNull(privateKey);
        Assert.notNull(data);
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",
                    provider);
            cipher.init(2, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
        }
        return null;
    }

    public static String decrypt(PrivateKey privateKey, String text) {
        Assert.notNull(privateKey);
        Assert.notNull(text);
        byte[] decryptByteArray = decrypt(privateKey, Base64.decodeBase64(text));
        return decryptByteArray != null ? new String(decryptByteArray) : null;
    }
}