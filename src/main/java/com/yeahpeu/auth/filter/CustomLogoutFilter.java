package com.yeahpeu.auth.filter;

import com.yeahpeu.auth.servcie.JwtService;
import com.yeahpeu.auth.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

public class CustomLogoutFilter extends GenericFilterBean {


    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    public CustomLogoutFilter(
            JwtService jwtService,
            CookieUtil cookieUtil
    ) {
        this.jwtService = jwtService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        if (!requestURI.startsWith("/api/v1/auth/logout") || !requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.convertToString(cookieUtil.deleteCookie("refresh")));

        //response 추가하기
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        response.setContentType("application/json;charset=UTF-8");
        String errorMessage = "{\"error\": \"" + "로그아웃 성공" + "\"}";
        response.getWriter().write(errorMessage);
        //ResponseMessage.setHeadersResponse(response, AuthResponseCode.LOGOUT_SUCCESS, headers);
    }
}
