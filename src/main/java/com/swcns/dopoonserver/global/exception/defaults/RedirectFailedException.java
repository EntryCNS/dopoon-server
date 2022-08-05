package com.swcns.dopoonserver.global.exception.defaults;

import com.swcns.dopoonserver.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class RedirectFailedException extends CustomException {
    public RedirectFailedException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "리다이렉트에 실패했습니다");
    }
}
