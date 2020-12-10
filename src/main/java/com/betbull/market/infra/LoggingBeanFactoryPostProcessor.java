package com.betbull.market.infra;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        String[] names = configurableListableBeanFactory.getBeanDefinitionNames();
        for (String name : names) {
            BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(name);
            if (beanDefinition instanceof  AnnotatedBeanDefinition) {
                AnnotatedBeanDefinition definition = (AnnotatedBeanDefinition) configurableListableBeanFactory.getBeanDefinition(name);
                if (definition.getMetadata().hasAnnotation(ProcessedBean.class.getName())) {
                    System.out.println("BEAN FACTORY POST PROCESSOR: " + name);
                }
            }
        }
    }
}
