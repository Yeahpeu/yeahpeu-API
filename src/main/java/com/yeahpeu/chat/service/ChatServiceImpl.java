package com.yeahpeu.chat.service;

import com.yeahpeu.chat.domain.ChatRoomEntity;
import com.yeahpeu.chat.domain.ChatRoomMessageCounter;
import com.yeahpeu.chat.domain.ChatRoomUserEntity;
import com.yeahpeu.chat.repository.ChatMessageCounterRepository;
import com.yeahpeu.chat.repository.ChatMessageRepository;
import com.yeahpeu.chat.repository.ChatRoomRepository;
import com.yeahpeu.chat.repository.ChatRoomUserRepository;
import com.yeahpeu.chat.service.command.CreateRoomCommand;
import com.yeahpeu.chat.service.command.JoinChatRoomCommand;
import com.yeahpeu.chat.service.command.LeaveChatRoomCommand;
import com.yeahpeu.chat.service.dto.ChatRoomDTO;
import com.yeahpeu.chat.service.dto.ChatRoomDetailDTO;
import com.yeahpeu.chat.service.dto.ChatRoomMessageDTO;
import com.yeahpeu.chat.service.dto.LastMessageDTO;
import com.yeahpeu.common.exception.BadRequestException;
import com.yeahpeu.common.exception.ExceptionCode;
import com.yeahpeu.common.exception.NotFoundException;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.user.service.dto.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageCounterRepository chatMessageCounterRepository;
    private final ChatMessageRepository chatMessageRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public ChatRoomDTO createRoom(CreateRoomCommand command) {
        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
        chatRoomEntity.setReservedMemberCount(command.getReservedMemberCount());

        chatRoomEntity.setTitle(command.getTitle());
        chatRoomEntity.setUsedMemberCount(0);
        chatRoomEntity.setImageUrl(command.getImageUrl());

        UserEntity creator = userRepository.findById(command.getCreatorId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER_ID));
        chatRoomEntity.setCreator(creator);
        ChatRoomEntity newChatRoom = chatRoomRepository.save(chatRoomEntity);

        // Create Room's MessageCounter
        ChatRoomMessageCounter chatRoomMessageCounter = new ChatRoomMessageCounter();
        chatRoomMessageCounter.setRoomId(newChatRoom.getId());
        chatRoomMessageCounter.setLastMessageId(0L);
        chatMessageCounterRepository.save(chatRoomMessageCounter);

        List<Long> userIds = List.of(command.getCreatorId());

        joinRoom(new JoinChatRoomCommand(newChatRoom.getId(), userIds));

        return new ChatRoomDTO(newChatRoom.getId(), newChatRoom.getTitle(), newChatRoom.getImageUrl(),
                newChatRoom.getReservedMemberCount(), newChatRoom.getUsedMemberCount());
    }

    @Override
    public List<ChatRoomDTO> getChatRooms() {
        List<ChatRoomDTO> DTOs = chatRoomRepository.findAll().stream()
                .map(ChatRoomDTO::from)
                .toList();
        return DTOs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDetailDTO> getUserChatRooms(Long userId) {

        // 1. 사용자의 채팅방 참여 정보를 가져온다.
        List<ChatRoomUserEntity> chatRoomUserEntities = chatRoomUserRepository.findByUserId(userId);

        // 2. 참여 정보로부터 채팅방 ID를 가져온다.
        List<Long> chatRoomIds = chatRoomUserEntities.stream()
                .map(ChatRoomUserEntity::getChatRoom)
                .map(ChatRoomEntity::getId)
                .toList();

        // 3. 채팅방 ID로 채팅방 메시지 카운터를 가져온다.
        List<ChatRoomMessageCounter> chatMessageCounts = chatMessageCounterRepository.findAllByRoomIds(chatRoomIds);

        // 4. Map<채팅방 ID, ChatRoomMessageCounter>
        Map<Long, ChatRoomMessageCounter> chatMessageCountMap = new HashMap<>();
        for (ChatRoomMessageCounter chatMessageCount : chatMessageCounts) {
            chatMessageCountMap.put(chatMessageCount.getRoomId(), chatMessageCount);
        }

        // 5. 각 채팅방 별로 unseenMessageCount를 계산한다.
        List<ChatRoomDetailDTO> chatRoomDetailDTOS = new ArrayList<>();

        for (ChatRoomUserEntity chatRoomUserEntity : chatRoomUserEntities) {
            ChatRoomEntity chatRoomEntity = chatRoomUserEntity.getChatRoom();
            ChatRoomMessageCounter chatRoomMessageCounter = chatMessageCountMap.get(chatRoomEntity.getId());
            Long unseenMessageCount =
                    chatRoomMessageCounter.getLastMessageId() - chatRoomUserEntity.getLastReadMessageId();
            chatRoomDetailDTOS.add(new ChatRoomDetailDTO(
                    chatRoomEntity.getId(),
                    chatRoomEntity.getTitle(),
                    chatRoomEntity.getImageUrl(),
                    chatRoomEntity.getReservedMemberCount(),
                    chatRoomEntity.getUsedMemberCount(),
                    unseenMessageCount,
                    LastMessageDTO.from(chatRoomMessageCounter.getLastMessage())
            ));
        }

        // 6. 채팅방의 메시지가 먼저 보내진 순서대로 정렬
        chatRoomDetailDTOS.sort(
                Comparator
                        .comparing(ChatRoomDetailDTO::getLastMessageDTO, Comparator.nullsFirst(
                                Comparator.comparing(LastMessageDTO::getSentAt)
                        ))
                        .reversed()
        );

        return chatRoomDetailDTOS;
    }

    @Override
    @Transactional
    public void leaveRoom(LeaveChatRoomCommand command) {
        // 1️. 채팅방 조회 (없으면 예외)
        ChatRoomEntity chatRoom = chatRoomRepository.findById(command.getRoomId())
                .orElseThrow(() -> new NotFoundException("채팅룸이 존재하지 않습니다. 다시 확인해 주세요"));

        // 2️. 유저가 채팅방에 있는지 확인
        boolean userExists = chatRoomUserRepository.existsByUser_IdAndChatRoom_Id(command.getUserId(),
                command.getRoomId());
        if (!userExists) {
            throw new BadRequestException("해당 유저는 채팅방에 존재하지 않습니다. 다시 확인해주세요");
        }

        // 3️. 유저를 채팅방에서 제거
        chatRoomUserRepository.deleteByUser_IdAndChatRoom_Id(command.getUserId(), command.getRoomId());

        // 4️. 멤버 수 감소 - DB에서 업데이트
        chatRoomRepository.decreaseUsedMemberCount(chatRoom.getId());

        entityManager.refresh(chatRoom);

        // 5️. 채팅방이 비었으면 관련 데이터 삭제 후 채팅방 삭제
        if (chatRoom.getUsedMemberCount() == 0) {
            chatRoomRepository.delete(chatRoom); // 채팅방 삭제
        }
    }


    @Transactional
    @Override
    public ChatRoomDTO joinRoom(JoinChatRoomCommand command) {

        // 1. UserEntity 검색
        List<UserEntity> users = userRepository.findByIdIn(command.getUserIds());
        // 선택한 유저가 없는 경우 Exception
        if (users.size() != command.getUserIds().size()) {
            throw new NotFoundException("User not found");
        }

        // 2. ChatRoomEntity 검색
        ChatRoomEntity chatRoom = chatRoomRepository.findById(command.getRoomId())
                .orElseThrow(() -> new NotFoundException("ChatRoom not found"));

        // 3. 이미 참여한 사용자
        List<ChatRoomUserEntity> existingChatRoomUsers = chatRoomUserRepository.findByUserIdsAndChatRoomId(
                command.getUserIds(), command.getRoomId());

        Set<Long> existingUserIds = existingChatRoomUsers.stream()
                .map(entity -> entity.getUser().getId())
                .collect(Collectors.toSet());

        // 4. 새롭게 참여하는 사용자
        List<UserEntity> newUsers = users.stream()
                .filter(user -> !existingUserIds.contains(user.getId()))
                .toList();

        if (chatRoom.getUsedMemberCount() + newUsers.size() > chatRoom.getReservedMemberCount()) {
            throw new BadRequestException("채팅방이 꽉 찼습니다. 다른 방에 참여해보세요!");
        }

        // get ChatRoomMessageCounter
        ChatRoomMessageCounter chatRoomMessageCounter = chatMessageCounterRepository.findByRoomId(command.getRoomId())
                .orElseThrow(() -> new NotFoundException("ChatRoomMessageCounter not found"));

        if (!newUsers.isEmpty()) {

            List<ChatRoomUserEntity> newChatRoomUsers = newUsers.stream()
                    .map(user -> {
                        ChatRoomUserEntity cru = new ChatRoomUserEntity();
                        cru.setUser(user);
                        cru.setChatRoom(chatRoom);
                        cru.setLastReadMessageId(chatRoomMessageCounter.getLastMessageId());
                        return cru;
                    })
                    .collect(Collectors.toList());

            chatRoomUserRepository.saveAll(newChatRoomUsers);

            chatRoom.setUsedMemberCount(chatRoom.getUsedMemberCount() + newUsers.size());
            chatRoomRepository.save(chatRoom);
        }

        return new ChatRoomDTO(chatRoom.getId(), chatRoom.getTitle(), chatRoom.getImageUrl(),
                chatRoom.getReservedMemberCount(), chatRoom.getUsedMemberCount());
    }

    @Transactional(readOnly = true)
    @Override
    public ChatRoomDTO getChatRoom(Long roomId) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Chatroom not found"));
        return ChatRoomDTO.from(chatRoomEntity);
    }

    @Override
    public List<UserDTO> getChatRoomUsers(Long roomId) {

        return chatRoomUserRepository.findByChatRoomId(roomId)
                .stream()
                .map(UserDTO::from)
                .toList();
    }

    @Override
    public List<ChatRoomMessageDTO> getChatMessages(Long roomId) {

        return chatMessageRepository.findMessagesByRoomId(roomId)
                .stream()
                .map(ChatRoomMessageDTO::from)
                .toList();
    }

    @Override
    public void updateLastSeenMessage(Long userId, Long roomId, Long messageId) {
        ChatRoomUserEntity chatRoomUserEntity = chatRoomUserRepository.findByUserAndChatRoom(userId, roomId)
                .orElseThrow(() -> new NotFoundException("ChatRoomUser not found"));

        if (chatRoomUserEntity.getLastReadMessageId() >= messageId) {
            return;
        }

        chatRoomUserEntity.setLastReadMessageId(messageId);
        chatRoomUserRepository.save(chatRoomUserEntity);
    }
}
