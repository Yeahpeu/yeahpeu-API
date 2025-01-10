package com.yeahpeu.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2Response oAuth2Response;
    private final String role;

    public CustomOAuth2User(OAuth2Response oAuth2Response, String role) {
        this.oAuth2Response = oAuth2Response;
        this.role = role;

        System.out.println("====================SAVE CLEAR=============");
    }


    @Override
    //로그인을 진행하면 resource server 로부터 넘어오는 모든 데이터
    public Map<String, Object> getAttributes() {
        //todo
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role;
            }
        });
        return collection;
    }




    @Override
    public String getName() {
        return oAuth2Response.getName();
    }

    //오버라이딩메서드 외에, 추가적인 데이터를 발급해주는 코드를 작성할 수 있습니다.
    // 구글로그인을 진행한 사람의 id를 임의로 제작하겠습니다.
    public String getUsername() {
        return oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
    }


}
