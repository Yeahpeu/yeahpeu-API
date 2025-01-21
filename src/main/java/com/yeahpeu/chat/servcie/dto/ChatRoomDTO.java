package com.yeahpeu.chat.servcie.dto;

import com.yeahpeu.chat.domain.ChatRoomEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatRoomDTO {
    private Long id;
    private String title;

    public static ChatRoomDTO from(ChatRoomEntity entity){
        return new ChatRoomDTO(
                entity.getId(),
                entity.getTitle()
        );
    }
}

