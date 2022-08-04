package com.swcns.dopoonserver.global.infra.nh.exception;

import com.swcns.dopoonserver.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CardHistoryQueryFailedException extends CustomException {
    public CardHistoryQueryFailedException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "카드기록 조회에 실패했습니다");
    }
}
