package com.busap.vcs.operate.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogIntercept {

    @Around("execution(public * com.busap.vcs.*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Long start = System.currentTimeMillis();
        Object obj = pjp.proceed();
        Long end = System.currentTimeMillis();
        System.err.println("执行" + pjp.getTarget().getClass().getSimpleName() + "." + pjp.getSignature().getName() + pjp.getArgs() + "用时:" + (end - start) + "ms");
        return obj;
    }
}