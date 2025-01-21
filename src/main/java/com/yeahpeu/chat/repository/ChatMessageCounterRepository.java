package com.yeahpeu.chat.repository;

import com.yeahpeu.chat.domain.ChatRoomMessageCounter;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageCounterRepository extends JpaRepository<ChatRoomMessageCounter, Long> {

    /**
     * LockModeType.PESSIMISTIC_WRITE
     *  다른 트랜잭션에서 읽기, 쓰기를 하지 못하도록 배타락을 걸어둡니다.
     *  락이 걸리는 대상은
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "1000")})
    @Query("SELECT c FROM ChatRoomMessageCounter c WHERE c.roomId = :roomId")
    Optional<ChatRoomMessageCounter> findByRoomIdWithLock(Long roomId);

    /**
     * 락 없이 ChatRoomMessageCounter 단순 조회
     */
    Optional<ChatRoomMessageCounter> findByRoomId(Long roomId);

    @Query("""
            SELECT ccm FROM ChatRoomMessageCounter ccm
            LEFT JOIN FETCH ccm.lastMessage
            WHERE ccm.roomId IN :roomIds
            """)
    List<ChatRoomMessageCounter> findAllByRoomIds(List<Long> roomIds);
}
