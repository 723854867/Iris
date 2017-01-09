package com.spnetty.server.util;

import com.spnetty.util.SSLUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

import javax.net.ssl.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * Created by
 * User: djyin
 * Date: 2/13/14
 * Time: 5:05 PM
 * 一个用在服务器端的SSL帮助类,需要 org.bouncycastle.jce.provider.BouncyCastleProvider
 */
public class SSLServerUtils extends SSLUtils {


    /**
     * 加载 BouncyCastle JCE Provider
     */
    public static void loadBC() {
        try {
            if (Security.getProvider("BC") == null)
                Security.addProvider(new BouncyCastleProvider());
        } catch (Throwable e) {
            throw new RuntimeException("Miss JSSE provider BC, please check classpath for org.bouncycastle.jce.provider");
        }
    }

    /**
     * 加载SSL需要的KeyManager
     * @param type
     * @param in
     * @param password
     * @return
     * @throws Exception
     */
    public static KeyManager[] loadKeyManager(String type, InputStream in, String password) throws Exception {
        loadBC();

        final char[] pwd = (password == null || password.length() == 0) ? null : password.toCharArray();

        if (type.equalsIgnoreCase("pem")) {
            PEMReader pemReader = new PEMReader(new InputStreamReader(in), new PasswordFinder() {
                public char[] getPassword() {
                    return pwd;
                }
            });

            Object obj, keyObj = null, certObj = null, keyPair = null;

            while ((obj = pemReader.readObject()) != null) {
                if (obj instanceof X509Certificate) certObj = obj;
                else if (obj instanceof PrivateKey) keyObj = obj;
                else if (obj instanceof KeyPair) keyPair = obj;
            }

            if ((keyObj != null || keyPair != null) && certObj != null) {
                final PrivateKey key = keyPair != null ? ((KeyPair) keyPair).getPrivate() : (PrivateKey) keyObj;
                final X509Certificate cert = (X509Certificate) certObj;

                KeyStore ksKeys = KeyStore.getInstance("JKS");
                ksKeys.load(null, pwd == null ? "".toCharArray() : pwd);

                ksKeys.setCertificateEntry("", cert);
                ksKeys.setKeyEntry("", key, pwd == null ? "".toCharArray() : pwd, new Certificate[]{cert});
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(ksKeys, pwd == null ? "".toCharArray() : pwd);

                return kmf.getKeyManagers();


            } else {
                throw new RuntimeException("Could not load PEM source");
            }
        }

        KeyStore ksKeys = KeyStore.getInstance(type);
        ksKeys.load(in, pwd);

        Enumeration<String> aliases = ksKeys.aliases();

        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            System.err.println("KeyStore Alias: " + alias);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        kmf.init(ksKeys, pwd);

        return kmf.getKeyManagers();
    }

    public static X509Certificate loadCertificateFromPEM(InputStream in, final char[] pwd) throws Exception {
        loadBC();

        PEMReader pemReader = new PEMReader(new InputStreamReader(in), new PasswordFinder() {
            public char[] getPassword() {
                return pwd;
            }
        });

        Object obj;
        while ((obj = pemReader.readObject()) != null) {
            if (obj instanceof X509Certificate) {
                return (X509Certificate) obj;
            }
        }

        return null;
    }

    /**
     *
     * @param type pem, jks, pkcs12
     * @param in
     * @param password
     * @return
     * @throws Exception
     */
    public static TrustManager[] loadTrustManager(String type, InputStream in, String password) throws Exception {
        loadBC();

        String algorithm = getKeyManagerFactoryAlgorithm();

        char[] pwd = (password == null || password.length() == 0) ? null : password.toCharArray();

        if (type.equalsIgnoreCase("pem")) {

            final X509Certificate cert = loadCertificateFromPEM(in, pwd);

            KeyStore ksKeys = KeyStore.getInstance("JKS");
            ksKeys.load(null, pwd == null ? "".toCharArray() : pwd);

            ksKeys.setCertificateEntry("", cert);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ksKeys);

            return tmf.getTrustManagers();

        }

        KeyStore caKeys = KeyStore.getInstance(type);
        caKeys.load(in, pwd);

        Enumeration<String> aliases = caKeys.aliases();
        while (aliases.hasMoreElements()) {
            String alias = (String) aliases.nextElement();
            System.err.println("KeyStore Alias: " + alias);
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        tmf.init(caKeys);

        return tmf.getTrustManagers();
    }


}
