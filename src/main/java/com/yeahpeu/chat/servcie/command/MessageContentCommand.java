package com.yeahpeu.chat.servcie.command;

import com.yeahpeu.chat.controller.request.AttachmentRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageContentCommand {

    private String url;
    private String contentType;

    public static MessageContentCommand from(AttachmentRequest request) {
        return new MessageContentCommand(
                request.getUrl(),
                request.getContentType()
        );
    }
}
