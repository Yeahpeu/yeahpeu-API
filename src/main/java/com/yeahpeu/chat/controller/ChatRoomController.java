package com.yeahpeu.chat.controller;

import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.chat.controller.request.CreateChatRoomRequest;
import com.yeahpeu.chat.controller.request.SendMessageRequest;
import com.yeahpeu.chat.controller.response.ChatRoomResponse;
import com.yeahpeu.chat.servcie.ChatMessageService;
import com.yeahpeu.chat.servcie.ChatService;
import com.yeahpeu.chat.servcie.command.*;
import com.yeahpeu.chat.servcie.dto.ChatRoomDTO;
import com.yeahpeu.chat.servcie.dto.ChatRoomMessageDTO;
import com.yeahpeu.common.schema.ListResponse;
import com.yeahpeu.user.controller.response.UserResponse;
import com.yeahpeu.user.service.dto.UserDTO;
import com.yeahpeu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat/rooms")
public class ChatRoomController {

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    @MessageMapping("/chat/rooms/{roomId}")
    public void handleChatMessage(
        @DestinationVariable String roomId,
        @Payload SendMessageRequest payload,
        Principal principal
    ) {
        Long userId = Long.valueOf(principal.getName());
        UserDTO me = userService.getMe(userId);
        ChatRoomMessageDTO newMessage = chatMessageService.sendMessage(
            new SendMessageCommand(
                Long.valueOf(roomId),
                new SenderCommand(userId, me.getName()),
                payload.getMessage(),
                payload.getSentAt(),
                payload.getAttachmentRequests().stream().map(MessageContentCommand::from).toList()
            )
        );

        String topic = "/api/sub/chat/rooms/" + roomId; // 구독 경로 지정
        messagingTemplate.convertAndSend(topic, newMessage);
        System.out.println("메시지 발행됨: " + newMessage);
    }

    @PostMapping
    public ResponseEntity<ChatRoomResponse> createChatRoom(
            @RequestBody CreateChatRoomRequest body,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ChatRoomDTO room = chatService.createRoom(
                CreateRoomCommand.from(body, Long.valueOf(userPrincipal.getUsername()))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ChatRoomResponse.from(room)
        );
    }

    @PostMapping("/{roomId}/messages")
    public ResponseEntity<ChatRoomMessageDTO> sendMessage(
        @PathVariable Long roomId,
        @RequestBody SendMessageRequest body,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        UserDTO me = userService.getMe(Long.valueOf(userPrincipal.getUsername()));
        ChatRoomMessageDTO chatRoomMessageDTO = chatMessageService.sendMessage(
            new SendMessageCommand(
                roomId,
                new SenderCommand(me.getId(), me.getName()),
                body.getMessage(),
                body.getSentAt(),
                body.getAttachmentRequests().stream().map(MessageContentCommand::from).toList()
            )
        );

        return ResponseEntity.ok(chatRoomMessageDTO);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ListResponse<ChatRoomMessageDTO>> getChatRoomMessages(@PathVariable Long roomId) {
        List<ChatRoomMessageDTO> chatRoomMessageDTOS = chatService.getChatMessages(roomId);
        ListResponse<ChatRoomMessageDTO> response = new ListResponse<>(chatRoomMessageDTOS);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{roomId}/users")
    public ResponseEntity<ListResponse<UserResponse>> getChatRoomUsers(
            @PathVariable("roomId") Long roomId
    ) {
        List<UserResponse> userDTOS = chatService.getChatRoomUsers(roomId)
                .stream()
                .map(UserResponse::from)
                .toList();
        ListResponse<UserResponse> response = new ListResponse<>(userDTOS);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<ChatRoomResponse> joinChatRoom(
            @PathVariable("roomId") Long roomId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        //사용자 id와 chatRoomId로 그룹에 참여한다
        ChatRoomDTO chatRoomDTO = chatService.joinRoom(
                new JoinChatRoomCommand(roomId, List.of(Long.valueOf(userPrincipal.getUsername()))
                )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ChatRoomResponse.from(chatRoomDTO)
        );
    }

    @DeleteMapping("/{roomId}/leave")
    public ResponseEntity<Void> leaveChatRoom(
            @PathVariable("roomId") Long roomId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        chatService.leaveRoom(new LeaveChatRoomCommand(
                roomId,
                Long.valueOf(userPrincipal.getUsername())));
        return ResponseEntity.ok().build();
    }
}
