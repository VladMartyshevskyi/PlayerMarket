package com.betbull.market.infra.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TracedAdvice {

    private final static Logger LOG = LoggerFactory.getLogger(TracedAdvice.class);

    @Around("execution(* com.betbull.market.service.impl.mail.GoogleMailServiceImpl.*(..)) && @annotation(Traced)")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        LOG.debug("Tracing aspect: invocation of " + joinPoint.getSignature().getName() + " took " + (System.currentTimeMillis() - startTime));
        return result;
    }


}
