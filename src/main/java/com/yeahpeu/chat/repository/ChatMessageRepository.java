package com.yeahpeu.chat.repository;

import com.yeahpeu.chat.domain.ChatRoomMessageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatRoomMessageEntity, Long> {

    @Query("SELECT m FROM ChatRoomMessageEntity m " +
            "LEFT JOIN FETCH m.sender s " +
            "LEFT JOIN FETCH m.messageContent c " +
            "WHERE m.roomId = :roomId " +
            "ORDER BY m.messageId")
    List<ChatRoomMessageEntity> findMessagesByRoomId(@Param("roomId") Long roomId);

}
