package com.yeahpeu.wedding.repository;

import com.yeahpeu.wedding.domain.WeddingEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WeddingRepository extends JpaRepository<WeddingEntity, Long> {

    @Query("""
            SELECT w FROM WeddingEntity w
            LEFT JOIN w.bride b
            LEFT JOIN w.groom g
            WHERE w.id = :weddingId
            """)
    Optional<WeddingEntity> getWeddingWithCouple(Long weddingId);
}
