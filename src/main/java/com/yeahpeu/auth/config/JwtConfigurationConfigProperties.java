package com.yeahpeu.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.security.jwt")
@Getter
@Setter
public class JwtConfigurationConfigProperties {
    private String secretKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}
