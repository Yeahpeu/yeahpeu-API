package com.yeahpeu.auth.servcie;

import com.yeahpeu.auth.servcie.dto.JwtDto;
import com.yeahpeu.auth.util.CookieUtil;
import com.yeahpeu.auth.util.JwtUtil;
import com.yeahpeu.common.exception.ExceptionCode;
import com.yeahpeu.common.exception.TokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    JwtServiceImpl(final JwtUtil jwtUtil, final CookieUtil cookieUtil) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public JwtDto reissueRefreshToken(String refreshToken) {

        checkRefreshTokenError(refreshToken);

        String userEmail = jwtUtil.getSubject(refreshToken);
        return setTokens(userEmail);
    }

    @Override
    public void checkRefreshTokenError(String refreshToken) {

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (Exception e) {
            throw new TokenException(ExceptionCode.TOKEN_INVALID);
        }
    }

    @Override
    public JwtDto setTokens(String userEmail) {

        String accessToken = jwtUtil.createAccessToken(userEmail);
        String refreshToken = jwtUtil.createRefreshToken(userEmail);
        Cookie refreshTokenCookie = cookieUtil.createCookie("refresh", refreshToken);

        return JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenCookie)
                .build();
    }

    @Override
    public String getRefreshToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) return cookie.getValue();
        }
        return null;
    }
}
