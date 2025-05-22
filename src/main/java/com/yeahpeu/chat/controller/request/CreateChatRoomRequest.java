package com.yeahpeu.chat.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class CreateChatRoomRequest {
    @NotNull
    private String title;

    @NotNull
    @Min(2)
    private int reservedMemberCount;

    private String imageUrl;
//    private List<Long> userIds;
}
