package com.yeahpeu.wedding.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardingCheckDto {

    private boolean onboarded;

    public static BoardingCheckDto of(boolean onboarded) {
        return new BoardingCheckDto(onboarded);
    }
}
