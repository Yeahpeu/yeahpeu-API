package com.yeahpeu.auth.servcie;

import com.yeahpeu.auth.servcie.dto.JwtDto;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {
    JwtDto reissueRefreshToken(String refreshToken);
    void checkRefreshTokenError(String refreshToken);
    JwtDto setTokens(String userId);
    String getRefreshToken(HttpServletRequest request);
}
