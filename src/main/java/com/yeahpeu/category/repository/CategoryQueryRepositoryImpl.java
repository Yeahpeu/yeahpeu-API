package com.yeahpeu.category.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeahpeu.category.domain.CategoryEntity;
import com.yeahpeu.category.domain.QCategoryEntity;
import com.yeahpeu.category.domain.QWeddingCategoryEntity;
import com.yeahpeu.category.service.dto.CategoryDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryQueryRepositoryImpl implements CategoryQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QCategoryEntity category = QCategoryEntity.categoryEntity;
    private final QWeddingCategoryEntity weddingCategory = QWeddingCategoryEntity.weddingCategoryEntity;

    @Override
    public List<CategoryDTO> findAllCategories() {
        List<CategoryEntity> categoryEntities = queryFactory
                .selectFrom(category)
                .fetch();

        return CategoryDTO.fromEntities(categoryEntities);
    }

    /**
     * 특정 카테고리 + 하위 카테고리 조회
     */
    @Override
    public List<CategoryDTO> findCategoryWithChildren(Long categoryId) {
        List<CategoryEntity> categoryEntities = queryFactory
                .selectFrom(category)
                .leftJoin(category.children).fetchJoin()
                .where(category.id.eq(categoryId)
                        .or(category.parentCategory.id.eq(categoryId)))
                .distinct()
                .fetch();

        return CategoryDTO.fromEntities(categoryEntities);
    }

    @Override
    public List<CategoryEntity> findSubCategoriesByWeddingId(Long weddingId) {
        return queryFactory
                .select(category)
                .from(category)
                .join(weddingCategory).on(weddingCategory.category.eq(category))
                .where(weddingCategory.wedding.id.eq(weddingId))
                .fetch();
    }

    @Override
    public List<CategoryEntity> findMainCategories() {
        return queryFactory
                .selectFrom(category)
                .where(category.parentCategory.isNull())
                .fetch();
    }
}
