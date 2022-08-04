package com.swcns.dopoonserver.domain.finance.service;

import com.swcns.dopoonserver.domain.finance.presentation.dto.request.GoalCreationRequest;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.ExpenditureListResponse;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.GoalResponse;

public interface GoalService {
    void createNewGoal(GoalCreationRequest goalCreationRequest);

    GoalResponse getMyGoal();

    void decideGoal();

    ExpenditureListResponse getExpenditures(int year, int month, int page);
}
