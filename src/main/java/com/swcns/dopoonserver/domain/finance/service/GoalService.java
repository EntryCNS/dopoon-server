package com.swcns.dopoonserver.domain.finance.service;

import com.swcns.dopoonserver.domain.finance.entity.GoalHistory;
import com.swcns.dopoonserver.domain.finance.presentation.dto.request.GoalCreationRequest;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.ExpenditureListResponse;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.GoalHistoriesResponse;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.GoalHistoryResponse;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.GoalResponse;
import com.swcns.dopoonserver.domain.finance.type.GoalFinishType;

public interface GoalService {
    void createNewGoal(GoalCreationRequest goalCreationRequest);

    GoalResponse getMyGoal();

    void decideGoal();

    void finishGoal(GoalFinishType finishType);

    ExpenditureListResponse getExpenditures(int year, int month, int page);

    GoalHistoriesResponse getMyGoalHistories();
}
