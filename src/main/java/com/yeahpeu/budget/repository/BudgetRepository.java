package com.yeahpeu.budget.repository;

import com.yeahpeu.budget.domain.BudgetEntity;
import com.yeahpeu.wedding.domain.WeddingEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {
    void deleteByWedding(WeddingEntity wedding);

    @Query("""
            SELECT b FROM BudgetEntity b
            WHERE b.wedding.id = :weddingId
            """)
    Optional<BudgetEntity> findByWeddingId(@Param("weddingId") Long weddingId);
}
