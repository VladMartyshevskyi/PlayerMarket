package com.betbull.market.infra.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TracedAdvice {

    private final static Logger LOG = LoggerFactory.getLogger(TracedAdvice.class);

    @After("execution(* com.betbull.market.service.impl.mail.GoogleMailServiceImpl.*(..)) && @annotation(Traced) ")
    public void trace() {
        LOG.info("Tracing aspect log");
    }

}
