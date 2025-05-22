package com.yeahpeu.chat.repository;

import com.yeahpeu.chat.domain.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE ChatRoomEntity c SET c.usedMemberCount = c.usedMemberCount - 1 WHERE c.id = :chatRoomId")
    int decreaseUsedMemberCount(Long chatRoomId);
}
