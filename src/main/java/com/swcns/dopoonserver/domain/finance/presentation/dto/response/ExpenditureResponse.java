package com.swcns.dopoonserver.domain.finance.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ExpenditureResponse {

    @ApiModelProperty("결제 시점")
    @JsonProperty("purchased_at")
    private String purchasedAt;

    @ApiModelProperty("결제 업체명")
    private String store;

    @ApiModelProperty("소비 카테고리")
    private String category;

    @ApiModelProperty("결제 금액")
    private int bill;
}
