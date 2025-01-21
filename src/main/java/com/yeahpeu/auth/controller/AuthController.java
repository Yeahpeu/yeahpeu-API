package com.yeahpeu.auth.controller;

import com.yeahpeu.auth.controller.request.LoginRequest;
import com.yeahpeu.auth.controller.request.SignupRequest;
import com.yeahpeu.auth.controller.response.AuthenticationResponse;
import com.yeahpeu.auth.servcie.AuthService;
import com.yeahpeu.auth.servcie.dto.AuthTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody SignupRequest body) {
        AuthTokenDTO result = authService.signup(body.getEmailAddress(), body.getPassword(), body.getNickname());
        return ResponseEntity.ok(new AuthenticationResponse(result.getAccessToken(), result.getRefreshToken()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest body) {
        AuthTokenDTO result = authService.login(body.getEmailAddress(), body.getPassword());
        return ResponseEntity.ok(new AuthenticationResponse(result.getAccessToken(), result.getRefreshToken()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> reissue(@RequestHeader("Authorization") String refreshToken){
        AuthTokenDTO result = authService.reissueTokens(refreshToken);
        return ResponseEntity.ok(new AuthenticationResponse(result.getAccessToken(), result.getRefreshToken()));
    }

}
