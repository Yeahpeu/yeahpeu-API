package com.yeahpeu.chat.servcie.dto;

import com.yeahpeu.chat.domain.ChatRoomMessageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

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

    public static ChatRoomMessageDTO from (ChatRoomMessageEntity entity) {
        return new ChatRoomMessageDTO(
                entity.getMessageId(),
                entity.getRoomId(),
                new SenderDTO(entity.getSender().getId(), entity.getSender().getNickname()),
                entity.getText(),
                entity.getSentAt(),
                entity.getMessageContent()
                        .stream()
                        .map(MessageContentDTO::from)
                        .toList()
        );
    }
}
