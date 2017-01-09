package com.busap.vcs.service;

import java.awt.image.BufferedImage;

public abstract interface CaptchaService {

    /**
     * Build new captcha id.
     *
     * @return the string
     */
    public abstract String buildNewCaptchaId();

    /**
     * Build image.
     *
     * @param paramString
     *         the param string
     * @return the buffered image
     */
    public abstract BufferedImage buildImage(String paramString);

    /**
     * Is valid.
     *
     * @param captchaId
     *         the captcha id
     * @param captcha
     *         the captcha
     * @return the boolean
     */
    public abstract boolean isValid(String captchaId, String captcha);

    /**
     * Is disabled.
     *
     * @return the boolean
     */
    public boolean isDisabled();
}
