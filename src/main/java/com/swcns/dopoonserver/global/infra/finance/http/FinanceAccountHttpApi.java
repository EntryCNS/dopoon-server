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

public interface FinanceAccountHttpApi {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    class FinanceTransactionResponse {
        private String tranDate;
        private String tranTime;
        private String printContent;
        private String tranAmt;
        private String branchName;
    }


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    class FinanceBillResponse {
        private String balanceAmt;
        private List<FinanceTransactionResponse> resList;
    }

    @GET("/oauth/2.0/token")
    Call<FinanceBillResponse> getBillsByFintechNumber(
            @Header("Authorization") String token,
            @Query("bank_tran_id") String bankTranId,
            @Query("fintech_use_num") String bankUserNum,
            @Query("inquiry_type") String inquiryType,
            @Query("inquiry_base") String inquiryBase,
            @Query("from_date") String fromDate,
            @Query("to_date") String toDate,
            @Query("sort_order") String sortOrder,
            @Query("tran_dtime") String tranDtime
    );
}
