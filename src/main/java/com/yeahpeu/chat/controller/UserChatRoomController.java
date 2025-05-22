package com.yeahpeu.chat.controller;

import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.chat.controller.request.UpdateLastSeenMessageRequest;
import com.yeahpeu.chat.controller.response.ChatRoomDetailResponse;
import com.yeahpeu.chat.controller.response.ChatRoomResponse;
import com.yeahpeu.chat.service.ChatService;
import com.yeahpeu.common.schema.ListResponse;
import java.security.Principal;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
        name = "User Chat Room Controller",
        description = "유저별 채팅방정보 관리 \n 1. 채팅방 별 안읽은 메시지 처리\n2. 사용자가 참가한 채팅방 조회"
)
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


    @Operation(
            summary = "참가한 채팅방 전체 조회",
            description = "참가상태인 채팅방을 전체 조회합니다"
    )
    @GetMapping
    public ResponseEntity<ListResponse<ChatRoomDetailResponse>> getChatRooms(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<ChatRoomDetailResponse> chatRoomDetailRespons = chatService.getUserChatRooms(
                        Long.valueOf(userPrincipal.getUsername()))
                .stream()
                .map(ChatRoomDetailResponse::from)
                .toList();

        ListResponse<ChatRoomDetailResponse> response = new ListResponse<>(chatRoomDetailRespons);
        return ResponseEntity.ok().body(response);
    }

    @Operation(
            summary = "채팅방 조회",
            description = "입장할 채팅방의 정보를 조회합니다"
    )
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponse> getChatRoom(
            @PathVariable("roomId") Long roomId
    ) {
        ChatRoomResponse response = ChatRoomResponse.from(chatService.getChatRoom(roomId));

        return ResponseEntity.ok().body(response);
    }

}
