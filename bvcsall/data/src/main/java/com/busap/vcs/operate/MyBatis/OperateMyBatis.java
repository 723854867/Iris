package com.busap.vcs.operate.MyBatis;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface OperateMyBatis {
    String value() default "";
}
