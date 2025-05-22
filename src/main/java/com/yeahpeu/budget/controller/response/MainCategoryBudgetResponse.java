package com.yeahpeu.budget.controller.response;

import com.yeahpeu.budget.service.dto.MainCategoryBudgetDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MainCategoryBudgetResponse {
    private Long id;
    private String name;
    private Double percent;
    private List<SubCategoryBudgetResponse> subCategories;

    public static MainCategoryBudgetResponse from(MainCategoryBudgetDTO dto) {
        return new MainCategoryBudgetResponse(
                dto.getId(),
                dto.getName(),
                dto.getPercentage(),
                dto.getSubCategories().stream()
                        .map(SubCategoryBudgetResponse::from)
                        .toList()
        );
    }

}
