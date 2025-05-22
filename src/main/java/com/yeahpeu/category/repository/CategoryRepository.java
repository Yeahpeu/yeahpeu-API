package com.yeahpeu.category.repository;

import com.yeahpeu.category.domain.CategoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>, CategoryQueryRepository {
    @Query("""
                SELECT c FROM CategoryEntity c
                JOIN FETCH c.parentCategory p
                WHERE c.parentCategory IS NOT NULL
            """)
    List<CategoryEntity> findAllSubCategoriesWithParent();


}
