package com.yeahpeu.chat.servcie.command;

import com.yeahpeu.chat.controller.request.CreateChatRoomRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CreateRoomCommand {
    private String title;
    private List<Long> userIds;
    private Long creatorId;

    public static CreateRoomCommand from(CreateChatRoomRequest request, Long creator) {
        return CreateRoomCommand.builder()
                .title(request.getTitle())
                .userIds(request.getUserIds())
                .creatorId(creator)
                .build();
    }
}
