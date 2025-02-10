package com.sac_lishchuk.config;

import com.sac_lishchuk.enums.Role;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "sac")
public class BusinessOptions {
    private PasswordConfig passwordConfig;
    private Map<Role, List<Role>> roleCreatePermitList;


    @Data
    public static class PasswordConfig {
        private Integer minComplexLength;
    }
}
