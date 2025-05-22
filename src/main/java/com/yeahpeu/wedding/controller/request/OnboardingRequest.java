package com.yeahpeu.wedding.controller.request;


import com.yeahpeu.wedding.domain.WeddingRole;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OnboardingRequest {

    private WeddingRole weddingRole;
    private ZonedDateTime weddingDay;
    private Long budget;
    private List<Long> categoryIds;
}
