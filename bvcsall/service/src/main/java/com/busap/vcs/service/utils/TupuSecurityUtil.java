package com.busap.vcs.service.utils;

import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 图谱鉴黄安全验证工具类
 * Created by huoshanwei on 2016/4/22.
 */
public class TupuSecurityUtil {


    public static String RIVATE_KEY = "/home/maishi/pem/pkcs8_private_key.pem";
    public static String TUPU_PUBLIC_KEY = "/home/maishi/pem/open_tuputech_com_public_key.pem";


    /**
     * 进行签名
     *
     * @param timestamp
     * @param nonce
     * @param secretId
     **/
    public static String createSignature(String timestamp, String nonce, String secretId) {
        try {
            String signString = secretId + "," + timestamp + "," + nonce;
            //System.out.println(signString);
            // 读取你的密钥pkcs8_private_key.pem
            //System.out.println(RIVATE_KEY);
            File private_key_pem = new File(RIVATE_KEY);
            InputStream inPrivate = new FileInputStream(private_key_pem);
            String privateKeyStr = readKey(inPrivate);
            byte[] buffer = TupuBase64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // 获取密钥对象
            PrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            // 用私钥对信息生成数字签名
            Signature signer = Signature.getInstance("SHA256WithRSA");
            signer.initSign(privateKey);
            signer.update(signString.getBytes());
            byte[] signed = signer.sign();
            return new String(TupuBase64.encode(signed));
        } catch (Exception e) {
            return "err";
        }
    }

    /**
     * 进行验证
     *
     * @param signature 数字签名
     * @param json      真正的有效数据的字符串
     **/
    public static boolean verify(String signature, String json) {
        try {
            // 读取图普公钥open_tuputech_com_public_key.pem
            File open_tuputech_com_public_key_pem = new File(TUPU_PUBLIC_KEY);
            InputStream inPublic = new FileInputStream(
                    open_tuputech_com_public_key_pem);
            String publicKeyStr = readKey(inPublic);
            byte[] buffer = TupuBase64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            // 获取公钥匙对象
            PublicKey pubKey = (RSAPublicKey) keyFactory
                    .generatePublic(keySpec);

            Signature signer = Signature.getInstance("SHA256WithRSA");
            signer.initVerify(pubKey);
            signer.update(json.getBytes());
            // 验证签名是否正常
            return signer.verify(TupuBase64.decode(signature));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 读取密钥信息
     *
     * @param in
     * @throws IOException
     */
    private static String readKey(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
                sb.append('\r');
            }
        }
        return sb.toString();
    }


}
