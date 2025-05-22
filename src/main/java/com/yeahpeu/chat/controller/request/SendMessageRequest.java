package com.yeahpeu.chat.controller.request;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest {
    private String message;
    private ZonedDateTime sentAt;
    private List<AttachmentRequest> attachmentRequests;
}
