package com.yeahpeu.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private String username;
    private String password;
    private String nickname;
    private String role;
}
