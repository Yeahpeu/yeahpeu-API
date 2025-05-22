package com.yeahpeu.chat.service.command;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinChatRoomCommand {
    private final Long roomId;
    private final List<Long> userIds;
}
