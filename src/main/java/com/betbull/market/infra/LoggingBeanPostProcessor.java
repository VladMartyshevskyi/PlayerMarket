package com.betbull.market.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Example of BeanPostProcessor. Logs when processing classes Benchmark annotation. Ordered
 */
@Component
public class LoggingBeanPostProcessor implements BeanPostProcessor, Ordered {

    private final static Logger LOG = LoggerFactory.getLogger(LoggingBeanFactoryPostProcessor.class);

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Benchmark.class)) {
            LOG.debug("LOGGING BEAN POST PROCESSOR BEFORE INIT: " + beanName);
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Benchmark.class)) {
            LOG.debug("LOGGING BEAN POST PROCESSOR AFTER INIT: " + beanName);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
