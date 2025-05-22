package com.yeahpeu.category.service.dto;

import com.yeahpeu.category.domain.CategoryEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId;
    private List<CategoryDTO> children;

    public CategoryDTO(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.children = new ArrayList<>();
    }

    public void addChild(CategoryDTO child) {
        this.children.add(child);
    }

    public static List<CategoryDTO> fromEntities(List<CategoryEntity> categoryEntities) {
        Map<Long, CategoryDTO> categoryMap = new HashMap<>();

        // 1. 모든 엔티티를 DTO로 변환
        for (CategoryEntity entity : categoryEntities) {
            categoryMap.put(entity.getId(), new CategoryDTO(
                    entity.getId(),
                    entity.getName(),
                    entity.getParentCategory() != null ? entity.getParentCategory().getId() : null
            ));
        }

        // 2. 부모-자식 관계 설정
        List<CategoryDTO> rootCategories = new ArrayList<>();
        for (CategoryDTO dto : categoryMap.values()) {
            if (dto.getParentId() == null || !categoryMap.containsKey(dto.getParentId())) {
                rootCategories.add(dto);
            } else {
                categoryMap.get(dto.getParentId()).addChild(dto);
            }
        }

        return rootCategories;
    }


}
