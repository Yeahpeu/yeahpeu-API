package com.yeahpeu.category.service;

import com.yeahpeu.budget.domain.CategoryExpenseEntity;
import com.yeahpeu.budget.repository.CategoryExpenseRepository;
import com.yeahpeu.category.domain.CategoryEntity;
import com.yeahpeu.category.domain.WeddingCategoryEntity;
import com.yeahpeu.category.repository.CategoryRepository;
import com.yeahpeu.category.repository.WeddingCategoryRepository;
import com.yeahpeu.category.service.dto.CategoryDTO;
import com.yeahpeu.common.exception.ExceptionCode;
import com.yeahpeu.common.exception.NotFoundException;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.wedding.domain.WeddingEntity;
import com.yeahpeu.wedding.repository.WeddingRepository;
import com.yeahpeu.wedding.service.command.WeddingCategorySaveCommand;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryExpenseRepository categoryExpenseRepository;
    private final WeddingCategoryRepository weddingCategoryRepository;
    private final WeddingRepository weddingRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO> findAllCategories() {
        // 전체 카테고리 조회
        return categoryRepository.findAllCategories();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO> findCategory(Long categoryId) {
        // 특정 카테고리의 하위 카테고리까지 함께 조회
        return categoryRepository.findCategoryWithChildren(categoryId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO> findWeddingCategories(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER_ID));

        WeddingEntity wedding = user.getWedding();

        if (wedding == null || wedding.getId() == null) {
            throw new NotFoundException(ExceptionCode.NOT_FOUND_WEDDING_ID);
        }

        // 1. 서브 카테고리 조회
        List<CategoryEntity> subCategories = categoryRepository.findSubCategoriesByWeddingId(wedding.getId());

        // 2. 부모 카테고리 조회
        List<CategoryEntity> mainCategories = categoryRepository.findMainCategories();

        // 3. 상위와 하위 카테고리 묶기
        return mapMainAndSubCategories(mainCategories, subCategories);
    }


    @Override
    @Transactional
    public void saveWeddingCategories(WeddingCategorySaveCommand command) {

        // 1. 모든 소주제 + 부모를 한 번의 쿼리로 가져옴 (FETCH JOIN)
        List<CategoryEntity> allSubCategories = categoryRepository.findAllSubCategoriesWithParent();
        Map<Long, CategoryEntity> subCategoryMap = allSubCategories.stream()
                .collect(Collectors.toMap(CategoryEntity::getId, category -> category));

        WeddingEntity wedding = weddingRepository.findById(command.getWeddingId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_WEDDING_ID));

        // 2. 사용자가 선택한 소주제 ID 가져오기
        Set<Long> selectedCategoryIds = new HashSet<>(command.getCategoryIds());

        // 3. 선택한 소주제의 부모 ID 추출
        Set<Long> parentIds = selectedCategoryIds.stream()
                .map(subCategoryMap::get)
                .filter(Objects::nonNull)
                .map(CategoryEntity::getParentCategory)
                .filter(Objects::nonNull)
                .map(CategoryEntity::getId)
                .collect(Collectors.toSet());

        // 4. 부모 ID에 해당하는 "기타" 소주제 ID 매핑 (DB 조회 없이 Map 사용)
        Map<Long, Long> defaultCategoryMap = Map.of(
                1L, 113L, // 결혼식 -> 기타
                2L, 206L, // 스드메 -> 기타
                3L, 304L, // 예물 예단 -> 기타
                4L, 403L, // 신혼집 -> 기타
                5L, 504L  // 신혼여행 -> 기타
        );

        // 5. 추가할 "기타" 소주제 ID 찾기 (애플리케이션에서 처리, DB 쿼리 없음)
        Set<Long> additionalCategoryIds = parentIds.stream()
                .map(defaultCategoryMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 6. 최종 저장할 소주제 ID 리스트 (선택한 소주제 + 추가된 "기타" 소주제)
        Set<Long> finalCategoryIds = new HashSet<>(selectedCategoryIds);
        finalCategoryIds.addAll(additionalCategoryIds);

        // 7. 최종 저장할 소주제 엔티티 리스트 만들기 (쿼리 없이 메모리에서 필터링)
        List<CategoryEntity> finalCategories = finalCategoryIds.stream()
                .map(subCategoryMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 8. WeddingCategoryEntity와 CategoryExpenseEntity 생성
        List<WeddingCategoryEntity> weddingCategories = new ArrayList<>();
        List<CategoryExpenseEntity> categoryExpenses = new ArrayList<>();

        for (CategoryEntity category : finalCategories) {
            // WeddingCategoryEntity 생성
            WeddingCategoryEntity weddingCategory = new WeddingCategoryEntity();
            weddingCategory.setWedding(wedding);
            weddingCategory.setCategory(category);
            weddingCategories.add(weddingCategory);

            // CategoryExpenseEntity 생성 (0원으로 초기화)
            CategoryExpenseEntity categoryExpense = CategoryExpenseEntity.builder()
                    .wedding(wedding)
                    .category(category)
                    .expense(0)
                    .build();
            categoryExpenses.add(categoryExpense);
        }

        // 9. 저장
        weddingCategoryRepository.saveAll(weddingCategories);
        categoryExpenseRepository.saveAll(categoryExpenses);
    }

    public List<CategoryDTO> mapMainAndSubCategories(List<CategoryEntity> mainCategories,
                                                     List<CategoryEntity> subCategories) {
        Map<Long, CategoryDTO> categoryMap = new HashMap<>();

        // 1. 부모(Main) 카테고리를 먼저 DTO 변환
        for (CategoryEntity main : mainCategories) {
            categoryMap.put(main.getId(), new CategoryDTO(main.getId(), main.getName(), null));
        }

        // 2. 서브(Sub) 카테고리를 DTO로 변환하고 부모에 추가
        for (CategoryEntity sub : subCategories) {
            CategoryDTO subDto = new CategoryDTO(sub.getId(), sub.getName(), null);
            Long parentId = sub.getParentCategory().getId();

            if (categoryMap.containsKey(parentId)) {
                categoryMap.get(parentId).addChild(subDto);
            }
        }
        return new ArrayList<>(categoryMap.values());
    }
}
