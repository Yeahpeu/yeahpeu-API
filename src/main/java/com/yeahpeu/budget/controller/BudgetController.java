package com.yeahpeu.budget.controller;

import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.budget.controller.response.BudgetSummaryResponse;
import com.yeahpeu.budget.controller.response.MainCategoryBudgetResponse;
import com.yeahpeu.budget.service.BudgetService;
import com.yeahpeu.budget.service.dto.BudgetSummaryDTO;
import com.yeahpeu.budget.service.dto.MainCategoryBudgetDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/wedding/budget")
@RestController
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<MainCategoryBudgetResponse>> getWeddingExpenses(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<MainCategoryBudgetDTO> DTOs = budgetService.getWeddingExpense(
                Long.valueOf(userPrincipal.getUsername()));

        List<MainCategoryBudgetResponse> responses = DTOs.stream()
                .map(MainCategoryBudgetResponse::from)
                .toList();

        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/summary")
    public ResponseEntity<BudgetSummaryResponse> getBudgetSummary(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Long userId = Long.valueOf(userPrincipal.getUsername());
        BudgetSummaryDTO dto = budgetService.getBudgetSummary(userId);

        return ResponseEntity.ok().body(BudgetSummaryResponse.from(dto));
    }


}
