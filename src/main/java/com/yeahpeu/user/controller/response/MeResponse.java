package com.yeahpeu.user.controller.response;

import com.yeahpeu.user.service.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeResponse {
    private Long id;
    private String emailAddress;
    private String nickname;

    public static MeResponse from(UserDTO dto) {
        return new MeResponse(dto.getId(), dto.getEmailAddress(), dto.getName());
    }
}
