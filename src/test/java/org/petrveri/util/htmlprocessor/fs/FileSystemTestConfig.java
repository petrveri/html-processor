package org.petrveri.util.htmlprocessor.fs;

import org.petrveri.util.htmlprocessor.config.AppConfig;
import org.springframework.context.annotation.Bean;

/**
 * @author Petro Veriienko
 */
public class FileSystemTestConfig {
    @Bean
    public AppConfig appConfig() {
        return new AppConfig();
    }

    @Bean
    public FileSystem fileSystem() {
        return new FileSystem();
    }
}
