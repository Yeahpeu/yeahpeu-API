package com.yeahpeu.event.service.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map.Entry;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventSummaryDto {

    private LocalDate date;

    private List<CategoryCountDto> categoryCount;

    public static EventSummaryDto from(Entry<LocalDate, List<CategoryCountDto>> entry) {
        return new EventSummaryDto(entry.getKey(), entry.getValue());
    }
}
