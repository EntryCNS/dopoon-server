package com.swcns.dopoonserver.domain.finance.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor @Builder @Getter
public class GoalResponse {
    @ApiModelProperty("목표 상품 이름")
    @JsonProperty("product_name")
    private String productName;

    @ApiModelProperty("가격")
    private Integer cost;

    @ApiModelProperty("이미지 URL")
    @JsonProperty("image_url")
    private String imageUrl;

    @ApiModelProperty("시작 일자")
    @JsonProperty("start_at")
    private String startAt;

    @ApiModelProperty("종료 일자")
    @JsonProperty("finish_at")
    private String finishAt;

    @ApiModelProperty("확정 여부")
    @JsonProperty("is_decided")
    private Boolean isDecided;

    @ApiModelProperty("실현 계획")
    private List<UsableMoneyResponse> details;
}
