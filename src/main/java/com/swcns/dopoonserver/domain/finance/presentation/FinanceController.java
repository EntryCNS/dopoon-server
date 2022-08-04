package com.swcns.dopoonserver.domain.finance.presentation;

import com.swcns.dopoonserver.domain.finance.presentation.dto.request.GoalCreationRequest;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.ExpenditureListResponse;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.GoalResponse;
import com.swcns.dopoonserver.domain.finance.service.GoalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api("금융 관련 API")
@RequestMapping("/finance")
@RestController
@RequiredArgsConstructor
public class FinanceController {
    private final GoalService goalService;

    @ApiOperation("새로운 목표를 생성합니다")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/goal")
    void createNewGoal(@RequestBody @Valid GoalCreationRequest goalCreationRequest) {
        goalService.createNewGoal(goalCreationRequest);
    }

    @ApiOperation("목표와 소비 계획을 조회합니다")
    @GetMapping("/goal")
    GoalResponse getGoal() {
        return goalService.getMyGoal();
    }

    @ApiOperation(value = "목표 확정")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/goal")
    public void decideGoal() {
        goalService.decideGoal();
    }

    @ApiOperation(value = "지출 내역 조회")
    @GetMapping("/expenditures")
    public ExpenditureListResponse getExpenditures(int year, int month, @RequestParam(value = "page", defaultValue = "0") int page) {
        return goalService.getExpenditures(year, month, page);
    }
}
