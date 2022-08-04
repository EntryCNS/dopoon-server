package com.swcns.dopoonserver.global.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secretKey;
    private Long accessExp;
    private Long refreshExp;

}
