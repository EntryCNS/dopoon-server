package com.swcns.dopoonserver.domain.user.exception;

import com.swcns.dopoonserver.global.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다");
    }
}
