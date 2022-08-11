package com.swcns.dopoonserver.domain.finance.presentation.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class GoalHistoriesResponse {
    @ApiModelProperty("목표 기록")
    private List<GoalHistoryResponse> histories;
}
