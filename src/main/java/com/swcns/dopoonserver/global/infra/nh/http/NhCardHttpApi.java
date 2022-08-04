package com.swcns.dopoonserver.global.infra.nh.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.swcns.dopoonserver.global.infra.nh.NhHeaderService;
import lombok.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

public interface NhCardHttpApi {
    @AllArgsConstructor @Builder
    class CardHistoryRequest {
        @JsonProperty("Header")
        private NhHeaderService.NhHeader header;

        @JsonProperty("IousDsnc")
        private String iousDsnc;

        @JsonProperty("Insymd")
        private String insymd;

        @JsonProperty("Ineymd")
        private String ineymd;

        @JsonProperty("PageNo")
        private String pageNumber;

        @JsonProperty("Dmcnt")
        private String count;

        @JsonProperty("FinCard")
        private String finCard;
    }

    @AllArgsConstructor @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter @ToString
    public class CardHistoryResponse {
        @JsonProperty("Header")
        private NhHeaderService.NhHeader header;

        @JsonProperty("PageNo")
        private String pageNo;

        @JsonProperty("CtntDataYn")
        private String hasNextData;

        @JsonProperty("REC")
        private List<HistoryDetail> records;

        @AllArgsConstructor @NoArgsConstructor
        @Getter @JsonIgnoreProperties(ignoreUnknown = true) @ToString
        public static class HistoryDetail {
            @JsonProperty("AfstNm")
            private String storeName;

            @JsonProperty("Usam")
            private String cost;

            @JsonProperty("AfstNoBrno")
            private String businessRegistrationNumber;

            @JsonProperty("Trdd")
            private String billYmd;

            @JsonProperty("Txtm")
            private String billHms;
        }
    }

    @POST("/InquireCreditCardAuthorizationHistory.nh")
    Call<CardHistoryResponse> getCardHistory(@Body CardHistoryRequest req);

}
