package com.yeahpeu.budget.controller.response;

import com.yeahpeu.budget.service.dto.SubCategoryBudgetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SubCategoryBudgetResponse {
    private Long id;
    private String name;
    private Long amount;

    public static SubCategoryBudgetResponse from(SubCategoryBudgetDTO dto) {
        return new SubCategoryBudgetResponse(dto.getId(), dto.getName(), dto.getAmount());
    }
}
