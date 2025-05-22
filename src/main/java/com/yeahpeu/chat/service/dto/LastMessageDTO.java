package com.yeahpeu.chat.service.dto;

import com.yeahpeu.chat.domain.ChatRoomMessageEntity;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LastMessageDTO {
    private ZonedDateTime sentAt;
    private String text;

    public static LastMessageDTO from(ChatRoomMessageEntity entity) {
        if (entity == null) {
            return null;
        }
        return new LastMessageDTO(entity.getSentAt(), entity.getText());
    }
}
