package com.yeahpeu.budget.repository;

import com.yeahpeu.budget.domain.CategoryExpenseEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryExpenseRepository extends JpaRepository<CategoryExpenseEntity, Long> {

    @Query("""
            Select c from CategoryExpenseEntity c
            WHERE c.category.id = :categoryId
            AND c.wedding.id = :weddingId
            """)
    Optional<CategoryExpenseEntity> findByCategoryIdAndWeddingId(Long categoryId, Long weddingId);

    @Query("""
                SELECT ce FROM CategoryExpenseEntity ce
                JOIN FETCH ce.category c
                LEFT JOIN FETCH c.parentCategory
                WHERE ce.wedding.id = :weddingId
            """)
    List<CategoryExpenseEntity> findByWeddingIdWithParentCategory(Long weddingId);

}
