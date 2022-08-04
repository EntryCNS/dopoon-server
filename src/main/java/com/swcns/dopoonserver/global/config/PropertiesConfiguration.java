package com.swcns.dopoonserver.global.config;

import com.swcns.dopoonserver.global.security.JwtProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfiguration {
    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }
}
