package com.yeahpeu.chat.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class SendMessageRequest {
    private String message;
    private ZonedDateTime sentAt;
    private List<AttachmentRequest> attachmentRequests;
}
