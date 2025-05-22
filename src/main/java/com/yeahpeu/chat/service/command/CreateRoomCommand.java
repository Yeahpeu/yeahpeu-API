package com.yeahpeu.chat.service.command;

import com.yeahpeu.chat.controller.request.CreateChatRoomRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateRoomCommand {
    private String title;
    private int reservedMemberCount;
    private String imageUrl;
    private Long creatorId;

    public static CreateRoomCommand from(CreateChatRoomRequest request, Long userId) {
        return CreateRoomCommand.builder()
                .title(request.getTitle())
                .reservedMemberCount(request.getReservedMemberCount())
                .imageUrl(request.getImageUrl())
                .creatorId(userId)
                .build();
    }
}
