package com.busap.vcs.util.page;

import java.lang.annotation.*;

/**
 * 分页注解
 * User: huoshanwei
 * Date: 15-10-23
 * Time: 16:41
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnablePaging {
    Class pageClass() default Page.class;
}
