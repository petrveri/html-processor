package org.petrveri.util.htmlprocessor.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.petrveri.util.htmlprocessor.exceptions.ApplicationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests App configuration
 *
 * @author Petro Veriienko
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfigTestConfig.class}, loader = AnnotationConfigContextLoader.class)
public class AppConfigTest {
    @Inject
    private AppConfig appConfig;

    @Test
    public void testConfigOK() {
        assertNotNull(appConfig);
        assertNotNull(appConfig.getConfig());
        assertNotNull(appConfig.getConfig().getProperty(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH));
    }

    @Test
    public void testPropertyNotFound() {
        assertNotNull(appConfig);
        assertNotNull(appConfig.getConfig());
        assertNull(appConfig.getConfig().getProperty("unreal.property.key"));
    }

    @Test
    public void testPropertyStringOK() {
        assertNotNull(appConfig);
        assertNotNull(appConfig.getString(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH));
        assertEquals(appConfig.getConfig().getProperty(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH),
                appConfig.getString(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH));
    }

    @Test
    public void testPropertyStringNotFound() {
        assertNotNull(appConfig);
        Throwable ex = assertThrows(ApplicationException.class, () -> {
            appConfig.getString("unreal.property.key");
        });
        assertEquals("Config Property Not Found: unreal.property.key", ((ApplicationException) ex).getCustomMessage());
    }

    @Test
    public void testPropertiesFilePath() {
        checkPropertiesFilePath(AppConfig.PropertyKind.APP);
        checkPropertiesFilePath(AppConfig.PropertyKind.ENV);
    }

    private void checkPropertiesFilePath(AppConfig.PropertyKind propertyKind) {
        String propertiesFilePath = System.getProperty(propertyKind.getKindPropertyName());
        if (StringUtils.isEmpty(propertiesFilePath)) {
            assertEquals(propertyKind.getDefaultFilePath(), appConfig.getPropertiesFilePath(propertyKind));
        } else {
            assertEquals(propertiesFilePath, appConfig.getPropertiesFilePath(propertyKind));
        }
    }

    @Test
    public void testConfigurationEmpty() throws IOException, ConfigurationException {
        checkConfigurationEmpty(AppConfig.PropertyKind.APP);
        checkConfigurationEmpty(AppConfig.PropertyKind.ENV);
    }

    private void checkConfigurationEmpty(AppConfig.PropertyKind propertyKind)
            throws IOException, ConfigurationException {
        String propertiesFilePath = appConfig.getPropertiesFilePath(propertyKind);
        File file = new File(propertiesFilePath);
        if (!file.exists() && propertyKind.isEmptyIfFileAbsent()) {
            assertTrue(appConfig.getConfiguration(propertyKind).isEmpty());
        } else {
            assertFalse(appConfig.getConfiguration(propertyKind).isEmpty());
        }
    }

    @Test
    public void testConfigurationMerge() throws IOException, ConfigurationException {
        Configuration app = appConfig.getConfiguration(AppConfig.PropertyKind.APP);
        Configuration env = appConfig.getConfiguration(AppConfig.PropertyKind.ENV);
        Configuration common = appConfig.getConfig();
        if (env.isEmpty()) {
            assertEquals(app.getProperty(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH),
                    common.getProperty(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH));
        } else {
            assertEquals(env.getProperty(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH),
                    common.getProperty(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH));
        }
    }
}

