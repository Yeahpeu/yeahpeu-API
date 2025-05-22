package com.yeahpeu.user.controller;

import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.common.schema.ListResponse;
import com.yeahpeu.user.controller.request.UpdateUserProfileRequest;
import com.yeahpeu.user.controller.request.UserRequest;
import com.yeahpeu.user.controller.response.OpponentResponse;
import com.yeahpeu.user.controller.response.UserProfileResponse;
import com.yeahpeu.user.service.UserService;
import com.yeahpeu.user.service.command.UpdateUserProfileCommand;
import com.yeahpeu.user.service.dto.UserDTO;
import com.yeahpeu.user.service.dto.UserProfileDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "User Controller",
        description = "사용자 정보 관리"
)
@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @Operation(
            summary = "모든 회원 조회",
            description = "등록된 모든 회원을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<ListResponse<UserDTO>> getUsers() {
        List<UserDTO> items = userService.getAllUsers();
        return ResponseEntity.ok(ListResponse.of(items));
    }

    @Operation(
            summary = "회원가입",
            description = "name, email, password 를 받아 회원가입합니다."
    )
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> addUser(
            @RequestBody UserRequest body
    ) {
        UserDTO userDto = userService.addUser(body);
        return ResponseEntity.ok(userDto);
    }

    @Operation(
            summary = "나의 프로필 조회",
            description = "나의 웨딩 프로필을 조회합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        // 나의 프로필 정보 반환
        UserProfileDTO userProfileDTO = userService.getMyProfile(
                Long.valueOf(principal.getUsername())
        );
        return ResponseEntity.ok().body(UserProfileResponse.from(userProfileDTO));
    }

    @Operation(
            summary = "나의 프로필 수정",
            description = "id로 웨딩 프로필을 수정합니다."
    )
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                               @RequestBody UpdateUserProfileRequest body) {
        // 나의 프로필 정보 수정
        UserProfileDTO userProfileDTO = userService.updateMyProfile(UpdateUserProfileCommand.from(
                Long.valueOf(principal.getUsername()), body)
        );

        return ResponseEntity.ok().body(UserProfileResponse.from(userProfileDTO));
    }

    @Operation(
            summary = "나의 요약 프로필 조회",
            description = "나의 요약 프로필을 조회합니다."
    )
    @GetMapping("/me/summary")
    public ResponseEntity<UserDTO> getMySummary(@AuthenticationPrincipal UserPrincipal principal) {
        // 나의 프로필 정보 조회
        UserDTO dto = userService.getMe(Long.valueOf(principal.getUsername()));

        return ResponseEntity.ok().body(dto);
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴합니다."
    )
    @DeleteMapping("/me/withdraw")
    public ResponseEntity<?> withdrawMembership(@AuthenticationPrincipal UserPrincipal principal) {
        userService.deleteUser(Long.valueOf(principal.getUsername()));

        return ResponseEntity.noContent()
                .build();
    }

    @Operation(
            summary = "상대 회원 조회",
            description = "초대코드로 상대방 회원을 조회합니다."
    )
    @GetMapping("/{opponentCode}")
    public ResponseEntity<OpponentResponse> getOpponent(@PathVariable String opponentCode) {
        UserProfileDTO opponent = userService.getOpponent(opponentCode);

        return ResponseEntity.ok(OpponentResponse.from(opponent));
    }
}
