package com.yeahpeu.wishlist.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class NaverAPIUtil {
    @Value("${api.naver.client-id}")
    private String cliientId;

    @Value("${api.naver.client-secret}")
    private String clientSecret;
}
