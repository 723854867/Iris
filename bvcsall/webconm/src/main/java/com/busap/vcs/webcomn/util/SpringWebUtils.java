package com.busap.vcs.webcomn.util;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/**
 * Spring环境下的帮助类
 */
@Component("springWebUtils")
@Lazy(false)
public final class SpringWebUtils implements DisposableBean,
        ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringWebUtils.applicationContext = applicationContext;
    }

    public void destroy() {
        applicationContext = null;
    }

    /**
     * 获取Spring的ApplicationContext
     *
     * @return the application context
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取一个Bean
     *
     * @param name the name
     * @return the bean
     */
    public static Object getBean(String name) {
        Assert.hasText(name);
        return applicationContext.getBean(name);
    }

    /**
     * 获取一个Bean
     *
     * @param name the name
     * @param type the type
     * @return the bean
     */
    public static <T> T getBean(String name, Class<T> type) {
        Assert.hasText(name);
        Assert.notNull(type);
        return applicationContext.getBean(name, type);
    }

    /**
     * 获取一个Spring管理的i18n消息
     *
     * @param code the code
     * @param msgArgs the args
     * @return the message
     */
    public static String getMessage(String code, Object... msgArgs) {
        LocaleResolver localeResolver = getBean("localeResolver",
                LocaleResolver.class);
        Locale locale = localeResolver.resolveLocale(null);
        try {
            return applicationContext.getMessage(code, msgArgs, locale);
        } catch (NoSuchMessageException e) {
            // ignored
            // 解决spring 3.2.5 还有的一个问题：
            // 就是ReloadableResourceBundleMessageSource设置了useCodeAsDefaultMessage=true，
            // 会导致jsr303的bean验证无法正确获取消息,而设置useCodeAsDefaultMessage=false,又会很麻烦
        }
        return code;
    }
}
