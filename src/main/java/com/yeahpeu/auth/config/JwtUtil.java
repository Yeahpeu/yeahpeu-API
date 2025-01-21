package com.yeahpeu.auth.config;

import com.yeahpeu.common.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@EnableConfigurationProperties(JwtConfigurationConfigProperties.class)
public class JwtUtil {


    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(JwtConfigurationConfigProperties jwtConfig) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = jwtConfig.getAccessTokenExpiration();
        this.refreshTokenExpiration = jwtConfig.getRefreshTokenExpiration();
    }

    /**
     * Access Token 생성
     */
    public String createAccessToken(Long userId) {
        return createToken(userId, accessTokenExpiration);
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, refreshTokenExpiration);
    }

    private String createToken(Long userId, long expiration) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenExpiration = now.plusSeconds(expiration);

        return Jwts.builder()
                .claims()
                .add("subject", userId)
                .and()
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(tokenExpiration.toInstant()))
                .signWith(key)
                .compact();
    }

    /**
     * Get User ID (subject)
     */
    public Long getSubject(String token) {
        Claims claims = Jwts.parser().verifyWith(this.key).build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("subject", Long.class);
    }

    // 토큰 만료 확인
    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    // Validate Jwt Token
    public Boolean validateToken(String token) {
        try {
            return !isExpired(token); // 만료되지 않은 경우에 false 반환
        } catch (JwtException e) {
            throw new TokenException("유효하지 않은 토큰입니다.", e);
        }
    }
}
