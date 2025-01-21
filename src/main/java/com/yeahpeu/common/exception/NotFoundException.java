package com.yeahpeu.common.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends BaseException{

    public NotFoundException(String code, String message) {
        super(code, message);
    }

    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}
