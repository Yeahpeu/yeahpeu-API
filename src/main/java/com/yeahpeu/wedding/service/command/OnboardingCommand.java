package com.yeahpeu.wedding.service.command;

import com.yeahpeu.wedding.controller.request.OnboardingRequest;
import com.yeahpeu.wedding.domain.WeddingRole;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OnboardingCommand {

    private Long userId; // 나의 아이디
    private WeddingRole weddingRole; // 나의 웨딩 롤, 신랑, 신부
    private ZonedDateTime weddingDay; // 웨딩 날짜
    private Long budget; // 예산
    private List<Long> categoryIds;

    public static OnboardingCommand from(Long userId, OnboardingRequest body) {
        return new OnboardingCommand(
                userId,
                body.getWeddingRole(),
                body.getWeddingDay(),
                body.getBudget(),
                body.getCategoryIds()
        );
    }
}
