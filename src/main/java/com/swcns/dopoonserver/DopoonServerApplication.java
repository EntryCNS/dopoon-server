package com.swcns.dopoonserver;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableBatchProcessing
@ConfigurationPropertiesScan
@SpringBootApplication
public class DopoonServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DopoonServerApplication.class, args);
    }

}
