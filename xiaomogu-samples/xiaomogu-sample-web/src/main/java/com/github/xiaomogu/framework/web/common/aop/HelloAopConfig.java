package com.github.xiaomogu.framework.web.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @auther JameHou
 * @date 2019/1/19 11:24
 */
@Aspect
@Component
@Slf4j
public class HelloAopConfig {

   // @Around("execution(* com.github.xiaomogu.framework.web.controller.HelloWorldController.getUserById(..))")
    public Object helloAop(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("执行aop操作...........................");
        return joinPoint.proceed();
    }
}
