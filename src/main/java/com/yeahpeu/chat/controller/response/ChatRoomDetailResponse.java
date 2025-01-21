package com.yeahpeu.chat.controller.response;

import com.yeahpeu.chat.servcie.dto.ChatRoomDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDetailResponse {
    private Long id;
    private String title;
    private Long unseenMessageCount;
    private LastMessageResponse lastMessage;

    public static ChatRoomDetailResponse from(ChatRoomDetailDTO dto) {
        if (dto == null) return null;
        return new ChatRoomDetailResponse(
                dto.getId(),
                dto.getTitle(),
                dto.getUnseenMessageCount(),
                LastMessageResponse.from(dto.getLastMessageDTO())
        );
    }

}
