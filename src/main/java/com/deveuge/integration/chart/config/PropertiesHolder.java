package com.deveuge.integration.chart.config;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.config.PropertiesFactoryBean;

@Named("propertiesHolder")
public class PropertiesHolder {

    private Properties properties;

    @Inject
    @Named("configurationProperties")
    private PropertiesFactoryBean propertiesFactory;

    @PostConstruct
    public void init() throws IOException {
        properties = propertiesFactory.getObject();
    }

    public String getStringProperty(final String key) {
        return properties.getProperty(key);
    }

    public boolean getBooleanProperty(final String key) {
        return Boolean.valueOf(properties.getProperty(key));
    }

    public String[] getArrayStringProperty(final String key) {
        return properties.getProperty(key).split(",");
    }

}
