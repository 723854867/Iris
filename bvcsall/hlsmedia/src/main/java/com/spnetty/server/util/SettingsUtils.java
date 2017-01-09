package com.spnetty.server.util;

import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.HashMap;
import java.util.Map;

/**
 * 帮助累,用来从spring中获取配置好的对象,或者配置值
 */
public class SettingsUtils {

    static Map<String, Object> settings = new HashMap<String, Object>();
    static Map<String, Object> typeSettings = new HashMap<String, Object>();
    static protected ConfigurableConversionService conversionService = new DefaultConversionService();

    /**
     * 获取一个配置好的对象
     *
     * @param clz
     * @param name
     * @param <T>
     * @return
     */
    public static <T> T getObject(String name, Class<T> clz) {
        return SimpleSpringCtxLaunchUtils.ctx.getBean(name, clz);
    }

    /**
     * 获取一个配置信息
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        Object obj = settings.get(key);
        if (obj != null) {
            return obj.toString();
        } else {
            String value = SimpleSpringCtxLaunchUtils.ctx.getBeanFactory().resolveEmbeddedValue("${" + key + "}");
            if (value != null) {
                settings.put(key, value);
                return value;
            }
        }

        return null;
    }

    public static <T> T getProperty(String key, Class<T> targetType) {
        Object obj = typeSettings.get(key);
        if (obj != null) {
            return (T) obj;
        } else {
            String value = SimpleSpringCtxLaunchUtils.ctx.getBeanFactory().resolveEmbeddedValue("${" + key + "}");
            if (value != null && conversionService.canConvert(String.class, targetType)) {
                T t = conversionService.convert(value, targetType);
                typeSettings.put(key, t);
                return t;
            }
        }

        return null;
    }
}
