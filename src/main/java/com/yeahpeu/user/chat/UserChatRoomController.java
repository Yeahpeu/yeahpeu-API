package com.yeahpeu.user.chat;

import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.chat.controller.response.ChatRoomDetailResponse;
import com.yeahpeu.chat.controller.response.ChatRoomResponse;
import com.yeahpeu.chat.servcie.ChatService;
import com.yeahpeu.common.schema.ListResponse;
import com.yeahpeu.user.chat.request.UpdateLastSeenMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/chat/rooms")
public class UserChatRoomController {

    private final ChatService chatService;

    @MessageMapping("/chat/rooms/{roomId}/read")
    public void updateLastSeenMessage(
            @DestinationVariable("roomId") String roomId,
            @Payload UpdateLastSeenMessageRequest payload,
            Principal principal
    ) {
        Long userId = Long.valueOf(principal.getName());
        chatService.updateLastSeenMessage(userId, Long.valueOf(roomId), payload.getMessageId());
    }

    @GetMapping
    public ResponseEntity<ListResponse<ChatRoomDetailResponse>> getChatRooms(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<ChatRoomDetailResponse> chatRoomDetailRespons = chatService.getChatRooms(Long.valueOf(userPrincipal.getUsername()))
                .stream()
                .map(ChatRoomDetailResponse::from)
                .toList();

        ListResponse<ChatRoomDetailResponse> response = new ListResponse<>(chatRoomDetailRespons);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponse> getChatRoom(
        @PathVariable("roomId") Long roomId
    ) {
        ChatRoomResponse response = ChatRoomResponse.from(chatService.getChatRoom(roomId));

        return ResponseEntity.ok().body(response);
    }

}
