package com.yeahpeu.chat.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaveChatRoomCommand {
    private Long roomId;
    private Long userId;
}
