package com.busap.vcs.base;

import org.apache.commons.beanutils.converters.AbstractConverter;

public class EnumConverter extends AbstractConverter {
    private final Class<?> defaultType;

    public EnumConverter(Class<?> enumClass) {
        this(enumClass, null);
    }

    public EnumConverter(Class<?> enumClass, Object defaultValue) {
        super(defaultValue);
        this.defaultType = enumClass;
    }

    protected Class<?> getDefaultType() {
        return defaultType;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object convertToType(Class type, Object value) {
        String valueStr = value.toString().trim();
        return Enum.valueOf(type, valueStr);
    }

    protected String convertToString(Object value) {
        return value.toString();
    }
}
