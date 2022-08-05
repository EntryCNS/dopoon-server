package com.swcns.dopoonserver.global.infra.finance.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FinanceTokenHttpApi {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor @NoArgsConstructor
    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    class FinanceTokenResponse {
        private String accessToken;
        private String refreshToken;
        private String userSeqNo;
    }

    @FormUrlEncoded
    @POST("/oauth/2.0/token")
    Call<FinanceTokenResponse> getTokenByCode(
        @Field("code") String code,
        @Field("client_id") String clientId,
        @Field("client_secret") String clientSecret,
        @Field("redirect_uri") String redirectUri,
        @Field("grant_type") String grantType
    );

}
