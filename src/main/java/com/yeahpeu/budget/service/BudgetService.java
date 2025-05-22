package com.yeahpeu.budget.service;

import com.yeahpeu.budget.service.dto.BudgetSummaryDTO;
import com.yeahpeu.budget.service.dto.MainCategoryBudgetDTO;
import java.util.List;

public interface BudgetService {
    List<MainCategoryBudgetDTO> getWeddingExpense(Long userId);

    BudgetSummaryDTO getBudgetSummary(Long userId);
}
