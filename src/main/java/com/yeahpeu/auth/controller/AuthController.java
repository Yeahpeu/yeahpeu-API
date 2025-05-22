package com.yeahpeu.auth.controller;

import com.yeahpeu.auth.controller.request.ConfirmVerificationCodeReqeust;
import com.yeahpeu.auth.controller.request.CreateVerificationCodeRequest;
import com.yeahpeu.auth.controller.response.AuthenticationResponse;
import com.yeahpeu.auth.servcie.EmailAuthService;
import com.yeahpeu.auth.servcie.JwtService;
import com.yeahpeu.auth.servcie.dto.JwtDto;
import com.yeahpeu.auth.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "Auth Controller",
        description = "인증, 인가 관련"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    //private final AuthService authService;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    private final EmailAuthService emailAuthService;

    @Operation(
            summary = "Refresh Token 재발급",
            description = "### 사용자의 refreshToken 을 활용해 accessToken을 재발급합니다. \n\n"
                    + "1. 사용자 Header에  Authorization에 갱신된 access token 추가 \n\n"
                    + "2. Cookie에 갱신된 refresh token 추가"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "accessToken 재발급 완료")
    })
    @PostMapping("/reissue")
    public ResponseEntity<AuthenticationResponse> reissue(
            @Parameter(
                    name = "refresh token",
                    description = "JWT Refresh Token"
            )
            @CookieValue(value = "refresh", defaultValue = "no_refresh_token")
            String refreshToken
    ) {

        JwtDto jwtDto = jwtService.reissueRefreshToken(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtDto.getAccessToken());

        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.convertToString(jwtDto.getRefreshToken()));


        return ResponseEntity.ok(new AuthenticationResponse(jwtDto.getAccessToken()));
    }

    @Operation(
            summary = "이메일 인증 코드 요청",
            description = "이메일 인증 코드를 메일로 보냅니다. \n"
                    + "응답으로 이메일 전송 성공 여부와 인증 완료 상태를 내보냅니다."

    )
    @PostMapping("/email-verification/request")
    public ResponseEntity<?> verifyEmail(@RequestBody CreateVerificationCodeRequest request) {
        emailAuthService.createVerificationCode(request.getEmailAddress());

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "이메일 인증 코드 검사",
            description = "이메일로 전달받은 인증 코드를 입력해 이메일을 인증합니다."
    )
    @PostMapping("/email-verification/confirm")
    public ResponseEntity<?> confirmEmail(@RequestBody ConfirmVerificationCodeReqeust request) {
        emailAuthService.validateAuthCode(request.getEmailAddress(), request.getAuthCode());

        return ResponseEntity.ok().build();
    }
}
