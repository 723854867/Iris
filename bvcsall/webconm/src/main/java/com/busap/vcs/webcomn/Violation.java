package com.busap.vcs.webcomn;


import com.busap.vcs.webcomn.util.SpringWebUtils;

import javax.validation.ConstraintViolation;
import java.util.Iterator;

/**
 * Created by
 * User: djyin
 * Date: 12/13/13
 * Time: 7:01 PM
 * 基于JSR-303的验证,其实想弄JSR-349的,可惜Spring 3.x 系列不支持.
 */
public class Violation {
    /**
     * The Property.
     */
    String property;
    /**
     * The Message.
     */
    String message;
    /**
     * The Invalid value.
     */
    Object invalidValue;

    public Violation() {
        super();
    }

    /**
     * Instantiates a new Violation.
     *
     * @param constraint the constraint
     */
    public Violation(ConstraintViolation<?> constraint) {
        Iterator<?> i = constraint.getPropertyPath().iterator();
        while (i.hasNext()) {
            property = i.next().toString();
        }
        message = constraint.getMessage();
        invalidValue = constraint.getInvalidValue();
    }

    /**
     * Instantiates a new Violation.
     *
     * @param property the property 属性名
     * @param message  the message 已经I18N后的消息
     * @param value    the value  错误的值
     */
    public Violation(String property, String message, Object value) {
        this.property = property;
        this.message = message;
        this.invalidValue = value;
    }


    /**
     * 数据验证失败的属性名
     *
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * 失败消息
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 设置未i18n后的消息
     *
     * @param messageTemplate the message template
     * @param args            the args
     */
    public void setI18NMessage(String messageTemplate, String... args) {
        this.message = SpringWebUtils.getMessage(messageTemplate, (Object)args);
    }

    /**
     * 错误的值
     *
     * @return the invalid value value
     */
    public Object getInvalidValueValue() {
        return invalidValue;
    }

    public void setInvalidValueValue(Object value) {
        this.invalidValue = value;
    }
}
