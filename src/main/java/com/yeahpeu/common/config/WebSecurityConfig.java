package com.yeahpeu.common.config;

import com.yeahpeu.auth.filter.CustomLoginFilter;
import com.yeahpeu.auth.filter.CustomLogoutFilter;
import com.yeahpeu.auth.filter.JwtTokenFilter;
import com.yeahpeu.auth.handler.CustomDaoAuthenticationProvider;
import com.yeahpeu.auth.handler.CustomOAuth2SuccessHandler;
import com.yeahpeu.auth.servcie.CustomOAuth2UserService;
import com.yeahpeu.auth.servcie.JwtService;
import com.yeahpeu.auth.util.CookieUtil;
import com.yeahpeu.auth.util.JwtUtil;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.wedding.service.WeddingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * EnableWebSecurity annotation 내부에 WebSecurityConfiguration 클래스가 SecurityFilterChain을 사용하기 위해서 여기서 설정
 */

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final JwtService jwtService;
    private final CorsProperties corsProperties;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final WeddingService weddingService;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomDaoAuthenticationProvider customDaoAuthenticationProvider() {
        return new CustomDaoAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomOAuth2SuccessHandler customOAuth2SuccessHandler
    ) throws Exception {

        // formLogin, httpBasic을 사용하지 않기 때문에 disable 처리
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.sessionManagement(
                        (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/oauth2/authorization/**","/api/v1/auth/**", "/api/ws/**").permitAll()
                                .requestMatchers("/api/v1/auth/**", "/api/ws/**").permitAll()
                                .requestMatchers("/api/v1/users/signup").permitAll()
                                .requestMatchers(
                                        "/swagger-ui/**", "/login/oauth2/**", "/swagger-ui.html", "/v3/api-docs/**",
                                        "/swagger-resources/**",
                                        "/webjars/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        /**
         * 로그인 filter custom
         */
        CustomLoginFilter customLoginFilter = new CustomLoginFilter(
                authenticationManager(authenticationConfiguration),
                jwtUtil,
                userRepository,
                cookieUtil,
                weddingService
        );
        customLoginFilter.setFilterProcessesUrl("/api/v1/auth/login");
        http.addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class);

        /**
         * JWT가 유효한지 인증을 거치는 filter추가
         */
        http.addFilterBefore(new JwtTokenFilter(jwtUtil, userDetailsService), CustomLoginFilter.class);

        /**
         * 로그아웃 filter custom
         */
        CustomLogoutFilter customLogoutFilter = new CustomLogoutFilter(
                jwtService,
                cookieUtil
        );
        http.addFilterBefore(customLogoutFilter, LogoutFilter.class);

        http.headers(
                headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        );

        // OAuth 인증을 커스텀합니다.
        // 이는 properties 인증이 필수입니다.
        // endPoint : 데이터를 받을 수 있는 경로를 의미합니다.
//        http
//                .oauth2Login((oauth2) -> oauth2
//                        .loginPage("/auth") // 커스텀로그인 경로
//
//                        .userInfoEndpoint((userInfoEndpointConfig) ->
//                                userInfoEndpointConfig.userService(customOAuth2UserService))
//                        .successHandler(customOAuth2SuccessHandler));
        /**
         * OAtuh2 관련 설정
         */
        http
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customOAuth2SuccessHandler)
                );

        http.authenticationProvider(customDaoAuthenticationProvider());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(getAllowHostsFromCorsProperties())); // 클라이언트 출처 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private String[] getAllowHostsFromCorsProperties() {
        return corsProperties.getAllowHosts().toArray(String[]::new);
    }
}
