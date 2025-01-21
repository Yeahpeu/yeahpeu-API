package com.yeahpeu.common.exception;

public class TokenException extends BaseException {

    public TokenException(String code, String message) {
        super(code, message);
    }

    public TokenException(String message) {
        super("INVALID_TOKEN", message);
    }

    public TokenException(String message, Throwable cause) {
        super("INVALID_TOKEN", message, cause);
    }
}
