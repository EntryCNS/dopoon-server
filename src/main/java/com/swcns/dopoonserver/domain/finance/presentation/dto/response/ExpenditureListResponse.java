package com.swcns.dopoonserver.domain.finance.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor @Builder @Getter
public class ExpenditureListResponse {

    @ApiModelProperty("현재 페이지")
    private int page;

    @ApiModelProperty("총 페이지 개수")
    @JsonProperty("page_count")
    private int pageCount;

    @ApiModelProperty("조회 결과")
    private List<ExpenditureGroupResponse> result;
}
