package com.yeahpeu.auth.servcie.dto;

import jakarta.servlet.http.Cookie;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtDto {
    private String accessToken;
    private Cookie refreshToken;
}
