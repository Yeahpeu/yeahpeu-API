package com.yeahpeu.chat.servcie.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SenderCommand {
    private Long userId;
    private String nickname;
}
