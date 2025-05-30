package com.yeahpeu.common.exception;

public class TokenException extends BaseException {

    public TokenException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

    public TokenException(String message) {
        super("INVALID_TOKEN", message);
    }
}
