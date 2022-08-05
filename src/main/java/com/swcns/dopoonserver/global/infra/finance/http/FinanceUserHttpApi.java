package com.swcns.dopoonserver.global.infra.finance.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface FinanceUserHttpApi {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    class FinanceBankResponse {
        private String fintechUseNum;
        private String accountAlias;
        private String bankName;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor @NoArgsConstructor
    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    class FinanceUserResponse {
        private String userName;
        private List<FinanceBankResponse> resList;
    }

    @GET("/v2.0/user/me")
    Call<FinanceUserResponse> getUserInfoByNumber(
            @Header("Authorization") String token,
            @Query("user_seq_no") String userSequenceNumber
    );
}
