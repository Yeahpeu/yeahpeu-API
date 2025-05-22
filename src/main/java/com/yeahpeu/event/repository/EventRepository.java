package com.yeahpeu.event.repository;

import com.yeahpeu.budget.service.dto.SubCategoryBudgetDTO;
import com.yeahpeu.event.domain.EventEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<EventEntity, Long>, EventRepositoryCustom {

    List<EventEntity> findByWeddingIdAndSubCategoryId(Long userId, Long subcategoryId);

    @Query("SELECT COUNT(e) FROM EventEntity e WHERE e.wedding.id = :weddingId")
    int countByWedding_Id(@Param("weddingId") Long weddingId);

    @Query("SELECT COUNT(e) FROM EventEntity e WHERE e.wedding.id = :weddingId AND e.completed = :completed")
    int countByWedding_IdAndCompleted(@Param("weddingId") Long weddingId, @Param("completed") boolean completed);

    @Query("""
                SELECT new com.yeahpeu.budget.service.dto.SubCategoryBudgetDTO(
                    s.id, s.name, m.id, m.name, COALESCE(CAST(SUM(e.price) AS long), 0L) )
                FROM WeddingCategoryEntity wc
                JOIN wc.category s
                JOIN s.parentCategory m
                LEFT JOIN EventEntity e
                    ON e.subCategory = s
                    AND e.wedding.id = :weddingId
                    AND e.completed = :completed
                WHERE wc.wedding.id = :weddingId
                GROUP BY s.id, s.name, m.id, m.name
            """)
    List<SubCategoryBudgetDTO> getCompletedExpensesGroupedBySubCategory(Long weddingId, Boolean completed);


}
