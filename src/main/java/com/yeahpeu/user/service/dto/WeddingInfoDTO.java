package com.yeahpeu.user.service.dto;

import com.yeahpeu.budget.domain.BudgetEntity;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.wedding.domain.WeddingEntity;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WeddingInfoDTO {
    private String partnerName;
    private Long budget;
    private ZonedDateTime weddingDay;

    public static WeddingInfoDTO from(UserEntity user, WeddingEntity wedding, BudgetEntity budget) {
        String partnerName = null;
        if (wedding.getBride() != null && wedding.getBride().equals(user)) {
            partnerName = wedding.getGroom() != null ? wedding.getGroom().getName() : null;
        } else if (wedding.getGroom() != null && wedding.getGroom().equals(user)) {
            partnerName = wedding.getBride() != null ? wedding.getBride().getName() : null;
        }

        return WeddingInfoDTO.builder()
                .partnerName(partnerName) // null일 경우 그대로 전달
                .budget(budget.getTotalBudget())
                .weddingDay(wedding.getWeddingDay())
                .build();
    }

}
