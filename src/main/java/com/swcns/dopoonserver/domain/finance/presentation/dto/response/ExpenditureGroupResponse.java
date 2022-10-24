package com.swcns.dopoonserver.domain.finance.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ExpenditureGroupResponse {
    private String date;
    private List<ExpenditureResponse> list;
}
