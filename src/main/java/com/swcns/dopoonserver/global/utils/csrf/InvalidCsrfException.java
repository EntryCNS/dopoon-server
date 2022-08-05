package com.swcns.dopoonserver.global.utils.csrf;

import com.swcns.dopoonserver.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidCsrfException extends CustomException {
    public InvalidCsrfException() {
        super(HttpStatus.UNAUTHORIZED, "잘못된 CSRF 토큰입니다");
    }
}
