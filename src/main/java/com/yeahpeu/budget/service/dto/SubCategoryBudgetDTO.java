package com.yeahpeu.budget.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubCategoryBudgetDTO {
    private Long id;
    private String name;
    private Long mainCategoryId;
    private String mainCategoryName;
    private Long amount;
}
