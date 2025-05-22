package com.yeahpeu.user.service.command;

import com.yeahpeu.user.controller.request.UpdateUserProfileRequest;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserProfileCommand {
    private Long userId;
    private String name;
    private String avatarUrl;
    private String nickname;
    private Long budget;
    private ZonedDateTime weddingDay;

    public static UpdateUserProfileCommand from(Long userId, UpdateUserProfileRequest body) {
        return new UpdateUserProfileCommand(
                userId,
                body.getUsername(),
                body.getAvatarUrl(),
                body.getNickname(),
                body.getBudget(),
                body.getWeddingDay()
        );
    }
}
