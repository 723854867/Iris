package com.spnetty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.KeyStore;

/**
 * 默认的服务器设置，使用函数的方式便于覆盖
 * 默认的参数设置为优化小数据量，高并发的应用
 */
@Component
public abstract class SSLServer extends TcpSocketServer {

    private static final Logger log = LoggerFactory.getLogger(SSLServer.class);
    private KeyStore ks;
    private char[] ksPwd;
    private String ksType;

    public String getKsURI() {
        return ksURI;
    }

    public void setKsURI(String ksURI) {
        this.ksURI = ksURI;
    }

    private String ksURI;

    public KeyStore getKs() {
        return ks;
    }

    public void setKs(KeyStore ks) {
        this.ks = ks;
    }

    public char[] getKsPwd() {
        return ksPwd;
    }

    public void setKsPwd(char[] ksPwd) {
        this.ksPwd = ksPwd;
    }

    public String getKsType() {
        return ksType;
    }

    public void setKsType(String ksType) {
        this.ksType = ksType;
    }
}
