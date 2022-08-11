package com.swcns.dopoonserver.domain.finance.presentation.dto.request;

import com.swcns.dopoonserver.domain.finance.type.GoalFinishType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Getter
public class FinishGoalRequest {
    @ApiModelProperty(dataType = "string", allowableValues = "SUCCESS, GIVEUP", value = "목표를 종료하는 사유")
    private GoalFinishType reason;
}
