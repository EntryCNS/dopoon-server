package com.swcns.dopoonserver.domain.finance.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor @Builder @Getter
public class UsableMoneyResponse {
    @ApiModelProperty("소비 카테고리")
    private String category;

    @ApiModelProperty("사용 가능한 돈")
    @JsonProperty("usable_money")
    private Integer usableMoney;
}
