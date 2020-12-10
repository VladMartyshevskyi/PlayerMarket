package com.betbull.market.infra;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class LoggingBeanPostProcessor implements BeanPostProcessor, Ordered {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(ProcessedBean.class)) {
            System.out.println("LOGGING BEAN POST PROCESSOR BEFORE INIT: " + beanName);
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(ProcessedBean.class)) {
            System.out.println("LOGGING BEAN POST PROCESSOR AFTER INIT: " + beanName);
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
