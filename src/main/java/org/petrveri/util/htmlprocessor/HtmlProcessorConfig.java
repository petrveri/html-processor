package org.petrveri.util.htmlprocessor;

import org.petrveri.util.htmlprocessor.config.AppConfig;
import org.petrveri.util.htmlprocessor.config.TagConfiguration;
import org.petrveri.util.htmlprocessor.fs.FileSystem;
import org.petrveri.util.htmlprocessor.parsing.FileProcessor;
import org.petrveri.util.htmlprocessor.parsing.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Petro Veriienko
 */
@Configuration
public class HtmlProcessorConfig {

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

    @Bean
    public FileProcessor fileProcessor() {
        return new FileProcessor();
    }

    @Bean
    public Processor processor() {
        return new Processor();
    }

    @Bean
    public HtmlProcessorApplication htmlProcessorApplication() {
        return new HtmlProcessorApplication();
    }
}
