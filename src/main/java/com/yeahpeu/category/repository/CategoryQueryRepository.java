package com.yeahpeu.category.repository;

import com.yeahpeu.category.domain.CategoryEntity;
import com.yeahpeu.category.service.dto.CategoryDTO;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface CategoryQueryRepository {
    List<CategoryDTO> findAllCategories();

    List<CategoryDTO> findCategoryWithChildren(Long categoryId);

    @Query("""
                SELECT wc.category
                FROM WeddingCategoryEntity wc
                WHERE wc.wedding.id = :weddingId
                ORDER BY wc.category.id ASC
            """)
    List<CategoryEntity> findSubCategoriesByWeddingId(Long weddingId);


    List<CategoryEntity> findMainCategories();
}
