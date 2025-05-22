package com.yeahpeu.user.repository;


import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.wedding.domain.WeddingEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByName(String username);

    Optional<UserEntity> findByName(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserEntity u WHERE u.id = :userId")
    Optional<UserEntity> findUserByIdWithLock(Long userId);

    @Query("""
                SELECT u FROM UserEntity u
                WHERE u.emailAddress = :emailAddress
            """)
    Optional<UserEntity> findByEmailAddress(@Param("emailAddress") String emailAddress);


    @Query("SELECT u FROM UserEntity u WHERE u.id IN :ids")
    List<UserEntity> findByIdIn(List<Long> ids);

    @Query("""
            SELECT u FROM UserEntity u
            LEFT JOIN FETCH u.wedding
            WHERE u.myCode = :myCode
            """)
    Optional<UserEntity> findByMyCodeWithWedding(String myCode);

    @Query("""
            SELECT u FROM UserEntity u
            JOIN FETCH u.wedding
            WHERE u.id = :userId
            """)
    Optional<UserEntity> findUserWithWedding(Long userId);

    @Query("SELECT u.wedding FROM UserEntity u WHERE u.id = :id")
    Optional<WeddingEntity> findWeddingById(Long id);

    Optional<UserEntity> findByMyCode(String myCode);
}
