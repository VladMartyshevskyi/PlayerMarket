package com.betbull.market.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * Example of BeanFactoryPostProcessor. Logs when processing classes with Benchmark annotation
 */
@Component
public class LoggingBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private final static Logger LOG = LoggerFactory.getLogger(LoggingBeanFactoryPostProcessor.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        String[] names = configurableListableBeanFactory.getBeanDefinitionNames();
        for (String name : names) {
            BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(name);
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                AnnotatedBeanDefinition definition = (AnnotatedBeanDefinition) configurableListableBeanFactory.getBeanDefinition(name);
                if (definition.getMetadata().hasAnnotation(Benchmark.class.getName())) {
                    LOG.debug("BEAN FACTORY POST PROCESSOR: " + name);
                }
            }
        }
    }
}
