package com.yeahpeu.auth.handler;

import com.yeahpeu.auth.servcie.dto.oauth2dto.CustomOAuth2User;
import com.yeahpeu.auth.util.CookieUtil;
import com.yeahpeu.auth.util.JwtUtil;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final CookieUtil cookieUtil;
    private final UserRepository userRepository;
    //@Value("${front-end.url}")
    private String frontEndUrl = "https://yeahpeu.site/";
    //private String frontEndUrl = "http://localhost:3000/";

    private final JwtUtil jwtUtil;

    public CustomOAuth2SuccessHandler(JwtUtil jwtUtil, CookieUtil cookieUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        onAuthenticationUserSuccess(response, oAuth2User);
    }

    private void onAuthenticationUserSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User)
            throws IOException, ServletException {

        String emailAddress = oAuth2User.getEmailAddress();

        UserEntity userEntity = userRepository.findByEmailAddress(emailAddress).orElse(null);

        // Token 생성
        String accessToken = jwtUtil.createAccessToken(emailAddress);
        String refreshToken = jwtUtil.createRefreshToken(emailAddress);
        //System.out.println("cookie is: " + cookie);

        // 토큰 설정
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        Cookie accessCookie = cookieUtil.createCookie("authToken", accessToken);
        Cookie refreshCookie = cookieUtil.createCookie("refreshToken", refreshToken);
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        response.sendRedirect(frontEndUrl + "oauth");


        // 1. 프론트의 경로로 리다이렉트 하는 경우
        //response.sendRedirect(frontEndUrl + "?token=" + accessToken);
        //System.out.println("redirect to: " + frontEndUrl + "?token=" + accessToken);

        // 2. 바로 응답으로 전송

//        // 응답 본문에 Access Token을 JSON으로 포함
//        Map<String, String> tokenMap = new HashMap<>();
//        tokenMap.put("accessToken", accessToken);
//
//        // Refresh Token은 쿠키로 설정되었으므로 응답 본문에 포함하지 않음
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.writeValue(response.getWriter(), tokenMap);
//
//        response.flushBuffer();


    }
}
