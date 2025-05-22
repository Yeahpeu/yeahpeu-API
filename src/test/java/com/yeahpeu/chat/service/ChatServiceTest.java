package com.yeahpeu.chat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yeahpeu.chat.domain.ChatRoomEntity;
import com.yeahpeu.chat.domain.ChatRoomMessageCounter;
import com.yeahpeu.chat.repository.ChatMessageCounterRepository;
import com.yeahpeu.chat.repository.ChatRoomRepository;
import com.yeahpeu.chat.repository.ChatRoomUserRepository;
import com.yeahpeu.chat.service.command.CreateRoomCommand;
import com.yeahpeu.chat.service.command.JoinChatRoomCommand;
import com.yeahpeu.chat.service.dto.ChatRoomDTO;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.transaction.annotation.Transactional;

class ChatServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatRoomUserRepository chatRoomUserRepository;

    @Mock
    private ChatMessageCounterRepository chatMessageCounterRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private ChatServiceImpl chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void createRoom_Success() {
        // Given
        Long creatorId = 1L;
        CreateRoomCommand command = new CreateRoomCommand("Test Room", 10, "image.png", creatorId);

        UserEntity mockUser = new UserEntity();
        mockUser.setId(creatorId);

        ChatRoomEntity savedChatRoom = new ChatRoomEntity();
        savedChatRoom.setId(100L);
        savedChatRoom.setTitle(command.getTitle());
        savedChatRoom.setReservedMemberCount(command.getReservedMemberCount());
        savedChatRoom.setCreator(mockUser);
        savedChatRoom.setUsedMemberCount(0);  // ✅ 기본값 설정

        // Mocking UserRepository
        when(userRepository.findById(creatorId)).thenReturn(Optional.of(mockUser));
        when(userRepository.findByIdIn(any())).thenReturn(List.of(mockUser));

        // Mocking ChatRoomRepository
        when(chatRoomRepository.save(any(ChatRoomEntity.class))).thenReturn(savedChatRoom);
        when(chatRoomRepository.findById(savedChatRoom.getId())).thenReturn(Optional.of(savedChatRoom));

        // Mocking ChatMessageCounterRepository
        ChatRoomMessageCounter counter = new ChatRoomMessageCounter();
        counter.setRoomId(savedChatRoom.getId());
        counter.setLastMessageId(0L);

        when(chatMessageCounterRepository.save(any(ChatRoomMessageCounter.class))).thenReturn(counter);
        when(chatMessageCounterRepository.findByRoomId(savedChatRoom.getId())).thenReturn(Optional.of(counter));

        // When
        ChatRoomDTO result = chatService.createRoom(command);

        // Then
        assertNotNull(result);
        assertEquals("Test Room", result.getTitle());
        assertEquals(10, result.getReservedMemberCount());
        assertEquals(1, result.getUsedMemberCount());

        // ✅ joinRoom이 올바르게 호출되었는지 검증
        ArgumentCaptor<JoinChatRoomCommand> captor = ArgumentCaptor.forClass(JoinChatRoomCommand.class);
        verify(chatService, times(1)).joinRoom(captor.capture());

        JoinChatRoomCommand capturedCommand = captor.getValue();
        assertEquals(savedChatRoom.getId(), capturedCommand.getRoomId());
        assertEquals(1, capturedCommand.getUserIds().size());
        assertTrue(capturedCommand.getUserIds().contains(creatorId));
    }

}