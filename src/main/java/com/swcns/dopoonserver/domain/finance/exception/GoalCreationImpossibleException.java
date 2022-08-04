package com.swcns.dopoonserver.domain.finance.exception;

import com.swcns.dopoonserver.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class GoalCreationImpossibleException extends CustomException {
    public GoalCreationImpossibleException() {
        super(HttpStatus.BAD_REQUEST, "실현할 수 없는 계획입니다");
    }
}
