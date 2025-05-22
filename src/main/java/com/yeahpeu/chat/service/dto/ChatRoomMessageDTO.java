package com.yeahpeu.chat.service.dto;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ChatRoomMessageDTO {
    private Long id;
    private Long roomId;
    private SenderDTO sender;
    private String message;
    private ZonedDateTime sentAt;
    private List<MessageContentDTO> attachments;

    public static ChatRoomMessageDTO from(com.yeahpeu.chat.domain.ChatRoomMessageEntity entity) {
        return new ChatRoomMessageDTO(
                entity.getMessageId(),
                entity.getRoomId(),
                getSender(entity),
                entity.getText(),
                entity.getSentAt(),
                entity.getMessageContent()
                        .stream()
                        .map(MessageContentDTO::from)
                        .toList()
        );
    }

    private static SenderDTO getSender(com.yeahpeu.chat.domain.ChatRoomMessageEntity entity) {
        if (entity.getSender() == null) {
            return new SenderDTO(null, null);
        }

        return new SenderDTO(entity.getSender().getId(), entity.getSender().getNickname());
    }
}
