package com.yeahpeu.chat.service.dto;

import com.yeahpeu.chat.domain.ChatRoomEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatRoomDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private Integer reservedMemberCount;
    private Integer usedMemberCount;

    public static ChatRoomDTO from(ChatRoomEntity entity) {
        return new ChatRoomDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getImageUrl(),
                entity.getReservedMemberCount(),
                entity.getUsedMemberCount()
        );
    }
}

