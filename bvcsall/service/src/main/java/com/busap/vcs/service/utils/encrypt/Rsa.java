package com.busap.vcs.service.utils.encrypt;

import javax.crypto.Cipher;

import org.bouncycastle.util.encoders.Base64;


import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Rsa {
    public static class Keys {
        private String privateKey;
        private String publicKey;

        public Keys(String privateKey, String publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }
    }

    public static class Generator {
        public static Keys generate() {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            try {
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
                generator.initialize(2048, new SecureRandom());
                KeyPair pair = generator.generateKeyPair();
                PublicKey publicKey = pair.getPublic();
                PrivateKey privateKey = pair.getPrivate();
                return new Keys(new String(Base64.encode(privateKey.getEncoded())), new String(Base64.encode(publicKey.getEncoded())));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class Encoder {
        private PrivateKey mPrivateKey;
        private Cipher cipher;

        public Encoder(String privateKey) {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            PKCS8EncodedKeySpec privatePKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey.getBytes()));
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
                mPrivateKey = keyFactory.generatePrivate(privatePKCS8);
                cipher = Cipher.getInstance("RSA", "BC");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String encode(String source) {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, mPrivateKey);
                byte[] cipherText = cipher.doFinal(source.getBytes("utf-8"));
                return new String(Base64.encode(cipherText));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class Decoder {
        private PublicKey mPublicKey;
        private Cipher cipher;

        public Decoder(String publicKey) {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            X509EncodedKeySpec publicX509 = new X509EncodedKeySpec(Base64.decode(publicKey.getBytes()));
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
                mPublicKey = keyFactory.generatePublic(publicX509);
                cipher = Cipher.getInstance("RSA", "BC");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String decode(String source) {
            try {
                cipher.init(Cipher.DECRYPT_MODE, mPublicKey);
                byte[] output = cipher.doFinal(Base64.decode(source.getBytes()));
                return new String(output, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public static void main(String[] args) {
		String pub="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAukzuu4OXRk3X9ICKnJIvQnUsVCCrkHxjxTXPk1QyPNStosSl5RfB69Itz9SPwna76sqVo/vpVOV5veSQWAAM1cumdWxqld+ONey6J2yMKWLk1aXYTmgNmiz+qLkkgrneR14VVPLNgDmCAX6sjC46cvdcIA1EmkN5yTRZoVaHH1VV8hCdn9Ad3UT+V7HVg4xJkmtPNn7wh95h4vPcmkUVxJApFjBMlTuPSTnYARuTemAqruikFjUhW/vy7YLu9aU0x1blF42Cx9qR1OiU/iwqhcXgOojNYD00MzCOb7fHAaZH0dwvjApqC8yjoEbGZOzK89+ebcyDkVIrQavMAS2BFwIDAQAB";
		String ssss=new Decoder(pub).decode("NavWdnmPKzucwP3WxB4Eib71O23e9w5SmuL5mBeqNAc0hvFJaLQElzAOd6Skdz9q2wjSCQQiTobEvORKyZjcyR8u9eKyJMcHlWPM1YTzzjZTdKwXHykQCHsef7UIbDMsqI/MWvJIFEglX1OahILRT+LcyvfOwsvCYh2+wnOM+V2KQcJ+CcPDhdwuPYZmby9WsRvylPAwp2ewGzMtJxfWlWO6r2xeJkQRjyRh6faqriUZ0/g8ZvRC943W7Eq1OBP4n4aKGg16PTKjYVw/8YXZsxPmLxiOi/dkvALKKlBunhe2pNmlndZ6sSVW8AbHC/BMNv2FyASPsPmEGknbkTX0Zw==");
        System.out.println(ssss);
    }
}
