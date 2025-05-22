package com.yeahpeu.auth.servcie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreateVerificationCodeDTO {
    private Boolean verificationCodeCreated;
    private Boolean authStatus;
}

