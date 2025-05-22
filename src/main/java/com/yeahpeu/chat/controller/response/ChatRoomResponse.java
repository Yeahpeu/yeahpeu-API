package com.yeahpeu.chat.controller.response;

import com.yeahpeu.chat.service.dto.ChatRoomDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatRoomResponse {
    private Long id;
    private String title;
    private String imageUrl;
    private Integer reservedMemberCount;
    private Integer usedMemberCount;

    public static ChatRoomResponse from(ChatRoomDTO dto) {
        return new ChatRoomResponse(
                dto.getId(),
                dto.getTitle(),
                dto.getImageUrl(),
                dto.getReservedMemberCount(),
                dto.getUsedMemberCount()
        );
    }
}
