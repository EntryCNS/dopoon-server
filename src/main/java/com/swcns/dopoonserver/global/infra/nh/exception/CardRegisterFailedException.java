package com.swcns.dopoonserver.global.infra.nh.exception;

import com.swcns.dopoonserver.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CardRegisterFailedException extends CustomException {
    public CardRegisterFailedException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "카드 등록에 실패했습니다");
    }
}
