package com.yeahpeu.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.auth.util.CookieUtil;
import com.yeahpeu.auth.util.JwtUtil;
import com.yeahpeu.common.exception.ExceptionResponse;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.wedding.service.WeddingService;
import com.yeahpeu.wedding.service.dto.BoardingCheckDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CookieUtil cookieUtil;
    private final WeddingService weddingService;

    // 로그인시에만 진행
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String userId = obtainUsername(request);
        String userPassword = obtainPassword(request);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userId, userPassword);

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    //인가에 성공한 경우
    //security context holder 에 인증용 객체 저장 후 아래 메서드 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // 해당 id 에 맞는 유저 로드
        String userEmail = ((UserPrincipal) authResult.getPrincipal()).getEmailAddress();
        // Token 형성
        String accessToken = jwtUtil.createAccessToken(String.valueOf(userEmail));
        String refreshToken = jwtUtil.createRefreshToken(String.valueOf(userEmail));

        //response.setHeader("Authorization", "Bearer " + accessToken);

        // add Cookie : refreshToken 합니다.
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("set-cookie", refreshToken);
//        Cookie cookie = cookieUtil.createCookie("refreshToken", refreshToken);
//        response.addCookie(cookie);

        // getName 으로 반환되는 내용을 id 에서 emailAdress 로 변경하면서 제거
        //sendOnboardingStatusResponse(response, userId);
    }

    // 인가에 실패한 경우
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        // 응답 상태를 401 Unauthorized로 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ExceptionResponse exceptionResponse;

        // 비밀번호가 틀렸을 경우 (BadCredentialsException)
        if (failed instanceof org.springframework.security.authentication.BadCredentialsException) {
            exceptionResponse = new ExceptionResponse("BAD_CREDENTIALS", "아이디(혹은 비밀번호)가 틀렸습니다\n정확히 입력해 주세요!");
        }
        // 사용자 존재하지 않을 경우 (UsernameNotFoundException)
        else if (failed instanceof org.springframework.security.core.userdetails.UsernameNotFoundException) {
            exceptionResponse = new ExceptionResponse("NOT_FOUND", "존재하지 않는 회원입니다.\n회원가입을 해주세요!");
        } else {
            // 그 외의 인증 실패 상황에 대한 기본 에러 메시지
            exceptionResponse = new ExceptionResponse("AUTHENTICATION_FAILED", "로그인에 실패하였습니다. 다시 시도해주세요!");
        }

        // JSON 형태로 응답
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), exceptionResponse);
    }


    private void sendOnboardingStatusResponse(HttpServletResponse response, Long userId) throws IOException {
        BoardingCheckDto onboarded = weddingService.isOnboarded(userId);

        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), onboarded);
    }
}
