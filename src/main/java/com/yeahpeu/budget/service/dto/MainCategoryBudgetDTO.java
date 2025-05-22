package com.yeahpeu.budget.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MainCategoryBudgetDTO {
    private Long id;
    private String name;
    private Double percentage;
    private Long totalAmount;
    private List<SubCategoryBudgetDTO> subCategories;

}
