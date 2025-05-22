package com.yeahpeu.ai.controller.response;

import lombok.Data;

@Data
public class PromptEventResponse {
    private String title;
    private String date;
    private String time;
    private String guide;
    private String reason;
}
