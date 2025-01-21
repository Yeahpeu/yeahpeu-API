package com.yeahpeu.chat.repository;

import com.yeahpeu.chat.domain.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> { }
