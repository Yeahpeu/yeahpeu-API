package com.yeahpeu.chat.controller.response;

import com.yeahpeu.chat.servcie.dto.LastMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class LastMessageResponse {
    private ZonedDateTime sentAt;
    private String text;

    public static LastMessageResponse from(LastMessageDTO dto) {
        if (dto == null) return null;
        return new LastMessageResponse(
                dto.getSentAt(),
                dto.getText()
        );
    }
}
