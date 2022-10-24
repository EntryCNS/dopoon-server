package com.swcns.dopoonserver.global.infra.kakao;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("infra.kakao")
public class KakaoApiProperties {
    private String apiKey;
}
