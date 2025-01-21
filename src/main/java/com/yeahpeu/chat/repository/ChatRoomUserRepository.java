package com.yeahpeu.chat.repository;

import com.yeahpeu.chat.domain.ChatRoomUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUserEntity, Long> {

    @Query("""
        SELECT cru FROM ChatRoomUserEntity cru
        JOIN FETCH cru.chatRoom cr
        WHERE cru.user.id = :userId
        AND cru.chatRoom.id = :roomId
    """)
    Optional<ChatRoomUserEntity> findByUserAndChatRoom(@Param("userId") Long userId, @Param("roomId") Long roomId);

    @Query("""
        SELECT cru FROM ChatRoomUserEntity cru
        JOIN FETCH cru.chatRoom cr
        WHERE cru.user.id = :userId
    """)
    List<ChatRoomUserEntity> findByUserId(Long userId);

    @Query("""
        SELECT cru FROM ChatRoomUserEntity cru
        JOIN FETCH cru.user
        WHERE cru.chatRoom.id = :roomId
    """)
    List<ChatRoomUserEntity> findByChatRoomId(Long roomId);

    void deleteByUser_IdAndChatRoom_Id(Long userId, Long roomId);

    @Query("""
        SELECT cru FROM ChatRoomUserEntity cru
        WHERE cru.user.id IN :userIds
        AND cru.chatRoom.id = :roomId
    """)
    List<ChatRoomUserEntity> findByUserIdsAndChatRoomId(List<Long> userIds, Long roomId);
}
