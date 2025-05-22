package com.yeahpeu.category.controller.response;

import com.yeahpeu.category.service.dto.CategoryDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CategoryResponse {
    private Long id;
    private String name;
    private List<CategoryResponse> children = new ArrayList<>();

    public static CategoryResponse from(CategoryDTO dto) {
        return new CategoryResponse(
                dto.getId(),
                dto.getName(),
                getChildren(dto)
        );
    }

    private static List<CategoryResponse> getChildren(CategoryDTO dto) {
        return !dto.getChildren().isEmpty() ?
                dto.getChildren().stream()
                        .map(CategoryResponse::from)
                        .toList() : new ArrayList<>();
    }
}
