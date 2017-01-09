package com.busap.vcs.webcomn.controller;

import com.busap.vcs.base.Constants;
import com.busap.vcs.webcomn.RespBodyBuilder;
import com.busap.vcs.webcomn.Violation;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;

import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class BaseController {


    @Resource(name = "validator")
    private Validator validator;
    @Resource(name = "localeResolver")
    private FixedLocaleResolver localeResolver;
    @Resource(name = "messageSource")
    private MessageSource messageSource;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MutliFormatDateEditor(true));
    }


    /**
     * The Resp body writer.
     * 输出RespBody对象
     */
    @Resource(name = "respBodyBuilder")
    protected RespBodyBuilder respBodyWriter = new RespBodyBuilder();


    /**
     * 通过注解,验证实体的全部属性
     *
     * @param entity
     *         被验证的实体对象
     * @param groups
     *         验证分组,输入NULL则使用默认的分组
     *         (default to {@link javax.validation.groups.Default})
     * @return 验证结果的集合
     * @throws IllegalArgumentException
     *         if object is null
     *         or if null is passed to the varargs groups
     * @throws javax.validation.ValidationException
     *         if a non recoverable error happens
     *         during the validation process
     */
    @SuppressWarnings("unchecked")
	protected boolean validator(Object entity, Class<?>... groups) {
        if (groups == null || groups.length < 1) {
            groups = new Class[]{Default.class};
        }
        Set<ConstraintViolation<Object>> set = validator.validate(entity, groups);
        if (set.isEmpty()) {
            return true;
        }
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Set<ConstraintViolation<Object>> oldSet = 
        		(Set<ConstraintViolation<Object>>)requestAttributes.getAttribute(Constants.ENTITY_CONSTRAINT_VIOLATIONS, RequestAttributes.SCOPE_REQUEST);
        if (oldSet != null) {
            set.addAll(oldSet);
        }
        requestAttributes.setAttribute(Constants.ENTITY_CONSTRAINT_VIOLATIONS, set, RequestAttributes.SCOPE_REQUEST);
        return false;
    }

    /**
     * 通过注解,验证实体的某一个属性
     * <p/>
     * Validates all constraints placed on the property named <code>propertyName</code>
     * of the class <code>beanType</code> would the property value be <code>value</code>
     * <p/>
     * <code>ConstraintViolation</code> objects return null for
     * {@link javax.validation.ConstraintViolation#getRootBean()} and {@link javax.validation.ConstraintViolation#getLeafBean()}
     *
     * @param beanType
     *         验证的Model对象的类
     * @param propertyName
     *         被验证的属性名
     * @param value
     *         属性对应的值
     * @param groups
     *          验证分组,输入NULL则使用默认的分组
     *         (default to {@link javax.validation.groups.Default})
     * @return 验证结果的集合
     * @throws IllegalArgumentException
     *         if <code>beanType</code> is null,
     *         if <code>propertyName</code> null, empty or not a valid object property
     *         or if null is passed to the varargs groups
     * @throws javax.validation.ValidationException
     *         if a non recoverable error happens
     *         during the validation process
     */
    @SuppressWarnings("unchecked")
	protected boolean validator(Class<?> beanType, String propertyName, Object value, Class<?>... groups) throws ValidationException {
        if (groups == null || groups.length < 1) {
            groups = new Class[]{Default.class};
        }
        Set<?> set = validator.validateValue(beanType, propertyName, value, groups);
        if (set.isEmpty()) {
            return true;
        }
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Set<ConstraintViolation<Object>> oldSet = (Set<ConstraintViolation<Object>>) requestAttributes.getAttribute(Constants.ENTITY_CONSTRAINT_VIOLATIONS, RequestAttributes.SCOPE_REQUEST);
        if (oldSet != null) {
            oldSet.addAll((Set<ConstraintViolation<Object>>)set);
            requestAttributes.setAttribute(Constants.ENTITY_CONSTRAINT_VIOLATIONS, oldSet, RequestAttributes.SCOPE_REQUEST);
        }else{
            requestAttributes.setAttribute(Constants.ENTITY_CONSTRAINT_VIOLATIONS, set, RequestAttributes.SCOPE_REQUEST);

        }
        return false;
    }

    /**
     * 增加一个自定义的数据验证错误
     *
     * @param v the v
     * @throws javax.validation.ValidationException the validation exception
     */
    protected void addViolation(Violation v) throws ValidationException {

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Set<Violation> set = (Set<Violation>) requestAttributes.getAttribute(Constants.ENTITY_CUSTOM_VIOLATIONS, RequestAttributes.SCOPE_REQUEST);
        if (set == null) {
            set = new HashSet<Violation>();
        }
        set.add(v);
        requestAttributes.setAttribute(Constants.ENTITY_CUSTOM_VIOLATIONS, set, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 检查当前是否已经存在数据验证错误
     *
     * @return 存在错误,则返回true
     */
    @SuppressWarnings("unchecked")
	public boolean checkViolation() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Set<ConstraintViolation<?>> c = 
        		(Set<ConstraintViolation<?>>) requestAttributes.getAttribute(Constants.ENTITY_CONSTRAINT_VIOLATIONS, RequestAttributes.SCOPE_REQUEST);
        if (c != null && !c.isEmpty()) {
            return true;
        }
        Set<Violation> set = 
        		(Set<Violation>) requestAttributes.getAttribute(Constants.ENTITY_CUSTOM_VIOLATIONS, RequestAttributes.SCOPE_REQUEST);
        if (set != null && !set.isEmpty()) {
            return true;
        }
        return false;
    }


    /**
     * Gets I18N message.
     *
     * @param code
     *         the code
     * @param args
     *         the args
     * @return the message
     */
    protected String getMessage(String code, Object... args) {
        Locale locale = localeResolver.resolveLocale(null);
        return messageSource.getMessage(code, args, locale);
    }

}
