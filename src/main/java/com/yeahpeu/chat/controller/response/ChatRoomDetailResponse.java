package com.yeahpeu.chat.controller.response;

import com.yeahpeu.chat.service.dto.ChatRoomDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDetailResponse {
    private Long id;
    private String title;
    private String imageUrl;
    private Integer reservedMemberCount;
    private Integer usedMemberCount;
    private Long unseenMessageCount;
    private LastMessageResponse lastMessage;

    public static ChatRoomDetailResponse from(ChatRoomDetailDTO dto) {
        if (dto == null) {
            return null;
        }
        return new ChatRoomDetailResponse(
                dto.getId(),
                dto.getTitle(),
                dto.getImageUrl(),
                dto.getReservedMemberCount(),
                dto.getUsedMemberCount(),
                dto.getUnseenMessageCount(),
                LastMessageResponse.from(dto.getLastMessageDTO())
        );
    }

}
