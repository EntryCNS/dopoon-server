package com.swcns.dopoonserver.global.infra.nh.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.swcns.dopoonserver.global.infra.nh.NhHeaderService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NhRegisterHttpApi {

    @AllArgsConstructor
    @Getter
    class IssueCardRequest {
        @JsonProperty("Header")
        private NhHeaderService.NhHeader header;
        @JsonProperty("Brdt")
        private String birthday;
        @JsonProperty("Cano")
        private String cardNumber;
    }

    @AllArgsConstructor @NoArgsConstructor
    @Getter @JsonIgnoreProperties(ignoreUnknown = true) @ToString
    class IssueCardResponse {
        @JsonProperty("Header")
        private NhHeaderService.NhHeader header;

        @JsonProperty("Rgno")
        private String registerNumber;
    }

    @AllArgsConstructor
    @Getter
    class ConfirmCardRequest {
        @JsonProperty("Header")
        private NhHeaderService.NhHeader header;
        @JsonProperty("Rgno")
        private String registerNumber;
        @JsonProperty("Brdt")
        private String birthDay;
    }

    @AllArgsConstructor @NoArgsConstructor
    @Getter @JsonIgnoreProperties(ignoreUnknown = true) @ToString
    class ConfirmCardResponse {
        @JsonProperty("FinCard")
        private String finCard;
    }

    @POST("/OpenFinCardDirect.nh")
    Call<IssueCardResponse> issueCard(@Body IssueCardRequest req);

    @POST("/CheckOpenFinCardDirect.nh")
    Call<ConfirmCardResponse> confirmCard(@Body ConfirmCardRequest req);
}
