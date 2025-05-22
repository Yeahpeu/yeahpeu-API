package com.yeahpeu.user.controller.request;


import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserProfileRequest {
    //    private WeddingRole weddingRole;
    @NotNull
    private String username;
    private String avatarUrl;
    private String nickname;
    private Long budget;
    private ZonedDateTime weddingDay;
}
