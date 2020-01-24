package org.petrveri.util.htmlprocessor.config;

import org.apache.commons.configuration2.Configuration;

/**
 * Defines and sets default values for configuration properties.
 *
 * @author Petro Veriienko
 */
public class DefaultConfig {
//    public static final String FILE_LINE_SEPARATOR = "\r\n";
    public static final String DEFAULT_OUTPUT_DIRECTORY_PATH = "./output/";
    public static final String DEFAULT_INPUT_DIRECTORY_PATH = "./";

    void setProperties(Configuration config) {
//        config.setProperty(ConfigKeys.FILE_LINE_SEPARATOR, FILE_LINE_SEPARATOR);
//        config.setProperty(ConfigKeys.DEFAULT_OUTPUT_DIRECTORY_PATH, DEFAULT_OUTPUT_DIRECTORY_PATH);
//        config.setProperty(ConfigKeys.DEFAULT_INPUT_DIRECTORY_PATH, DEFAULT_INPUT_DIRECTORY_PATH);
    }
}