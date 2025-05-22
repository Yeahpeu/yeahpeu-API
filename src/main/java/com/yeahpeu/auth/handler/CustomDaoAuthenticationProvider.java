package com.yeahpeu.auth.handler;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {
    public CustomDaoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        super();
        setHideUserNotFoundExceptions(false);  // 사용자 없을 경우 예외 숨기지 않음
        setUserDetailsService(userDetailsService);  // UserDetailsService 설정
        setPasswordEncoder(passwordEncoder);  // PasswordEncoder 설정
    }
}
