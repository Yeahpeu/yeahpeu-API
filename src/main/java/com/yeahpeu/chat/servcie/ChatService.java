package com.yeahpeu.chat.servcie;

import com.yeahpeu.chat.servcie.command.CreateRoomCommand;
import com.yeahpeu.chat.servcie.command.JoinChatRoomCommand;
import com.yeahpeu.chat.servcie.command.LeaveChatRoomCommand;
import com.yeahpeu.chat.servcie.dto.ChatRoomDTO;
import com.yeahpeu.chat.servcie.dto.ChatRoomDetailDTO;
import com.yeahpeu.chat.servcie.dto.ChatRoomMessageDTO;
import com.yeahpeu.user.service.dto.UserDTO;

import java.util.List;

public interface ChatService {
    /**
     * 채팅방을 생성합니다.
     * @param command
     * @return
     */
    ChatRoomDTO createRoom(CreateRoomCommand command);

    /**
     * 채팅방을 나갑니다.
     * @param command
     */
    void leaveRoom(LeaveChatRoomCommand command);

    /**
     * 채팅방에 참여합니다.
     */
    ChatRoomDTO joinRoom(JoinChatRoomCommand command);

    /**
     * 사용자의 채팅방 리스트를 가장 최신 메시지가 전송된 순서대로 조회합니다.
     */
    List<ChatRoomDetailDTO> getChatRooms(Long userId);

    /**
     * 채팅방을 id로 조회합니다.
     */
    ChatRoomDTO getChatRoom(Long roomId);

    /**
     * 채팅방 유저들을 chatRoomId로 조회합니다.
     */
    List<UserDTO> getChatRoomUsers(Long roomId);

    /**
     * 채팅방의 메시지를 조회합니다.
     * @param roomId
     */
    List<ChatRoomMessageDTO> getChatMessages(Long roomId);

    /**
     *  마지막으로 읽은 메시지를 업데이트합니다.
     */
    void updateLastSeenMessage(Long userId, Long roomId, Long messageId);
}
