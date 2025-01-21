package com.yeahpeu.chat.servcie.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class JoinChatRoomCommand {
    private final Long roomId;
    private final List<Long> userIds;
}
