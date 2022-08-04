package com.swcns.dopoonserver.domain.finance.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@AllArgsConstructor @NoArgsConstructor @Getter
public class GoalCreationRequest {
    @ApiModelProperty("목표 상품 이름")
    @JsonProperty("product_name")
    private String productName;

    @ApiModelProperty("가격")
    private Integer cost;

    @ApiModelProperty("원하는 기간 (월 단위)")
    @Range(min = 1, max = 12)
    private Integer month;
}
