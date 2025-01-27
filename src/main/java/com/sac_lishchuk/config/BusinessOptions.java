package com.sac_lishchuk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sac")
public class BusinessOptions {
    private PasswordConfig passwordConfig;

    @Data
    public static class PasswordConfig {
        private Integer minComplexLength;
    }
}
