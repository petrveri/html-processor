package org.petrveri.util.htmlprocessor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Petro Veriienko
 */
@Configuration
public class AppConfigTestConfig {
    @Bean
    public AppConfig appConfig() {
        return new AppConfig();
    }
}
