package com.yeahpeu.auth.servcie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class AuthTokenDTO {
    private String accessToken;
    private String refreshToken;
}
