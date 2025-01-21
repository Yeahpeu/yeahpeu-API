package com.yeahpeu.chat.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentRequest {
    private String url;
    private String contentType;
}
