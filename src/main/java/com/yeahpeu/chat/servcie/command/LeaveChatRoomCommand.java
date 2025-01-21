package com.yeahpeu.chat.servcie.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaveChatRoomCommand {
    private Long roomId;
    private Long userId;
}
