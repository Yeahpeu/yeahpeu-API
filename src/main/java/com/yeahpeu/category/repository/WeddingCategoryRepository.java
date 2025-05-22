package com.yeahpeu.category.repository;

import com.yeahpeu.category.domain.WeddingCategoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeddingCategoryRepository extends JpaRepository<WeddingCategoryEntity, Long> {


    @Query("""
            SELECT wc FROM WeddingCategoryEntity wc
            JOIN FETCH wc.category c
            JOIN FETCH c.parentCategory
            WHERE wc.wedding.id = :weddingId
            """)
    List<WeddingCategoryEntity> findByWeddingId(@Param("weddingId") Long weddingId);


}
