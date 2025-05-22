package com.yeahpeu.category.controller;

import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.category.controller.response.CategoryResponse;
import com.yeahpeu.category.service.CategoryService;
import com.yeahpeu.category.service.dto.CategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "Wedding Category Controller",
        description = "사용자가 저장한 카테고리 관리"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/wedding/categories")
public class WeddingCategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "사용자가 저장한 카테고리 조회",
            description = "사용자가 저장한 카테고리를 묶어서 전부 조회힙니다."
    )
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getWeddingCategories(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<CategoryDTO> allCategoryDTOS = categoryService.findWeddingCategories(
                Long.valueOf(userPrincipal.getUsername())
        );

        List<CategoryResponse> response = Optional.ofNullable(allCategoryDTOS)
                .orElseGet(List::of) // null이면 빈 리스트 반환
                .stream()
                .map(CategoryResponse::from)
                .toList();

        return ResponseEntity.ok().body(response);
    }

}
