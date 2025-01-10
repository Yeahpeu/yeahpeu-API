package com.yeahpeu.config;

//import com.example.demo.auth.service.CustomOAuth2UserService;


import com.yeahpeu.auth.filter.LoginFilter;
import com.yeahpeu.auth.service.CustomOAuth2UserService;
import com.yeahpeu.user.entity.RoleType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,
                          CustomOAuth2UserService customOAuth2UserService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.customOAuth2UserService = customOAuth2UserService;
    }


    // @Bean 을 통해 password 암호화 클래스를 주입해야 합니다. 이 클래스 기반으로 비밀번호를 암호화시켜줍니다.
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    //private final JwtTokenFilter jwtTokenFilter;


    // 수직적 계층을 표현하는 클래스입니다.
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withRolePrefix("ROLE_")
                .role(RoleType.ADMIN.toString()).implies(RoleType.USER.toString())
                .build();
    }

    // 본격적인 시큐리티 설정 클래스입니다.
    @Bean
    public SecurityFilterChain FilterChain(HttpSecurity http
                                                   ) throws Exception {
        //CustomOAuth2UserService customOAuth2UserService

        //인증, 인가, 세션 등의 설정을 진행할 수 있습니다.


        // 1. csrf 보안을 해제해야 합니다. (개발환경에서만)
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        );

        // OAuth 인증을 커스텀합니다.
        // 이는 properties 인증이 필수입니다.
        // endPoint : 데이터를 받을 수 있는 경로를 의미합니다.
        http
                .oauth2Login((oauth2)-> oauth2
                        .loginPage("/login") // 커스텀로그인 경로
                        .userInfoEndpoint((userInfoEndpointConfig) ->
                                userInfoEndpointConfig.userService(customOAuth2UserService) ));


        // 2. 접근 경로별 인가를 설정합니다. (현재는 모든 사람이 모든 경로에 대해 접근 가능합니다.)
        http
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/", "/oauth2/**", "/login/**").permitAll()
                        .requestMatchers("/user/join").permitAll()
                        .requestMatchers("/user/update/**").hasRole("USER") //유저권한만 접근
                        .anyRequest().authenticated()); // 마지막행 - 이외의 경로는 인증받은자들만.



        //시큐리티에서 주입할 필터 클래스 등록
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

        // 3. 로그인 방식을 설정합니다. (Form 로그인 방식)
        http
                .formLogin((loginForm) ->loginForm.disable());

        http
                .httpBasic((basic)->basic.disable());


        // 작성중. Security는 POST, PUT 등의 요청에 대해 요청 헤더에 CSRF 토큰을 넣는 CSRF 필터를 기본적으로 사용
        // -> CSRF 토큰을 넣지 않으면 403 에러 발생합니다.
//        http.sessionManagement(
//                        (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                ).authorizeHttpRequests(
//                        authorize -> authorize
//                                .requestMatchers("/api/v1/auth/**", "/api/ws/**", "/h2-console/**").permitAll()
//                                .requestMatchers(
//                                        "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**"
//                                ).permitAll()
//                                .anyRequest().authenticated()
//                ).addFilterBefore(jwtTokenFilter, BasicAuthenticationFilter.class)
//                .csrf(AbstractHttpConfigurer::disable)
//
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()));


        //http.oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);

        // 설정을 등록합니다.
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
//        configuration.setAllowedOrigins(List.of(getAllowHostsFromCorsProperties())); // 클라이언트 출처 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    private String[] getAllowHostsFromCorsProperties() {
//        return corsProperties.getAllowHos.ts().toArray(String[]::new);
//    }
}
