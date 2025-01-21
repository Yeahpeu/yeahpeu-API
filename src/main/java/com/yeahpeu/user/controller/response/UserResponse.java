package com.yeahpeu.user.controller.response;

import com.yeahpeu.user.service.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String emailAddress;
    private String nickname;

    public static UserResponse from(UserDTO dto) {
        return new UserResponse(
                dto.getId(), dto.getEmailAddress(), dto.getName());
    }
}
