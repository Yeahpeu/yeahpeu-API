package com.yeahpeu.event.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProgressDto {

    private int percentage;

    public static ProgressDto of(int completedCount, int totalCount) {
        if (totalCount == 0) {
            return new ProgressDto(0);
        }

        int calculated = completedCount * 100 / totalCount;

        if (calculated > 100) {
            calculated = 100;
        }

        return new ProgressDto(calculated);
    }
}
