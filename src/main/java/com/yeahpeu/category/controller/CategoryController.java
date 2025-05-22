package com.yeahpeu.category.controller;

import com.yeahpeu.category.controller.response.CategoryResponse;
import com.yeahpeu.category.service.CategoryService;
import com.yeahpeu.category.service.dto.CategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "Category Controller",
        description = "전체 카테고리 관리"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "전체 카테고리 목록 조회",
            description = "모든 카테고리를 묶어서 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        List<CategoryDTO> allCategoryDTOS = categoryService.findAllCategories();

        List<CategoryResponse> response = allCategoryDTOS.stream()
                .map(CategoryResponse::from)
                .toList();
        return ResponseEntity.ok().body(response);
    }

    @Operation(
            summary = "특정 카테고리 조회",
            description = "카테고리 ID를 통해 관련된 하위 카테고리까지 묶어서 조회합니다."
    )
    @GetMapping("/{categoryId}")
    public ResponseEntity<List<CategoryResponse>> getCategory(@PathVariable Long categoryId) {
        List<CategoryDTO> allCategoryDTOS = categoryService.findCategory(categoryId);

        List<CategoryResponse> response = allCategoryDTOS.stream()
                .map(CategoryResponse::from)
                .toList();
        return ResponseEntity.ok().body(response);
    }


}
