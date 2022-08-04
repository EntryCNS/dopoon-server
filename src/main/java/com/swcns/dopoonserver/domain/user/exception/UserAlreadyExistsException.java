package com.swcns.dopoonserver.domain.user.exception;

import com.swcns.dopoonserver.global.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CustomException {

    @AllArgsConstructor
    public static enum ExistsType {
        LOGIN_ID("해당 아이디를 사용하는 사용자는 이미 존재합니다"),
        EMAIL("해당 이메일을 사용하는 사용자는 이미 존재합니다");

        private final String message;
    }

    public UserAlreadyExistsException(ExistsType type) {
        super(HttpStatus.CONFLICT, type.message);
    }
}
