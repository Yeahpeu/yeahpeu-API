package com.yeahpeu.auth.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateVerificationCodeRequest {
    private String emailAddress;
}
