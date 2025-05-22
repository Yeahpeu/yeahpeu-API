package com.yeahpeu.auth.servcie.dto.oauth2dto;

import com.yeahpeu.user.entity.UserEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final UserEntity user;

    public CustomOAuth2User(UserEntity user) {
        this.user = user;

    }

    @Override
    //로그인을 진행하면 resource server 로부터 넘어오는 모든 데이터
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> "ROLE_USER");
        return authorities;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    //오버라이딩메서드 외에, 추가적인 데이터를 발급해주는 코드를 작성할 수 있습니다.
    // 구글로그인을 진행한 사람의 id를 임의로 제작하겠습니다.
    public String getId() {
        return user.getProvider() + user.getProviderId();
    }

    public String getEmailAddress() {
        return user.getEmailAddress();
    }
    
}
