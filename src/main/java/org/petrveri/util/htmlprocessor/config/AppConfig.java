package org.petrveri.util.htmlprocessor.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.petrveri.util.htmlprocessor.exceptions.ApplicationException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Petro Veriienko
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AppConfig {

    private PropertiesConfiguration origConfig;
    private Configuration config;

    public String getString(String propertyKey) {
        Object property = getConfig().getProperty(propertyKey);
        if (property != null) {
            return property.toString();
        }
        throw new ApplicationException("Config Property Not Found: " + propertyKey);
    }

    public String getString(String propertyKey, String defaultValue) {
        Object property = getConfig().getProperty(propertyKey);
        if (property != null) {
            return property.toString();
        }
        return defaultValue;
    }

    public Configuration getConfig() {
        return config;
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    @PostConstruct
    private void init() throws IOException, ConfigurationException {
        origConfig = getConfiguration(PropertyKind.APP);
        PropertiesConfiguration env = getConfiguration(PropertyKind.ENV);
        Iterator<String> keysIter = env.getKeys();
        while (keysIter.hasNext()) {
            String key = keysIter.next();
            origConfig.setProperty(key, env.getProperty(key));
        }
        config = origConfig.interpolatedConfiguration();
    }

    PropertiesConfiguration getConfiguration(PropertyKind propertyKind)
            throws IOException, ConfigurationException {
        PropertiesConfiguration result;
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder;
        String propertiesFilePath = getPropertiesFilePath(propertyKind);
        File file = new File(propertiesFilePath);
        if (file.exists()) {
            builder = getConfigBuilder(propertiesFilePath);
            result = builder.getConfiguration();
        } else if (propertyKind.isEmptyIfFileAbsent()) {
            result = new PropertiesConfiguration();
        } else {
            file.createNewFile();
            builder = getConfigBuilder(propertiesFilePath);
            new DefaultConfig().setProperties(builder.getConfiguration());
            builder.save();
            result = builder.getConfiguration();
            if (result.isEmpty()) {
                file.delete();
            }
        }
        return result;
    }

    private FileBasedConfigurationBuilder<PropertiesConfiguration> getConfigBuilder(String propertiesFilePath) {
        return new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(new Parameters().properties().setFileName(propertiesFilePath));
    }

    String getPropertiesFilePath(PropertyKind propertyKind) {
        String propertiesFilePath = System.getProperty(propertyKind.getKindPropertyName());
        if (StringUtils.isEmpty(propertiesFilePath)) {
            propertiesFilePath = propertyKind.getDefaultFilePath();
        }
        return propertiesFilePath;
    }

    enum PropertyKind {
        APP("app.properties.file.name", false, "app.properties"),
        ENV("env.properties.file.name", true, "env.properties");

        private final String kindPropertyName;
        private final boolean emptyIfFileAbsent;
        private final String defaultFilePath;

        PropertyKind(String kindPropertyName, boolean emptyIfFileAbsent, String defaultFilePath) {
            this.kindPropertyName = kindPropertyName;
            this.emptyIfFileAbsent = emptyIfFileAbsent;
            this.defaultFilePath = defaultFilePath;
        }

        String getKindPropertyName() {
            return kindPropertyName;
        }

        String getDefaultFilePath() {
            return defaultFilePath;
        }

        boolean isEmptyIfFileAbsent() {
            return emptyIfFileAbsent;
        }
    }
}
