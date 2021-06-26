package com.deveuge.integration.chart.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class PlaceholderBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private final Properties properties;

    public PlaceholderBeanFactoryPostProcessor(final Properties properties) {
        super();
        this.properties = properties;
    }

    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof DefaultListableBeanFactory)) {
            return;
        }

        final DefaultListableBeanFactory bf = (DefaultListableBeanFactory) beanFactory;

        final String[] names = bf.getBeanDefinitionNames();
        for (final String name : names) {
            if (name.indexOf('$') < 0) {
                continue;
            }

            final BeanDefinition bd = bf.getBeanDefinition(name);
            bf.removeBeanDefinition(name);


            final String propertyName = name.substring(name.indexOf("{") + 1, name.indexOf("}"));
            final String finalName = name.replaceAll("\\$\\{.*\\}", properties.getProperty(propertyName));

            bf.registerBeanDefinition(finalName, bd);
        }
    }
}