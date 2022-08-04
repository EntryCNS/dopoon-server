package com.swcns.dopoonserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {
    private final Integer code;
    private final String message;
}
