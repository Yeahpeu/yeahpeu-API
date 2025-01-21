package com.yeahpeu.chat.repository;

import com.yeahpeu.chat.domain.ChatRoomMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatRoomMessageEntity, Long> {

    @Query("SELECT m FROM ChatRoomMessageEntity m " +
            "JOIN FETCH m.sender s " +
            "LEFT JOIN FETCH m.messageContent c " +
            "WHERE m.roomId = :roomId " +
            "ORDER BY m.messageId")
    List<ChatRoomMessageEntity> findMessagesByRoomId(@Param("roomId") Long roomId);

}
