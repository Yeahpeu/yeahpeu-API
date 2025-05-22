package com.yeahpeu.user.controller.response;

import com.yeahpeu.user.service.dto.UserProfileDTO;
import com.yeahpeu.wedding.domain.WeddingRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpponentResponse {

    private long id;

    private String name;

    private WeddingRole weddingRole;

    public static OpponentResponse from(UserProfileDTO opponent) {
        return new OpponentResponse(opponent.getId(), opponent.getName(), opponent.getWeddingRole());
    }
}
