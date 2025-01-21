package com.yeahpeu.chat.servcie.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

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
