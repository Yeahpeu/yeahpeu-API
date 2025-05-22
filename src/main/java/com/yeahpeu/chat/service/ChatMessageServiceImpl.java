package com.yeahpeu.chat.service;

import com.yeahpeu.chat.domain.ChatRoomEntity;
import com.yeahpeu.chat.domain.ChatRoomMessageCounter;
import com.yeahpeu.chat.domain.ChatRoomMessageEntity;
import com.yeahpeu.chat.domain.ChatRoomUserEntity;
import com.yeahpeu.chat.repository.ChatMessageCounterRepository;
import com.yeahpeu.chat.repository.ChatMessageRepository;
import com.yeahpeu.chat.repository.ChatRoomRepository;
import com.yeahpeu.chat.repository.ChatRoomUserRepository;
import com.yeahpeu.chat.service.command.SendMessageCommand;
import com.yeahpeu.chat.service.dto.ChatRoomMessageDTO;
import com.yeahpeu.common.exception.BadRequestException;
import com.yeahpeu.common.exception.NotFoundException;
import com.yeahpeu.user.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageCounterRepository chatMessageCounterRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final EntityManager entityManager;

    @Transactional
    @Override
    public ChatRoomMessageDTO sendMessage(SendMessageCommand command) {

        // 1. Room 조회
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(command.getRoomId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채팅방입니다."));

        ChatRoomUserEntity chatRoomUserEntity = chatRoomUserRepository.findByUserAndChatRoom(
                        command.getSender().getUserId(),
                        chatRoomEntity.getId()
                )
                .orElseThrow(() -> new BadRequestException("메시지 발송 실패"));

        ChatRoomMessageCounter chatRoomMessageCounter = chatMessageCounterRepository.findByRoomIdWithLock(
                        command.getRoomId())
                .orElseThrow(() -> new NotFoundException("채팅방 카운터가 존재하지 않습니다."));

        // user 조회 없이 id만으로 객체 생성
        UserEntity senderProxy = entityManager.getReference(UserEntity.class, command.getSender().getUserId());

        // 2. Message 생성
        ChatRoomMessageEntity newMessage = new ChatRoomMessageEntity();
        Long nextMessageId = chatRoomMessageCounter.getNextMessageId();
        newMessage.setMessageId(nextMessageId);
        newMessage.setRoomId(chatRoomEntity.getId());
        newMessage.setText(command.getMessage());
        newMessage.setSentAt(ZonedDateTime.now(ZoneOffset.UTC));

        newMessage.setSender(senderProxy);

        newMessage.setMessageContentFromCommands(
                command.getContentCommands()
        );
        ChatRoomMessageEntity save = chatMessageRepository.save(newMessage);

        // 3. Message Counter 업데이트
        chatRoomMessageCounter.setLastMessageId(nextMessageId);
        chatRoomUserEntity.setLastReadMessageId(nextMessageId); // 본인이 보낸 메시지는 읽음 처리

        return ChatRoomMessageDTO.from(save);
    }
}
