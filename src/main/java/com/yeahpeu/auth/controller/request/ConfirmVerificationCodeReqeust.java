package com.yeahpeu.auth.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ConfirmVerificationCodeReqeust {
    private String emailAddress;
    private String authCode;
}
