package com.yeahpeu.user.service.dto;

import com.yeahpeu.chat.domain.ChatRoomUserEntity;
import com.yeahpeu.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String emailAddress;
    private String name;
    private String nickname;
    private String avatarUrl;

    static public UserDTO fromEntity(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .emailAddress(userEntity.getEmailAddress())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .avatarUrl(userEntity.getAvatarUrl())
                .build();
    }

    static public UserDTO from(ChatRoomUserEntity chatRoomUserEntity) {
        return UserDTO.builder()
                .id(chatRoomUserEntity.getUser().getId())
                .emailAddress(chatRoomUserEntity.getUser().getEmailAddress())
                .nickname(chatRoomUserEntity.getUser().getNickname())
                .avatarUrl(chatRoomUserEntity.getUser().getAvatarUrl())
                .build();
    }
}
