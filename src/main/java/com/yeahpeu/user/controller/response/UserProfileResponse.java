package com.yeahpeu.user.controller.response;

import com.yeahpeu.user.service.dto.UserProfileDTO;
import com.yeahpeu.wedding.domain.WeddingRole;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserProfileResponse {
    private Long id;
    private WeddingRole weddingRole;
    private String username;
    private String avatarUrl;
    private String nickname;
    private String myCode;
    private String emailAddress;
    private WeddingInfoResponse weddingInfoResponse;

    public static UserProfileResponse from(UserProfileDTO dto) {
        return UserProfileResponse.builder()
                .id(dto.getId())
                .weddingRole(dto.getWeddingRole())
                .username(dto.getName())
                .avatarUrl(dto.getAvatarUrl())
                .nickname(dto.getNickname())
                .myCode(dto.getMyCode())
                .emailAddress(dto.getEmailAddress())
                .weddingInfoResponse(WeddingInfoResponse.from(dto.getWeddingInfoDTO()))
                .build();

    }
}
