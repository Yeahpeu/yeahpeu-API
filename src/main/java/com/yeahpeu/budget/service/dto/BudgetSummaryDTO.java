package com.yeahpeu.budget.service.dto;

import com.yeahpeu.budget.domain.BudgetEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetSummaryDTO {
    private Long totalBudget;
    private Long usedBudget;

    public static BudgetSummaryDTO from(BudgetEntity entity) {
        return new BudgetSummaryDTO(entity.getTotalBudget(), entity.getUsedBudget());
    }
}
