package com.yeahpeu.common.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends BaseException {

    public BadRequestException(String code, String message) {
        super(code, message);
    }

    public BadRequestException(String message) {
        super("BAD_REQUEST", message);
    }
}
