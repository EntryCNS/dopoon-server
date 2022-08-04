package com.swcns.dopoonserver.global.infra.nh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class NhHeaderService {

    private final NhProperties nhProperties;
    private static final AtomicInteger atomicInteger = new AtomicInteger();

    @AllArgsConstructor @NoArgsConstructor
    @Builder @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class NhHeader {
        @JsonProperty("ApiNm")
        private String ApiNm;

        @JsonProperty("Tsymd")
        private String Tsymd;

        @JsonProperty("Trtm")
        private String Trtm;

        @JsonProperty("Iscd")
        private String Iscd;

        @JsonProperty("FintechApsno")
        private String FintechApsno;

        @JsonProperty("ApiSvcCd")
        private String ApiSvcCd;

        @JsonProperty("IsTuno")
        private String IsTuno;

        @JsonProperty("AccessToken")
        private String AccessToken;

        @JsonProperty("Rsms")
        private String message;
    }

    public NhHeader getNhHeader(String method) {
        SimpleDateFormat mdFormatter = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat tmFormatter = new SimpleDateFormat("hhmmss");


        return NhHeader.builder()
                .ApiNm(method)
                .Tsymd(mdFormatter.format(new Date()))
                .Trtm(tmFormatter.format(new Date()))
                .Iscd(nhProperties.getOrganizationCode()) // 기관코드
                .FintechApsno("001")
                .ApiSvcCd("DrawingTransferA") // 서비스코드
                .IsTuno(System.currentTimeMillis() + "") // 랜덤번호
                .AccessToken(nhProperties.getApiAccessKey())
                .build();
    }
}
