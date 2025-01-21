package com.yeahpeu.chat.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class CreateChatRoomRequest {
    private String title;
    private List<Long> userIds;
}
