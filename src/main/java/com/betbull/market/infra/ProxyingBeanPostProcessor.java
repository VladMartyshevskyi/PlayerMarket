package com.betbull.market.infra;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class ProxyingBeanPostProcessor implements BeanPostProcessor , Ordered {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(ProcessedBean.class)) {
            System.out.println("PROXYING BEAN POST PROCESSOR BEFORE INIT " + beanName);
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean.getClass().isAnnotationPresent(ProcessedBean.class)) {
            System.out.println("PROXYING BEAN POST PROCESSOR AFTER INIT " + beanName);
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

    static class CustomInvocationHandler implements InvocationHandler {

        private Object original;

        public CustomInvocationHandler(Object original) {
            this.original = original;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("Custom proxy invoked: " + original.getClass().getName() + " : " + method.getName());
            long before = System.currentTimeMillis();
            Object result = method.invoke(original, args);
            System.out.println("Invocation took: " + (System.currentTimeMillis() - before) + " ms");
            return result;
        }
    }
}
