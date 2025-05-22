package com.yeahpeu.chat.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomDetailDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private Integer reservedMemberCount;
    private Integer usedMemberCount;
    private Long unseenMessageCount;
    private LastMessageDTO lastMessageDTO;
}
