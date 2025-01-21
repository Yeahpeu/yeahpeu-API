package com.yeahpeu.chat.servcie.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomDetailDTO {
    private Long id;
    private String title;
    private Long unseenMessageCount;
    private LastMessageDTO lastMessageDTO;
}
