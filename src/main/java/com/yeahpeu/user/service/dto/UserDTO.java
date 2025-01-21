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

    static public UserDTO fromEntity(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .emailAddress(userEntity.getEmailAddress())
                .name(userEntity.getNickname())
                .build();
    }

    static public UserDTO from(ChatRoomUserEntity chatRoomUserEntity) {
        return UserDTO.builder()
                .id(chatRoomUserEntity.getUser().getId())
                .emailAddress(chatRoomUserEntity.getUser().getEmailAddress())
                .name(chatRoomUserEntity.getUser().getNickname())
                .build();
    }
}
