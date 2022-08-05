package com.swcns.dopoonserver.global.infra.finance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@AllArgsConstructor @ConstructorBinding
@ConfigurationProperties("infra.finance")
public class FinanceProperties {
    private String redirectUri;
    private String clientId;
    private String clientSecret;
    private String useOrganizationCode;
}
