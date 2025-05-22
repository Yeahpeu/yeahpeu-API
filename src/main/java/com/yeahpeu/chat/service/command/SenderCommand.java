package com.yeahpeu.chat.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SenderCommand {
    private Long userId;
    private String nickname;
}
