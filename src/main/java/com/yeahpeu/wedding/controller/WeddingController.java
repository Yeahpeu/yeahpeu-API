package com.yeahpeu.wedding.controller;

import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.wedding.controller.request.JoinWeddingRequest;
import com.yeahpeu.wedding.controller.request.OnboardingRequest;
import com.yeahpeu.wedding.service.WeddingService;
import com.yeahpeu.wedding.service.command.JoinWeddingCommand;
import com.yeahpeu.wedding.service.command.OnboardingCommand;
import com.yeahpeu.wedding.service.dto.BoardingCheckDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "Wedding Controller",
        description = "결혼 관련 일자, 예산 등 정보 관리"
)
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class WeddingController {
    private final WeddingService weddingService;

    @PostMapping("/onboarding")
    public ResponseEntity<Void> createWeddingPlan(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @RequestBody @Valid OnboardingRequest body) {
        weddingService.processOnboarding(
                OnboardingCommand.from(
                        Long.valueOf(userPrincipal.getUsername()), body
                )
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/onboarding/status")
    public ResponseEntity<BoardingCheckDto> checkOnboardingStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        BoardingCheckDto response = weddingService.isOnboarded(Long.valueOf(userPrincipal.getUsername()));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/wedding/join")
    public ResponseEntity<Void> joinWedding(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @RequestBody JoinWeddingRequest body
    ) {
        weddingService.joinWedding(
                JoinWeddingCommand.from(Long.valueOf(userPrincipal.getUsername()), body)
        );
        return ResponseEntity.ok().build();
    }

}
