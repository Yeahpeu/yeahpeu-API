package com.yeahpeu.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class BaseException extends RuntimeException{
    private final String code;
    private final String message;

    public BaseException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseException(String code, String message, Throwable cause) {
        this.code = code;
        this.message = message;
        log.error("[{}] {} - Cause: {}", code, message, cause.getMessage());
    }
}
