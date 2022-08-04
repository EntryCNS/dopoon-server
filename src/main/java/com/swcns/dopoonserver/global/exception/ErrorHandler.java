package com.swcns.dopoonserver.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleException(CustomException ex) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code(ex.getCode().value())
                        .message(ex.getMessage())
                        .build(),
                ex.getCode()
        );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BindException ex) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code(400)
                        .message("Bad request")
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBadParamRequest(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code(400)
                        .message("올바르지 않은 요청: " + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
