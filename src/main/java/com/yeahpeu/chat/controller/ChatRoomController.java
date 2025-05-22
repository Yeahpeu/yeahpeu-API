package com.yeahpeu.chat.controller;

import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.chat.controller.request.CreateChatRoomRequest;
import com.yeahpeu.chat.controller.request.SendMessageRequest;
import com.yeahpeu.chat.controller.response.ChatRoomResponse;
import com.yeahpeu.chat.service.ChatMessageService;
import com.yeahpeu.chat.service.ChatService;
import com.yeahpeu.chat.service.command.CreateRoomCommand;
import com.yeahpeu.chat.service.command.JoinChatRoomCommand;
import com.yeahpeu.chat.service.command.LeaveChatRoomCommand;
import com.yeahpeu.chat.service.command.MessageContentCommand;
import com.yeahpeu.chat.service.command.SendMessageCommand;
import com.yeahpeu.chat.service.command.SenderCommand;
import com.yeahpeu.chat.service.dto.ChatRoomDTO;
import com.yeahpeu.chat.service.dto.ChatRoomMessageDTO;
import com.yeahpeu.common.schema.ListResponse;
import com.yeahpeu.user.controller.response.UserResponse;
import com.yeahpeu.user.service.UserService;
import com.yeahpeu.user.service.dto.UserDTO;
import java.security.Principal;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Chat Room Controller",
        description = "1. 채팅방 조회, 생성, 참가.\n 2. 메시지 전송 및 확인"
)
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
                        new SenderCommand(userId, me.getNickname()),
                        payload.getMessage(),
                        payload.getSentAt(),
                        payload.getAttachmentRequests().stream().map(MessageContentCommand::from).toList()
                )
        );

        String topic = "/api/sub/chat/rooms/" + roomId; // 구독 경로 지정
        messagingTemplate.convertAndSend(topic, newMessage);
        System.out.println("메시지 발행됨: " + newMessage);
    }

    @Operation(
            summary = "채팅방 생성",
            description = "새로운 채팅방을 개설합니다. "
    )
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


    @Operation(
            summary = "전체 채팅방 조회",
            description = "전체 채팅방을 조회합니다. "
    )
    @GetMapping
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms(
    ) {
        List<ChatRoomResponse> responses = chatService.getChatRooms().stream()
                .map(ChatRoomResponse::from)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }


    @Operation(
            summary = "메시지 전송",
            description = "채팅방으로 메시지를 전송합니다. "
    )
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
                        new SenderCommand(me.getId(), me.getNickname()),
                        body.getMessage(),
                        body.getSentAt(),
                        body.getAttachmentRequests().stream().map(MessageContentCommand::from).toList()
                )
        );

        return ResponseEntity.ok(chatRoomMessageDTO);
    }


    @Operation(
            summary = "채팅방의 전체 메시지 전송",
            description = "채팅방 내부 모든 메시지를 확인합니다."
    )
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ListResponse<ChatRoomMessageDTO>> getChatRoomMessages(@PathVariable Long roomId) {
        List<ChatRoomMessageDTO> chatRoomMessageDTOS = chatService.getChatMessages(roomId);
        ListResponse<ChatRoomMessageDTO> response = new ListResponse<>(chatRoomMessageDTOS);

        return ResponseEntity.ok().body(response);
    }


    @Operation(
            summary = "채팅방 유저",
            description = "채팅방의 유저정보를 확인합니다."
    )
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


    @Operation(
            summary = "채팅방 참가",
            description = "새로운 채팅방에 참가합니다"
    )
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


    @Operation(
            summary = "채팅방 탈퇴",
            description = "채팅방에서 이탈합니다."
    )
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
