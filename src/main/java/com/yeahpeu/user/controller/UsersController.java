package com.yeahpeu.user.controller;

import com.yeahpeu.common.schema.ListResponse;
import com.yeahpeu.user.service.UserService;
import com.yeahpeu.user.service.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ListResponse<UserDTO>> getUsers() {
        List<UserDTO> items = userService.getAllUsers();
        return ResponseEntity.ok(ListResponse.of(items));
    }
}
