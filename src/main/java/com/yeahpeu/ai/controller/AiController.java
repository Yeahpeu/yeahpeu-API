package com.yeahpeu.ai.controller;


import com.yeahpeu.ai.controller.requset.PromptJudgeRequest;
import com.yeahpeu.ai.controller.response.PromptIncomingResponse;
import com.yeahpeu.ai.controller.response.PromptJudgeResponse;
import com.yeahpeu.ai.service.AiChatService;
import com.yeahpeu.auth.domain.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {
    private final AiChatService aiChatService;

    public AiController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

//    @Operation(
//            summary = "사용자의 일정 주의사항 알려주는 AI",
//            description = "사용자가 입력한 일정 기반으로 준비해야 할 사항을 알려줍니다."
//    )
//    @PostMapping("/recommend")
//    public ResponseEntity<PromptEventResponse> recommendEvent(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
//            @RequestBody
//                    PromptRequest promptRequest
//    ) {
//        System.out.println(promptRequest.getNativePrompt());
//        PromptEventResponse response = aiChatService.generateEventResponse(promptRequest, extractUserId(userPrincipal));
//
//        return ResponseEntity.ok(response);
//    }

    @Operation(
            summary = "부부 간 잘못된 점을 판단해주는 AI",
            description = "사용자가 입력한 일정 기반으로 준비해야 할 사항을 알려줍니다."
    )
    @PostMapping("/judge")
    public ResponseEntity<PromptJudgeResponse> judge(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody
            PromptJudgeRequest promptRequest
    ) {
        PromptJudgeResponse response = aiChatService.judgeResponse(promptRequest, extractUserId(userPrincipal));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/incoming")
    public ResponseEntity<PromptIncomingResponse> recommendIncoming(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PromptIncomingResponse response = aiChatService.generateIncomingEventResponse(extractUserId(userPrincipal));

        return ResponseEntity.ok(response);
    }

    private static Long extractUserId(UserPrincipal userPrincipal) {
        return Long.valueOf(userPrincipal.getUsername());
    }


}
