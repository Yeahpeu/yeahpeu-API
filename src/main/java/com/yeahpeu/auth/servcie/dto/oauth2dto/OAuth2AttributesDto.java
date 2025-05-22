package com.yeahpeu.auth.servcie.dto.oauth2dto;

import com.yeahpeu.user.entity.RoleType;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.util.AuthCodeUtil;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/*
 * 구글 소셜로그인 인증 후 OAuthAttributes dto에 셋팅해서 CustomOAuth2UserService에서 dto를 넘겨줄 것임
 * */
@Getter
public class OAuth2AttributesDto {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuth2AttributesDto(Map<String, Object> attributes, String nameAttributeKey, String name, String email,
                               String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    // OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 변환
    public static OAuth2AttributesDto of(String registrationId, String userNameAttributeName,
                                         Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    //구글 인증
    private static OAuth2AttributesDto ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2AttributesDto.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    //User 엔티티 생성, OAuthAttributes에서 엔티티를 생성하는 시점은 처음 가입할 때임
    //가입할 때의 기본 권한을 USER로 주기 위해서 role 빌더값에 Role.USER를 사용
    //OAuthAttributes 클래스 생성이 끝났으면 같은 패키지에 SesstionUser 클래스를 생성함
    public UserEntity toEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setProvider(this.nameAttributeKey);
        userEntity.setName(this.name);
        userEntity.setAvatarUrl(this.picture);
        userEntity.setNickname(this.name);
        userEntity.setMyCode(AuthCodeUtil.generateCode());
        userEntity.setRole(RoleType.USER);

        return userEntity;

    }
}
