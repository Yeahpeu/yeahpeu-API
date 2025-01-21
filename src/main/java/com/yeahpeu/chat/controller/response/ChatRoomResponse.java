package com.yeahpeu.chat.controller.response;

import com.yeahpeu.chat.servcie.dto.ChatRoomDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatRoomResponse {
    private Long id;
    private String title;

    public static ChatRoomResponse from(ChatRoomDTO dto) {
        return new ChatRoomResponse(dto.getId(), dto.getTitle());
    }
}
