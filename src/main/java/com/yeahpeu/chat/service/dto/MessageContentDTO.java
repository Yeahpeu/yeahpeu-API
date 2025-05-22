package com.yeahpeu.chat.service.dto;

import com.yeahpeu.chat.domain.MessageContentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageContentDTO {
    private String url;
    private String contentType;

    public static MessageContentDTO from(MessageContentEntity entity) {
        return new MessageContentDTO(
                entity.getUrl(),
                entity.getContentType()
        );
    }
}
