package com.busap.vcs.restadmin.utils;

import java.lang.annotation.*;

/**
 * 功能声明
 * User: huoshanwei
 * Date: 15-11-6
 * Time: 上午15:40
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableFunction {

    public String value() default "";

}
