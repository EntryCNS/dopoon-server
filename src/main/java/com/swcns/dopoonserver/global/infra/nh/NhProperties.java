package com.swcns.dopoonserver.global.infra.nh;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@AllArgsConstructor @NoArgsConstructor
@Setter @Getter
@ConstructorBinding
@ConfigurationProperties("api.nh")
public class NhProperties {
    private String apiAccessKey;
    private String endpoint;
    private String organizationCode;
}
