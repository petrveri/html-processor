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
public class ProcessorTestConfig {
    @Bean
    public Processor processor() {
        return new Processor();
    }

    @Bean
    public FileSystem fileSystem() {
        return new FileSystem();
    }

    @Bean
    public AppConfig appConfig() {
        return new AppConfig();
    }
}
