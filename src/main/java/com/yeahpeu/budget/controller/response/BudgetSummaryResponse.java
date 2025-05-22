package com.yeahpeu.budget.controller.response;

import com.yeahpeu.budget.service.dto.BudgetSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BudgetSummaryResponse {
    private Long totalBudget;
    private Long usedBudget;

    public static BudgetSummaryResponse from(BudgetSummaryDTO dto) {
        return new BudgetSummaryResponse(dto.getTotalBudget(), dto.getUsedBudget());
    }
}
