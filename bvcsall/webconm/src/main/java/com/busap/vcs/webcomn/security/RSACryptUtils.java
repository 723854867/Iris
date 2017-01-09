package com.busap.vcs.webcomn.security;

import com.busap.vcs.util.RSAUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSACryptUtils {
    private static final String PRIVATEKEY = "privateRSACryptKey";

    public static RSAPublicKey generateKey(HttpServletRequest request) {
        Assert.notNull(request);
        KeyPair keyPair = RSAUtils.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        HttpSession session = request.getSession();
        session.setAttribute(PRIVATEKEY, rsaPrivateKey);
        return rsaPublicKey;
    }

    public static void removePrivateKey(HttpServletRequest request) {
        Assert.notNull(request);
        HttpSession session = request.getSession();
        session.removeAttribute(PRIVATEKEY);
    }

    public static String decryptParameter(String name, HttpServletRequest request) {
        Assert.notNull(request);
        if (name != null) {
            HttpSession session = request.getSession();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) session
                    .getAttribute(PRIVATEKEY);
            String cryptName = request.getParameter(name);
            if ((rsaPrivateKey != null) && (StringUtils.isNotEmpty(cryptName))) {
                return RSAUtils.decrypt(rsaPrivateKey, cryptName);
            }
        }
        return null;
    }
}
