package com.yeahpeu.user.chat.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateLastSeenMessageRequest {
    private Long messageId;
}
