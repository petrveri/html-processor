package org.petrveri.util.htmlprocessor.parsing;

import org.petrveri.util.htmlprocessor.config.AppConfig;
import org.petrveri.util.htmlprocessor.config.TagConfiguration;
import org.petrveri.util.htmlprocessor.fs.FileSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Petro Veriienko
 */
@Configuration
public class FileProcessorTestConfig {
    @Bean
    public Processor processor() {
        return new Processor();
    }

    @Bean
    public FileProcessor fileProcessor() {
        return new FileProcessor();
    }

    @Bean
    public FileSystem fileSystem() {
        return new FileSystem();
    }

    @Bean
    public TagConfiguration tagConfiguration() {
        return new TagConfiguration();
    }

    @Bean
    public AppConfig appConfig() {
        return new AppConfig();
    }
}
