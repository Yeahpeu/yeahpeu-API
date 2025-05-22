package com.yeahpeu.user.service.dto;

import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.wedding.domain.WeddingRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileDTO {
    private Long id;
    private WeddingRole weddingRole;
    private String name;
    private String avatarUrl;
    private String nickname;
    private String myCode;
    private String emailAddress;
    private WeddingInfoDTO weddingInfoDTO;

    public static UserProfileDTO from(UserEntity user) {
        return UserProfileDTO.builder()
                .id(user.getId())
                .weddingRole(user.getWeddingRole())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .nickname(user.getNickname())
                .myCode(user.getMyCode())
                .emailAddress(user.getEmailAddress())
                .build();
    }
}
