package com.yeahpeu.chat.servcie.dto;

import com.yeahpeu.chat.domain.ChatRoomMessageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class LastMessageDTO {
    private ZonedDateTime sentAt;
    private String text;

    public static LastMessageDTO from(ChatRoomMessageEntity entity) {
        if (entity == null) return null;
        return new LastMessageDTO( entity.getSentAt(), entity.getText());
    }
}
