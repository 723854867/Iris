/* Copyright (c) 2013 RelayRides
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.spnetty.util;

import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * 客户端代码中使用SSL
 * 默认使用SUN的实现
 */
public class SSLUtils {

    public static final String DEFAULT_SSL_PROTOCOL = "TLS";
    public static final String DEFAULT_TRUSTMANAGER_ALGORITHM = "SunX509";

    public static String getKeyManagerFactoryAlgorithm() {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = DEFAULT_TRUSTMANAGER_ALGORITHM;
        }
        return algorithm;
    }


    public static SslHandler loadTrustAllSslHandler(final KeyStore keyStore, final char[] keyStorePassword, final boolean isClient)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {

        return loadSslHandler(keyStore, keyStorePassword, isClient, false, loadTrustAllManager());
    }

    public static SslHandler loadSslHandler(final KeyStore keyStore,
                                            final char[] keyStorePassword,
                                            final boolean isClient, final boolean wantClientAuth, TrustManager[] trustManagers)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        String algorithm = getKeyManagerFactoryAlgorithm();

        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(algorithm);
        keyManagerFactory.init(keyStore, keyStorePassword);
        final SSLContext sslContext = SSLContext.getInstance(DEFAULT_SSL_PROTOCOL);
        sslContext.init(keyManagerFactory.getKeyManagers(),
                trustManagers, null);
        final SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(isClient);
        sslEngine.setWantClientAuth(wantClientAuth);
        return new SslHandler(sslEngine);
    }

    public static SslHandler loadSslHandler(final KeyStore keyStore,
                                            final char[] keyStorePassword,
                                            final boolean isClient, final boolean wantClientAuth)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {

        return loadSslHandler(keyStore,keyStorePassword,isClient,wantClientAuth,loadTrustManager(keyStore));
    }

    /**
     * @return
     */
    public static TrustManager[] loadTrustAllManager() {

        return new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                // ignored
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                // ignored
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
    }

    /**
     * @return
     * @throws Exception
     */
    public static TrustManager[] loadTrustManager(KeyStore certsKeystore) throws KeyStoreException, NoSuchAlgorithmException {

        String algorithm = getKeyManagerFactoryAlgorithm();

        Enumeration<String> aliases = certsKeystore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            System.err.println("KeyStore Alias: " + alias);
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        tmf.init(certsKeystore);

        return tmf.getTrustManagers();
    }
    /**
     * @param type     jks, pkcs12
     * @param in
     * @param password
     * @return
     * @throws Exception
     */
    public static TrustManager[] loadTrustManager(String type, InputStream in, String password) throws Exception {

        String algorithm = getKeyManagerFactoryAlgorithm();

        char[] pwd = (password == null || password.length() == 0) ? null : password.toCharArray();

        KeyStore caKeys = KeyStore.getInstance(type);
        caKeys.load(in, pwd);

        Enumeration<String> aliases = caKeys.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            System.err.println("KeyStore Alias: " + alias);
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
        tmf.init(caKeys);

        return tmf.getTrustManagers();
    }


    /**
     * Load keystore.
     *
     * @param keyStoreInputStream the key store input stream
     * @param keystorePwd         the keystore pwd
     * @param keystoreType        the keystore type
     * @return the key store
     * @throws java.security.cert.CertificateException     the certificate exception
     * @throws java.security.NoSuchAlgorithmException the no such algorithm exception
     * @throws java.io.IOException      the iO exception
     * @throws java.security.KeyStoreException        the key store exception
     */
    public static KeyStore loadKeyStore(String keystoreType, InputStream keyStoreInputStream, char[] keystorePwd)
            throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, NoSuchProviderException {
        if (keyStoreInputStream == null) {
            throw new IOException("keystore's inputstream is null.");
        }
        if (keystoreType == null) {
            keystoreType = "jks";
        }
        final KeyStore keyStore = KeyStore.getInstance(keystoreType);
        keyStore.load(keyStoreInputStream, keystorePwd);
        return keyStore;

    }


}
