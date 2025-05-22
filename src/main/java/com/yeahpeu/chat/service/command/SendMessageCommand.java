package com.yeahpeu.chat.service.command;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SendMessageCommand {
    private Long roomId;
    private SenderCommand sender;
    private String message;
    private ZonedDateTime sentAt;
    private List<MessageContentCommand> contentCommands;
}
