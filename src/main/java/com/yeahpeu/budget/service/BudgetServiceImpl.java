package com.yeahpeu.budget.service;

import com.yeahpeu.budget.domain.BudgetEntity;
import com.yeahpeu.budget.repository.BudgetRepository;
import com.yeahpeu.budget.service.dto.BudgetSummaryDTO;
import com.yeahpeu.budget.service.dto.MainCategoryBudgetDTO;
import com.yeahpeu.budget.service.dto.SubCategoryBudgetDTO;
import com.yeahpeu.category.domain.WeddingCategoryEntity;
import com.yeahpeu.category.repository.WeddingCategoryRepository;
import com.yeahpeu.common.exception.ExceptionCode;
import com.yeahpeu.common.exception.NotFoundException;
import com.yeahpeu.event.repository.EventRepository;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BudgetServiceImpl implements BudgetService {
    private final WeddingCategoryRepository weddingCategoryRepository;
    private final BudgetRepository budgetRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<MainCategoryBudgetDTO> getWeddingExpense(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER_ID));

        if (user.getWedding() == null) {
            throw new NotFoundException(ExceptionCode.NOT_FOUND_WEDDING_ID);
        }

        Long weddingId = user.getWedding().getId();

        List<WeddingCategoryEntity> weddingCategories = weddingCategoryRepository.findByWeddingId(
                weddingId);

        List<SubCategoryBudgetDTO> subCategoryBudgets = eventRepository.getCompletedExpensesGroupedBySubCategory(
                weddingId, true);

        Map<Long, SubCategoryBudgetDTO> expenseMap = subCategoryBudgets.stream()
                .collect(Collectors.toMap(SubCategoryBudgetDTO::getId, dto -> dto));

        List<SubCategoryBudgetDTO> finalSubCategoryBudgets = new ArrayList<>();
        for (WeddingCategoryEntity wc : weddingCategories) {
            Long subCategoryId = wc.getCategory().getId();
            String subCategoryName = wc.getCategory().getName();
            Long mainCategoryId = wc.getCategory().getParentCategory().getId();
            String mainCategoryName = wc.getCategory().getParentCategory().getName();

            // 기존 데이터가 있으면 그대로 사용, 없으면 예산 0으로 추가
            SubCategoryBudgetDTO dto = expenseMap.getOrDefault(subCategoryId,
                    new SubCategoryBudgetDTO(subCategoryId, subCategoryName, mainCategoryId, mainCategoryName, 0L)
            );

            finalSubCategoryBudgets.add(dto);
        }
        
        finalSubCategoryBudgets.sort(Comparator.comparingLong(SubCategoryBudgetDTO::getId));

        // 5. 메인 카테고리별로 그룹화
        Map<Long, List<SubCategoryBudgetDTO>> groupedByMainCategory = finalSubCategoryBudgets.stream()
                .collect(Collectors.groupingBy(SubCategoryBudgetDTO::getMainCategoryId));

        // 6. 전체 예산 합산
        long grandTotal = finalSubCategoryBudgets.stream()
                .mapToLong(SubCategoryBudgetDTO::getAmount)
                .sum();

        // 7. 그룹화된 데이터를 기반으로 MainCategoryBudgetDTO 리스트 생성
        return groupedByMainCategory.entrySet().stream()
                .map(entry -> {
                    Long mainCategoryId = entry.getKey();
                    List<SubCategoryBudgetDTO> subCategories = entry.getValue();
                    String mainCategoryName = subCategories.getFirst()
                            .getMainCategoryName(); // 같은 메인 카테고리이므로 첫 번째 항목에서 가져옴

                    // 해당 메인 카테고리의 전체 예산 합산
                    long totalAmount = subCategories.stream()
                            .mapToLong(SubCategoryBudgetDTO::getAmount)
                            .sum();

                    double percentage =
                            grandTotal > 0 ? Math.round((totalAmount / (double) grandTotal) * 100 * 10) / 10.0 : 0.0;

                    return new MainCategoryBudgetDTO(mainCategoryId, mainCategoryName, percentage, totalAmount,
                            subCategories);
                })
                .collect(Collectors.toList());
    }


    @Override
    public BudgetSummaryDTO getBudgetSummary(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER_ID));

        BudgetEntity budget = budgetRepository.findById(user.getWedding().getId())
                .orElseThrow(() -> new NotFoundException("예산이 존재하지 않습니다. 온보딩을 진행해주세요!"));

        return BudgetSummaryDTO.from(budget);
    }

}
