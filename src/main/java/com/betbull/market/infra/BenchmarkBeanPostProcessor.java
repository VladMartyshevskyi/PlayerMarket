package com.betbull.market.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class BenchmarkBeanPostProcessor implements BeanPostProcessor, Ordered {

    private final static Logger LOG = LoggerFactory.getLogger(BenchmarkBeanPostProcessor.class);

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Benchmark.class)) {
            LOG.debug("BENCHMARK BEAN POST PROCESSOR BEFORE INIT " + beanName);
        }
        return bean;
    }

    /**
     * Returns a proxy that prints time of execution of each method
     *
     * @param bean the bean
     * @param beanName the bean name
     * @return benchmark proxy
     * @throws BeansException
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean.getClass().isAnnotationPresent(Benchmark.class)) {
            LOG.debug("BENCHMARK BEAN POST PROCESSOR AFTER INIT " + beanName);
            ClassLoader classLoader = bean.getClass().getClassLoader();
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            return Proxy.newProxyInstance(classLoader, interfaces, new CustomInvocationHandler(bean));
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return 10;
    }

    /**
     * Invocation handler for benchmark proxy
     */
    static class CustomInvocationHandler implements InvocationHandler {

        private final Object original;

        public CustomInvocationHandler(Object original) {
            this.original = original;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            LOG.debug("Benchmark proxy invoked: " + original.getClass().getName() + " : " + method.getName());
            long before = System.currentTimeMillis();
            Object result = method.invoke(original, args);
            LOG.debug("Invocation took: " + (System.currentTimeMillis() - before) + " ms");
            return result;
        }
    }
}
