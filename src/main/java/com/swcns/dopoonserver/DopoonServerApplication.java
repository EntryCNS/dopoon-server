package com.swcns.dopoonserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationPropertiesScan
@SpringBootApplication
public class DopoonServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DopoonServerApplication.class, args);
    }

}
