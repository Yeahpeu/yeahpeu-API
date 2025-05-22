package com.yeahpeu.event.service.dto;

import java.util.Map.Entry;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryCountDto {
    private Long categoryId;
    private Long count;

    public static CategoryCountDto from(Entry<Long, Long> entry) {
        return new CategoryCountDto(entry.getKey(), entry.getValue());
    }
}
