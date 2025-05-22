package com.yeahpeu.auth.servcie.dto.oauth2dto;

import com.yeahpeu.user.entity.RoleType;
import com.yeahpeu.user.entity.UserEntity;

import java.util.Map;

public class GoogleDto implements OAuth2Dto {
    private final Map<String, Object> attribute;

    public GoogleDto(Map<String, Object> attribute) {
        this.attribute = attribute;
    }
    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    @Override
    public String getPicture() {
        return attribute.get("picture").toString();
    }

    public static UserEntity toUserEntity(OAuth2Dto oAuth2Dto){
        UserEntity userEntity = new UserEntity();
        userEntity.setEmailAddress(oAuth2Dto.getEmail());
        userEntity.setNickname(oAuth2Dto.getName());
        userEntity.setName(oAuth2Dto.getName());
        userEntity.setRole(RoleType.USER);
        userEntity.setProvider(oAuth2Dto.getProvider());

        return userEntity;
    }
}